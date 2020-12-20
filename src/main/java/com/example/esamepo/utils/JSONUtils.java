package com.example.esamepo.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.esamepo.exception.ServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

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

    public static <T> ArrayList<T> jsonArrayToArrayList(JsonNode inputNode, Class<T> model) {

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
}
