package com.example.esamepo.utils;

import com.example.esamepo.model.TldDescription;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TldDescriptionSerializer  extends StdSerializer<TldDescription> {

        public TldDescriptionSerializer() {
            this(null);
        }

        public TldDescriptionSerializer(Class<TldDescription> t) {
            super(t);
        }

    @Override
    public void serialize(TldDescription tldDescription, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("Name", tldDescription.getName());
        jsonGenerator.writeStringField("includes", tldDescription.getIncludes().toString());
        jsonGenerator.writeStringField("descriptions", tldDescription.getDescription().toString());
        if (tldDescription.getDomainsCount()!=-1)
            jsonGenerator.writeNumberField("domainscount", tldDescription.getDomainsCount());
        jsonGenerator.writeEndObject();

    }
}
