package com.example.esamepo.utils;

import com.example.esamepo.model.TldClass;
import com.example.esamepo.model.TldInputModel;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

//i wanted to test if we can make cleaner code using custom deserializer
public class TldInputDeserializer extends StdDeserializer<TldInputModel> {

    public TldInputDeserializer() {
        this(null);
    }

    protected TldInputDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TldInputModel deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
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
       return new TldInputModel(name,includes,description);
    }
}
