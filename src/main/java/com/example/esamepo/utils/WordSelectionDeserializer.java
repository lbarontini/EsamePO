package com.example.esamepo.utils;

import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.WordSelection;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * deserializer for the class Word selection
 * @see com.fasterxml.jackson.databind.deser.std.StdDeserializer
 */
public class WordSelectionDeserializer extends StdDeserializer<WordSelection> {

    /**
     * needed for the serialization of StdDeserializer
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor needed by jackson
     */
    public WordSelectionDeserializer() {
        this(null);
    }

    /**
     * constructor needed by jackson
     */
    protected WordSelectionDeserializer(Class<WordSelection> vc) {
        super(vc);
    }

    /**
     * override of the abstract method for handling deserialization from json to WordSelection
     * @throws IOException in case of deserialization errors
     */
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
