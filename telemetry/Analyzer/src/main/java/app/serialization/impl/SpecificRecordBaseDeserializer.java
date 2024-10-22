package app.serialization.impl;

import app.serialization.GeneralAvroDeserializer;
import org.apache.avro.specific.SpecificRecordBase;

public class SpecificRecordBaseDeserializer extends GeneralAvroDeserializer<SpecificRecordBase> {

    public SpecificRecordBaseDeserializer() {
        super(SpecificRecordBase.class);
    }
}
