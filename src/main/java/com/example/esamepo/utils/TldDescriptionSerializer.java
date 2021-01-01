package com.example.esamepo.utils;

import com.example.esamepo.model.TldDescription;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class TldDescriptionSerializer  extends StdSerializer<TldDescription> {

    private static final long serialVersionUID = 1L;

    public TldDescriptionSerializer() {
            this(null);
        }

        public TldDescriptionSerializer(Class<TldDescription> t) {
            super(t);
        }

    @Override
    public void serialize(TldDescription tldDescription, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("name", tldDescription.getName());

        if (tldDescription.getIncludes() != null) {
            jsonGenerator.writeArrayFieldStart("includes");
            for (String s : tldDescription.getIncludes()){
                jsonGenerator.writeString(s);
            }
            jsonGenerator.writeEndArray();
        }

        if (tldDescription.getDomainsCount() != -1) {
            jsonGenerator.writeNumberField("domainsCount", tldDescription.getDomainsCount());
        }

        jsonGenerator.writeArrayFieldStart("description");
        for (String s : tldDescription.getDescription()){
            jsonGenerator.writeString(s);
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
