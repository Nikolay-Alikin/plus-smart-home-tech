package app.avro;

import org.apache.avro.specific.SpecificRecordBase;

public interface RecordBuilder<T> {

    SpecificRecordBase buildSpecificRecordBase(T data);
}
