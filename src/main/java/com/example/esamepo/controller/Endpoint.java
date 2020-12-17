package com.example.esamepo.controller;

import com.example.esamepo.exception.ApiSchemaException;
import com.example.esamepo.exception.BadRequestException;
import com.example.esamepo.model.TldDescription;
import com.example.esamepo.model.TldClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

@RestController
public class Endpoint {

    @GetMapping("/listAll")
    public ArrayList<TldClass> listAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TldClass> tlds = new ArrayList<>();

        try {
            URL url = new URL("https://api.domainsdb.info/v1/info/tld/");
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            InputStream is = httpcon.getInputStream();
            JsonNode jsonNode = objectMapper.readTree(is).get("includes");
            //checking response code dot know if it is useful for handling errors
            if(httpcon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Iterator<JsonNode> ite = jsonNode.elements();
                while (ite.hasNext()) {
                    JsonNode temp = ite.next();
                    tlds.add(objectMapper.treeToValue(temp, TldClass.class));
                }
            }else {
                httpcon.disconnect();
                return null;
            }
            httpcon.disconnect();
            return tlds;
        } catch (IOException | NoSuchElementException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/rank")
    public ArrayList<TldDescription> rank(@RequestParam(name = "type") String type,
                                          @RequestParam(name = "tld", required = false) String tld) {

        if (type.equals("size")) {

            ObjectMapper objectMapper = new ObjectMapper();

            //Reusing other endpoint here
            ArrayList<TldClass> tlds = listAll();
            if (tlds == null){
                return null;
            }

            ArrayList<TldDescription> rankedTLDs = new ArrayList <>();

            //This should iterate over all TLDs, but API is slow so will only fetch first 10 for the demo
            for (int tldIndex = 0; tldIndex < 10; tldIndex++) {

                try {
                    URL url = new URL("https://api.domainsdb.info/v1/info/stat/" + tlds.get(tldIndex).getName());

                    //objectMapper.readTree(url) can return an unexpected JSON, which means .get(String) won't work properly
                    JsonNode arrayNode = objectMapper.readTree(url).get("statistics");
                    Iterator<JsonNode> ite = arrayNode.elements();
                    JsonNode firstNode = ite.next();

                    String thisTLDName = firstNode.get("zone").asText();
                    int thisTLDSize = firstNode.get("total").asInt();

                    //Not populating the description here, waiting for other endpoint to be implemented so it can be reused
                    TldDescription generatedObject = new TldDescription(thisTLDName, thisTLDSize);
                    rankedTLDs.add(generatedObject);

                } catch (IOException | NoSuchElementException | ClassCastException e) {

                    //Handle errors
                    e.printStackTrace();

                    return null;
                }

            }

            Collections.sort(rankedTLDs);

            //From largest to smallest TLD
            Collections.reverse(rankedTLDs);

            return rankedTLDs;
        } else if (type.equals("keyword")){

            //Handle ranking by keyword

            return null;

        } else {

            return null;
        }

    }

    @GetMapping("/info")
    public ResponseEntity<TldDescription> info(@RequestParam(name = "tld", defaultValue = "null") String tld) throws BadRequestException {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> description = new ArrayList<>();
        try {
            URL url = new URL("https://api.domainsdb.info/v1/info/tld/" + tld);
            JsonNode jsonNode= objectMapper.readTree(url).get("description");
            //jsonnode can be null if the schema of the api changes
            if (jsonNode ==null)
                throw new ApiSchemaException("Api error","the api schema is changed in: https://api.domainsdb.info/v1/info/tld","please contact the server administrator");

            Iterator<JsonNode> ite = jsonNode.elements();
            while (ite.hasNext()) {
                JsonNode temp = ite.next();
                description.add(temp.asText());
            }
        }catch (IOException e)
        {
           throw new BadRequestException("API Error","The selected TLD does Not Exist","use /listAll for a list of tlds");
        }catch (NoSuchElementException | ClassCastException e){
            throw new ApiSchemaException("Api error","the api schema is changed in: https://api.domainsdb.info/v1/info/tld","please contact the server administrator");
        }
        return ResponseEntity.ok(new TldDescription(tld, description));
    }
}
