package com.example.esamepo.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.esamepo.exception.ServerException;
import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.WordSelection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * class for handling some json operations
 */
public class JSONUtils {

    /**
     * method for extracting a JsonNode from an input url, handling various errors
     * @see com.fasterxml.jackson.databind.JsonNode
     * @param inputURL the URL from which to extract the JsonNode
     * @return the extracted JsonNode
     * @throws ServerException if the input url is malformed
     * @throws ServerException if there is an error in the json parsing
     * @throws ServerException if there is a connection error
     */
    public static JsonNode UrlToJsonNode(String inputURL) {

        ObjectMapper objectMapper = new ObjectMapper();
        URL url;
        JsonNode outputNode;

        try {
            url = new URL(inputURL);
        } catch (MalformedURLException e) {
            throw new ServerException("Invalid URL " + inputURL,
                                      "Refer to https://tools.ietf.org/html/rfc1738 for the standard format");
        }

        try {
            outputNode = objectMapper.readTree(url);
        } catch (JsonProcessingException e) {
            throw new ServerException("JSON parsing error",
                                      "Refer to https://tools.ietf.org/html/rfc8259 for the standard format");
        } catch (IOException e) {
            throw new ServerException("Connection to " + inputURL + " failed",
                                      "Please contact the server administrator");
        }

        return outputNode;
    }

    /**
     * method that extract an arraylist of a model class from a JsonNode, handling parsing errors
     * @see com.fasterxml.jackson.databind.JsonNode
     *
     * @param inputNode the node from which to extract the array
     * @param model the model class
     * @return the array oof model class extracted from the JsonNode
     */
    public static <T> ArrayList<T> JsonNodeToArrayList(JsonNode inputNode, Class<T> model) {

        ArrayList<T> outputList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Iterator<JsonNode> ite = inputNode.elements();

        while (ite.hasNext()) {
            JsonNode temp = ite.next();
            try {
                outputList.add(objectMapper.treeToValue(temp, model));
            } catch (JsonProcessingException e) {
                throw new ServerException("JSON parsing error",
                                          "Refer to https://tools.ietf.org/html/rfc8259 for the standard format");
            }
        }

        return outputList;
    }


    /**
     * method that extract a WordSelection object from a json formatted string input
     * @see WordSelection
     *
     * @param rawInput json formatted string to be converted to WordSelection object
     * @return WordSelection containing the data in the input string
     * @throws UserException if the input string is not correctly formatted
     */
    public static WordSelection RawInputToWordSelection(String rawInput) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode inputNode = objectMapper.readTree(rawInput);
            //Deserialized data sent via HTTP POST request into WordSelection class
            return objectMapper.treeToValue(inputNode, WordSelection.class);
        } catch (JsonProcessingException e) {
            throw new UserException("JSON parsing error",
                                    "Refer to https://tools.ietf.org/html/rfc8259 for the standard format");
        }

    }
}
