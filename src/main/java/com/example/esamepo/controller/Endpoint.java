package com.example.esamepo.controller;

import com.example.esamepo.exception.ServerException;
import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.TldDescription;
import com.example.esamepo.model.TLDStats;
import com.example.esamepo.model.TldClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

@RestController
public class Endpoint {

    @GetMapping("/listAll")
    public ResponseEntity<ArrayList<TldClass>> listAll() {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TldClass> tlds = new ArrayList<>();

        try {
            URL url = new URL("https://api.domainsdb.info/v1/info/tld/");

            JsonNode jsonNode = objectMapper.readTree(url).get("includes");

            if (jsonNode == null) {
                throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                        "Please contact the server administrator");
            }

            Iterator<JsonNode> ite = jsonNode.elements();

            while (ite.hasNext()) {
                JsonNode temp = ite.next();
                tlds.add(objectMapper.treeToValue(temp, TldClass.class));
            }

        } catch (IOException | NoSuchElementException | ClassCastException e) {

            throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                    "Please contact the server administrator");
            // todo improve exception handling
        }

        return ResponseEntity.ok(tlds);
    }

    @GetMapping("/rank")
    public ResponseEntity<ArrayList<TldDescription>> rank(@RequestParam(name = "type") String type,
            @RequestParam(name = "tld", required = false) String tld) {

        if (type.equals("size")) {

            ObjectMapper objectMapper = new ObjectMapper();

            // Reusing other endpoint here
            ArrayList<TldClass> tlds = listAll().getBody();
            ArrayList<TldDescription> rankedTLDs = new ArrayList<>();

            // This should iterate over all TLDs, but API is slow so will only fetch first
            // 10 for the demo
            for (int tldIndex = 0; tldIndex < 10; tldIndex++) {

                try {
                    assert tlds != null;
                    String thisTLDName = tlds.get(tldIndex).getName();
                    // URL url = new URL("https://api.domainsdb.info/v1/info/stat/" + thisTLDName);
                    URL url = new URL("https://api.domainsdb.invalid");

                    JsonNode arrayNode = objectMapper.readTree(url).get("statistics");

                    // objectMapper.readTree(url) can return an unexpected JSON, which means the
                    // statistics array
                    // may not be present
                    if (arrayNode == null) {
                        throw new ServerException(
                                "The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                "Please contact the server administrator");
                    }

                    Iterator<JsonNode> ite = arrayNode.elements();
                    JsonNode firstNode = ite.next();

                    int thisTLDSize = firstNode.get("total").asInt();
                    // Exceptions thrown by info(thisTLDName) are already handled automatically
                    ArrayList<String> thisTLDDescription = info(thisTLDName).getBody().getDescription();

                    TldDescription generatedObject = new TldDescription(thisTLDName, thisTLDSize, thisTLDDescription);
                    rankedTLDs.add(generatedObject);

                } catch (IOException e) {
                    throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                            "Please contact the server administrator");

                } catch (NoSuchElementException | ClassCastException e) {

                    // Seems like you can't distinguish between a connection error and nonexistent
                    // endpoint
                    // on the upstream API using readtree(url): both throw IOException
                    // throw new ApiSchemaException("API error exception",
                    // "The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                    // "Please contact the server administrator");
                    e.printStackTrace();
                }

            }

            Collections.sort(rankedTLDs);
            // From largest to smallest TLD
            Collections.reverse(rankedTLDs);

            return ResponseEntity.ok(rankedTLDs);

        } else if (type.equals("keyword")) {

            // Handle ranking by keyword

            return null;
        } else {
            throw new UserException("Invalid ranking parameters",
                    "Use /rank?type=size to rank by TLD size and /rank?type=keyword&tld={tld} to rank by most used keywords within a TLD");
        }
    }

    @PostMapping(path = "/stats", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArrayList<TLDStats>> stats(@RequestBody String filter) {

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<TLDStats> allTLDStats = new ArrayList<>();

        try {
            JsonNode filterNode = objectMapper.readTree(filter);

            String tld = filterNode.get("tld").asText();

            JsonNode wordsNode = filterNode.get("words");

            Iterator<JsonNode> ite = wordsNode.elements();

            while (ite.hasNext()) {
                JsonNode wordNode = ite.next();
                String singleWord = wordNode.asText();

                try {
                    URL url = new URL("https://api.domainsdb.info/v1/domains/search?domain=" + singleWord + "&zone=" + tld);
                    JsonNode jsonNode = objectMapper.readTree(url).get("domains");

                    ArrayList<String> matchingDomains = new ArrayList<>();
                    Iterator<JsonNode> ite2 = jsonNode.elements();

                    while (ite2.hasNext()) {
                        JsonNode domainNode = ite2.next();
                        String singleDomain = domainNode.get("domain").asText();
                        matchingDomains.add(singleDomain);
                    }

                int matchesCount = matchingDomains.size();

                TLDStats thisTldStats = new TLDStats(tld, matchesCount, matchingDomains, singleWord);

                allTLDStats.add(thisTldStats);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    return null;
                }

            }

            return ResponseEntity.ok(allTLDStats);

        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return null;
        }

    }

    @GetMapping("/info")
    public ResponseEntity<TldDescription> info(@RequestParam(name = "tld", defaultValue = "null") String tld) throws UserException {

        ArrayList<TldClass> tlds = listAll().getBody();

        assert tlds != null;
        if (tlds.contains(new TldClass(tld))) {
           ObjectMapper objectMapper = new ObjectMapper();
           ArrayList<String> description = new ArrayList<>();
           try {
               URL url = new URL("https://api.domainsdb.info/v1/info/tld/" + tld);
               JsonNode jsonNode = objectMapper.readTree(url).get("description");
               //jsonNode can be null if the schema of the api changes
               if (jsonNode == null)
                   throw new ServerException("the api schema is changed in: https://api.domainsdb.info/v1/info/tld",
                           "please contact the server administrator");

               Iterator<JsonNode> ite = jsonNode.elements();
               while (ite.hasNext()) {
                   JsonNode temp = ite.next();
                   description.add(temp.asText());
               }
           } catch (IOException e) {
               //launched if the server is not responding
               throw new ServerException("https://api.domainsdb is not responding", "please contact the server administrator");
           } catch (Exception e) {
               //general exeption handler
               throw new ServerException("Internal error", "please contact the server administrator");
           }
           return ResponseEntity.ok(new TldDescription(tld, description));
       }else
           throw new UserException("The selected TLD does Not Exist",
                   "use /listAll for a list of all tlds");
    }
}
