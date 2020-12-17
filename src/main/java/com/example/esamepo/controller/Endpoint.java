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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

@RestController
public class Endpoint {

    @GetMapping("/listAll")
    public ResponseEntity<ArrayList<TldClass>> listAll() {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TldClass> tlds = new ArrayList<>();

        try {
            URL url = new URL("https://api.domainsdb.info/v1/info/tld/");

            JsonNode jsonNode = objectMapper.readTree(url).get("includes");

            if (jsonNode == null){
                throw new ApiSchemaException("API error",
                                             "The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                                             "Please contact the server administrator");
            }

            Iterator<JsonNode> ite = jsonNode.elements();

            while (ite.hasNext()) {
                JsonNode temp = ite.next();
                tlds.add(objectMapper.treeToValue(temp, TldClass.class));
            }

        } catch (IOException | NoSuchElementException | ClassCastException e) {

            throw new ApiSchemaException("API error",
                                         "The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                                         "Please contact the server administrator");

        }

        return ResponseEntity.ok(tlds);
    }


    @GetMapping("/rank")
    public ResponseEntity<ArrayList<TldDescription>> rank(@RequestParam(name = "type") String type,
                                                          @RequestParam(name = "tld", required = false) String tld){

        if (type.equals("size")) {

            ObjectMapper objectMapper = new ObjectMapper();

            //Reusing other endpoint here
            ArrayList<TldClass> tlds = listAll().getBody();
            ArrayList<TldDescription> rankedTLDs = new ArrayList <>();

            //This should iterate over all TLDs, but API is slow so will only fetch first 10 for the demo
            for (int tldIndex = 0; tldIndex < 10; tldIndex++) {

                try {
                    String thisTLDName = tlds.get(tldIndex).getName();
                    URL url = new URL("https://api.domainsdb.info/v1/info/stat/" + thisTLDName);

                    JsonNode arrayNode = objectMapper.readTree(url).get("statistics");

                    //objectMapper.readTree(url) can return an unexpected JSON, which means the statistics array
                    //may not be present
                    if (arrayNode == null){
                        throw new ApiSchemaException("API error",
                                                     "The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                                     "Please contact the server administrator");
                    }

                    Iterator<JsonNode> ite = arrayNode.elements();
                    JsonNode firstNode = ite.next();

                    int thisTLDSize = firstNode.get("total").asInt();
                    //Exceptions thrown by info(thisTLDName) are already handled automatically
                    ArrayList<String> thisTLDDescription = info(thisTLDName).getBody().getDescription();

                    TldDescription generatedObject = new TldDescription(thisTLDName, thisTLDSize, thisTLDDescription);
                    rankedTLDs.add(generatedObject);

                } catch (NoSuchElementException | ClassCastException | IOException e) {

                    //Seems like you can't distinguish between a connection error and nonexistent endpoint
                    //on the upstream API using readtree(url): both throw IOException
                    throw new ApiSchemaException("API error",
                                                 "The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                                 "Please contact the server administrator");
                }

            }

            Collections.sort(rankedTLDs);

            //From largest to smallest TLD
            Collections.reverse(rankedTLDs);

            return ResponseEntity.ok(rankedTLDs);

        } else if (type.equals("keyword")){

            //Handle ranking by keyword

            return null;

        } else {

            throw new BadRequestException("API error",
                                          "Invalid ranking parameters",
                                          "Use /rank?type=size to rank by TLD size and /rank?type=keyword&tld={tld} to rank by most used keywords within a TLD");

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
