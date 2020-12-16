package com.example.esamepo.controller;

import com.example.esamepo.model.TldClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
