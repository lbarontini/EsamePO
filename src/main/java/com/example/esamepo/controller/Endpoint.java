package com.example.esamepo.controller;

import com.example.esamepo.model.TldClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.assertj.core.util.Lists;
import org.json.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


@RestController
public class Endpoint {

    @GetMapping("/listAll")
    public ArrayList<TldClass> listAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TldClass> tlds = new ArrayList<>();
        try {
            URL url = new URL("https://api.domainsdb.info/v1/info/tld/");
            JsonNode jsonNode = objectMapper.readTree(url).get("includes");

            Iterator<JsonNode> ite = jsonNode.elements();
            while (ite.hasNext()) {
                JsonNode temp = ite.next();
                tlds.add(objectMapper.treeToValue(temp, TldClass.class));
            }
            return tlds;
        }catch (IOException | NoSuchElementException | ClassCastException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
