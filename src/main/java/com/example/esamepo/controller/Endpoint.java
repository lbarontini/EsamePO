package com.example.esamepo.controller;

import com.example.esamepo.exception.ServerException;
import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.StatsOutputModel;
import com.example.esamepo.model.TldDescription;
import com.example.esamepo.utils.JSONUtils;
import com.example.esamepo.model.TldStats;
import com.example.esamepo.model.TldClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
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

        ArrayList<TldClass> tlds = JSONUtils.JsonNodeToArrayList(tldsNode, TldClass.class);

        return ResponseEntity.ok(tlds);
    }

    @GetMapping("/rank")
    public ResponseEntity<ArrayList<TldDescription>> rank(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {

        ArrayList<TldClass> tlds = listAll().getBody();
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
    public ResponseEntity<ArrayList<TldStats>> stats(@RequestBody String filter) {

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<TldStats> allTldStats = new ArrayList<>();

        try {
            JsonNode inputNode = objectMapper.readTree(filter);

            String inputTLD = inputNode.get("tld").asText();
            ArrayList<String> inputWords = JSONUtils.JsonNodeToArrayList(inputNode.get("words"), String.class);

            ArrayList<TldClass> validTLDs = listAll().getBody();

            if (!validTLDs.contains(new TldClass(inputTLD))) {
                throw new UserException("The selected TLD does Not Exist",
                                        "Use /listAll for a list of all TLDs");
            }

            for (String singleWord : inputWords){

                try {
                    int matchesCount = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/domains/search?domain=" + singleWord + "&zone=" + inputTLD).get("total").asInt();

                    TldStats thisTldStats = new TldStats(inputTLD, matchesCount, singleWord);
                    allTldStats.add(thisTldStats);

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
        } else
            throw new UserException("The selected TLD does Not Exist",
                    "use /listAll for a list of all tlds");
    }

    @GetMapping("/statistic")
    public ResponseEntity<StatsOutputModel> statistic() {

        ArrayList<TldClass> tlds = listAll().getBody();
        ArrayList<TldDescription> tldList = rank("size","").getBody();
        assert tldList != null;
        TldDescription maxTld = Collections.max(tldList);
        TldDescription mintld = Collections.min(tldList);
        float avg=0;
        for (TldDescription td: tldList) {
            avg+=td.getDomainsCount();
        }
        avg=avg/tldList.size();
        return ResponseEntity.ok(new StatsOutputModel(mintld,maxTld,avg));
    }
}
