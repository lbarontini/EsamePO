package com.example.esamepo.utils;

import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.WordSelection;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;

public class WordSelectionDeserializer extends StdDeserializer<WordSelection> {

    private static final long serialVersionUID = 1L;

    public WordSelectionDeserializer() {
        this(null);
    }

    protected WordSelectionDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WordSelection deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        try {
            JsonNode inputNode = p.getCodec().readTree(p);
            String inputTLD = inputNode.get("tld").asText();
            ArrayList<String> inputWords = JSONUtils.JsonNodeToArrayList(inputNode.get("words"), String.class);
            return new WordSelection(inputTLD, inputWords);
        } catch (NullPointerException e) {
            throw new UserException("Invalid JSON format",
                                    "Use format {\"tld\":\"YOUR_TLD_HERE\",\"words\": [\"WORD_1\", \"WORD_2\", ..., \"WORD_N\"]}");
        }
   
    }
}
