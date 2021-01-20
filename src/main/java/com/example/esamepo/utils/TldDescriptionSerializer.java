package com.example.esamepo.utils;

import com.example.esamepo.model.TldDescription;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * serializer for the class TldDescription
 * it generate a json with the field that are non null (different from -1 in case of DomainsCount)
 */
public class TldDescriptionSerializer  extends StdSerializer<TldDescription> {

    /**
     * needed for the serialization of StdSerializer
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor needed by jackson
     */
    public TldDescriptionSerializer() {
        this(null);
    }

    /**
     * constructor needed by jackson
     */
    public TldDescriptionSerializer(Class<TldDescription> t) {
        super(t);
    }

    /**
     * override of the abstract method for handling serialization from TldDescription to json
     *
     * @throws IOException in case of serialization errors
     */
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
