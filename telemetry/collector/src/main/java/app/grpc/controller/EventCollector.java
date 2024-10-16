package app.grpc.controller;

import app.grpc.handler.HubProtoHandler;
import app.grpc.handler.SensorProtoHandler;
import app.model.hub.event.HubEvent;
import app.model.sensor.event.SensorEvent;
import app.service.impl.HubService;
import app.service.impl.SensorService;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc.CollectorControllerImplBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@GrpcService
public class EventCollector extends CollectorControllerImplBase {

    private final HubService hubService;
    private final SensorService sensorService;
    private final Map<HubEventProto.PayloadCase, HubProtoHandler<? extends HubEvent>> hubHandlers;
    private final Map<SensorEventProto.PayloadCase, SensorProtoHandler<? extends SensorEvent>> sensorHandlers;

    public EventCollector(Set<SensorProtoHandler<? extends SensorEvent>> sensorHandlers,
            SensorService sensorService, HubService hubService, Set<HubProtoHandler<? extends HubEvent>> hubHandlers) {
        this.hubService = hubService;
        this.sensorService = sensorService;
        this.sensorHandlers = sensorHandlers.stream()
                .collect(Collectors.toMap(SensorProtoHandler::getMessageType, Function.identity()));
        this.hubHandlers = hubHandlers.stream()
                .collect(Collectors.toMap(HubProtoHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            sensorService.transmit(buildSensorEvent(request));
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    new StatusRuntimeException(Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            hubService.transmit(buildHubEvent(request));
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    new StatusRuntimeException(Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)));
        }
    }

    private SensorEvent buildSensorEvent(SensorEventProto request) {
        SensorEventProto.PayloadCase payloadCase = request.getPayloadCase();

        if (!sensorHandlers.containsKey(payloadCase)) {
            throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
        }
        return sensorHandlers.get(payloadCase).handle(request);
    }

    private HubEvent buildHubEvent(HubEventProto request) {
        HubEventProto.PayloadCase payloadCase = request.getPayloadCase();

        if (!hubHandlers.containsKey(payloadCase)) {
            throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
        }
        return hubHandlers.get(payloadCase).handle(request);
    }
}
