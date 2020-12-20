package com.example.esamepo.controller;

import com.example.esamepo.exception.ServerException;
import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.TldDescription;
import com.example.esamepo.utils.JSONUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@RestController
public class Endpoint {

    @GetMapping("/listAll")
    public ResponseEntity<ArrayList<TldClass>> listAll() {

        JsonNode tldsNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/tld/").get("includes");

        if (tldsNode == null) {
            throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                                      "Please contact the server administrator");
        }

        ArrayList<TldClass> tlds = JSONUtils.jsonArrayToArrayList(tldsNode, TldClass.class);

        return ResponseEntity.ok(tlds);
    }

    @GetMapping("/rank")
    public ResponseEntity<ArrayList<TldDescription>> rank(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {

        ArrayList<TldClass> tlds = listAll().getBody();
        ArrayList<TldDescription> rankedTLDs = new ArrayList<>();

        // This should iterate over all TLDs, but API is slow so will only fetch first {count} for the demo
        for (int tldIndex = 0; tldIndex < count; tldIndex++) {

            String thisTLDName = tlds.get(tldIndex).getName();
            JsonNode arrayNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/stat/" + thisTLDName).get("statistics");

            if (arrayNode == null) {
                throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                          "Please contact the server administrator");
            }

            Iterator<JsonNode> ite = arrayNode.elements();
            JsonNode firstNode = ite.next();

            try {
                //Returns null if field is missing
                int thisTLDSize = firstNode.get("total").asInt();

                // Exceptions thrown by info(thisTLDName) are already handled automatically
                ArrayList<String> thisTLDDescription = info(thisTLDName).getBody().getDescription();

                TldDescription generatedObject = new TldDescription(thisTLDName, thisTLDSize, thisTLDDescription);
                rankedTLDs.add(generatedObject);

            } catch (NullPointerException e){
                throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                          "Please contact the server administrator");
            }

        }

        Collections.sort(rankedTLDs);
        // From largest to smallest TLD
        Collections.reverse(rankedTLDs);

        return ResponseEntity.ok(rankedTLDs);
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
