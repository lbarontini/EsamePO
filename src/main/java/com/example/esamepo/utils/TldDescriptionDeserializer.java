package com.example.esamepo.utils;

import com.example.esamepo.model.TldDescription;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * deserializer for the class TldDescription
 * @see com.fasterxml.jackson.databind.deser.std.StdDeserializer
 */
public class TldDescriptionDeserializer extends StdDeserializer<TldDescription> {

    private static final long serialVersionUID = 1L;

    public TldDescriptionDeserializer() {
        this(null);
    }

    protected TldDescriptionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TldDescription deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String name= node.get("zone").asText();
        ArrayList<String> includes = new ArrayList<>();
        Iterator<JsonNode> ite = node.get("includes").elements();
        while (ite.hasNext()) {
            JsonNode temp = ite.next();
            includes.add(temp.asText());
        }
        ArrayList<String> description = new ArrayList<>();
        ite = node.get("description").elements();
        while (ite.hasNext()) {
            JsonNode temp = ite.next();
            description.add(temp.asText());
        }
       return new TldDescription(name,includes,description);
    }
}
