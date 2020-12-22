package com.example.esamepo.controller;

import com.example.esamepo.exception.ServerException;
import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.*;
import com.example.esamepo.utils.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class Endpoint {

    @GetMapping("/listAll")
    public ResponseEntity<ArrayList<TldName>> listAll() {

        JsonNode tldsNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/tld/").get("includes");

        if (tldsNode == null) {
            throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                                      "Please contact the server administrator");
        }

        ArrayList<TldName> tlds = JSONUtils.JsonNodeToArrayList(tldsNode, TldName.class);

        return ResponseEntity.ok(tlds);
    }

    @GetMapping("/rank")
    public ResponseEntity<ArrayList<TldDescription>> rank(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {

        ArrayList<TldName> tlds = listAll().getBody();
        ArrayList<TldDescription> rankedTLDs = new ArrayList<>();

        // This should iterate over all TLDs, but API is slow so will only fetch first
        // {count} for the demo
        for (int tldIndex = 0; tldIndex < count; tldIndex++) {

            String thisTLDName = tlds.get(tldIndex).getName();
            JsonNode arrayNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/stat/" + thisTLDName).get("statistics");

            if (arrayNode == null) {
                throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                          "Please contact the server administrator");
            }

            // Empty array if TLD never surveyed
            JsonNode firstNode = arrayNode.get(0);

            if (firstNode == null) {
                throw new ServerException("Missing historical data for TLD " + thisTLDName,
                                          "Please contact the server administrator");
            }

            try {
                // Returns null if field is missing
                int thisTLDSize = firstNode.get("total").asInt();

                // Exceptions thrown by info(thisTLDName) are already handled automatically
                ArrayList<String> thisTLDDescription = info(thisTLDName).getBody().getDescription();

                TldDescription generatedObject = new TldDescription(thisTLDName, thisTLDSize, thisTLDDescription);
                rankedTLDs.add(generatedObject);

            } catch (NullPointerException e) {
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
    public ResponseEntity<ArrayList<WordUsage>> wordStats(@RequestBody String filter) {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<WordUsage> allTldStats = new ArrayList<>();

        try {
            JsonNode inputNode = objectMapper.readTree(filter);

            String inputTLD = inputNode.get("tld").asText();
            ArrayList<String> inputWords = JSONUtils.JsonNodeToArrayList(inputNode.get("words"), String.class);

            ArrayList<TldName> validTLDs = listAll().getBody();

            if (!validTLDs.contains(new TldName(inputTLD))) {
                throw new UserException("The selected TLD does Not Exist",
                                        "Use /listAll for a list of all TLDs");
            }

            for (String singleWord : inputWords){

                try {
                    int matchesCount = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/domains/search?domain=" + singleWord + "&zone=" + inputTLD).get("total").asInt();

                    WordUsage thisWordUsage = new WordUsage(inputTLD, matchesCount, singleWord);
                    allTldStats.add(thisWordUsage);

                    } catch (NullPointerException e) {
                        throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                                  "Please contact the server administrator");
                }
            }

        } catch (NullPointerException e) {
            throw new UserException("Invalid JSON filter",
                                    "Use format {\"tld\":\"YOUR_TLD_HERE\",\"words\": [\"WORD_1\", \"WORD_2\", ..., \"WORD_N\"]}");
        } catch (JsonProcessingException e) {
            throw new UserException("JSON parsing error",
                                    "Refer to https://tools.ietf.org/html/rfc8259 for the standard format");
        }

        Collections.sort(allTldStats);
        Collections.reverse(allTldStats);

        return ResponseEntity.ok(allTldStats);
    }

    @GetMapping("/info")
    public ResponseEntity<TldDescription> info(@RequestParam(name = "tld", defaultValue = "null") String tld) {

        ArrayList<TldName> tlds = listAll().getBody();
        assert tlds != null;
        if (tlds.contains(new TldName(tld))) {
            JsonNode jsonNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/tld/" + tld);
            //jsonNode can be null if the schema of the api changes
            if (jsonNode == null)
                throw new ServerException("the api schema is changed in: https://api.domainsdb.info/v1/info/tld",
                        "please contact the server administrator");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return ResponseEntity.ok(objectMapper.treeToValue(jsonNode, TldDescription.class));
            } catch (JsonProcessingException e) {
                //also this exeption can be launched if the api schema changes
                throw new ServerException("the api schema is changed in: https://api.domainsdb.info/v1/info/tld",
                        "please contact the server administrator");
            }
        }
        throw new UserException("The selected TLD does Not Exist",
                    "use /listAll for a list of all tlds");
    }

    @GetMapping("/stats")
    public ResponseEntity<TldStats> statistic(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {

        ArrayList<TldDescription> tldList = rank(count).getBody();
        assert tldList != null;
        TldDescription maxTld = Collections.max(tldList);
        TldDescription mintld = Collections.min(tldList);
        float avg=0;
        for (TldDescription td: tldList) {
            avg+=td.getDomainsCount();
        }
        avg=avg/tldList.size();
        return ResponseEntity.ok(new TldStats(mintld,maxTld,avg));
    }
}
