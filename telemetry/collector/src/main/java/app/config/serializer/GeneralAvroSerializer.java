package app.config.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class GeneralAvroSerializer implements Serializer<SpecificRecordBase> {

    private BinaryEncoder encoder;
    private final EncoderFactory encoderFactory = EncoderFactory.get();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] result = null;
        encoder = encoderFactory.binaryEncoder(out, encoder);
        if (data != null) {
            DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(data.getSchema());
            try {
                writer.write(data, encoder);
                encoder.flush();
            } catch (IOException e) {
                throw new SerializationException("Ошибка сериализации данных для топика [" + topic + "]", e);
            }
            result = out.toByteArray();
        }
        return result;
    }

    @Override
    public byte[] serialize(String topic, Headers headers, SpecificRecordBase data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
