package com.example.esamepo.controller;

import com.example.esamepo.exception.ServerException;
import com.example.esamepo.exception.UserException;
import com.example.esamepo.model.TldDescription;
import com.example.esamepo.model.TldName;
import com.example.esamepo.model.TldStats;
import com.example.esamepo.model.WordSelection;
import com.example.esamepo.model.WordUsage;
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

/**
 * Controller class for handling all the rest API route
 */
@RestController
public class Endpoint {

    /**
     * route that show all the Tld name as a response of a GET request.
     * because of the temporary downtime of the https://api.domainsdb.info/v1/info/tld/ API
     * tlds.json has been created as a workaround, it contains 100 randomly selected top-level domain,
     * that have been checked to be available in Domains-Index database.
     *
     * @return a json array of TldName containing all the Tlds Names
     * @throws ServerException exception launched is something went wrong with https://api.domainsdb.info/v1/info/tld/
     */
    @GetMapping("/listAll")
    public ResponseEntity<ArrayList<TldName>> listAll() {
        /*
        workaround as explained above
        JsonNode tldsNode = JSONUtils.UrlToJsonNode("file:./tlds.json").get("includes");
        */
        JsonNode tldsNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/tld/").get("includes");

        //tldsNode is null if "includes" field is missing, whereas tldsNode.isNull()
        //is true if "includes" is set to null
        if (tldsNode == null || tldsNode.isNull()) {
            throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/tld",
                                      "Please contact the server administrator");
        }

        ArrayList<TldName> tlds = JSONUtils.JsonNodeToArrayList(tldsNode, TldName.class);

        return ResponseEntity.ok(tlds);
    }

    /**
     * route that show a list of tlds, ordered by the number of domains contained, as a response of a get request
     *
     * @param count number of tlds to fetch, 10 by default, because the api is slow is better to maintain this number small
     //Todo is sub-domains the correct name?
     * @return return a json array of TldDescription, containing the name of the tld and the count of the sub-domains, ordered by this count
     * @throws ServerException exception launched if something went wrong with https://api.domainsdb.info/v1/info/stat/
     * @throws UserException exception launched if the user input is not a positive integer
     */
    @GetMapping("/rank")
    public ResponseEntity<ArrayList<TldDescription>> rank(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {

        if (count <= 0){
            throw new UserException(Integer.toString(count) + " is not a valid number",
                                    "Provide a positive integer as a count");
        }

        ArrayList<TldName> tlds = listAll().getBody();
        ArrayList<TldDescription> rankedTLDs = new ArrayList<>();

        // This should iterate over all TLDs, but as mentioned above,
        // API is slow so will only fetch first {count} for the demo
        for (int tldIndex = 0; tldIndex < count; tldIndex++) {

            String thisTLDName = tlds.get(tldIndex).getName();
            JsonNode arrayNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/stat/" + thisTLDName).get("statistics");

            if (arrayNode == null || arrayNode.isNull()) {
                throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                          "Please contact the server administrator");
            }

            // Empty array if TLD never surveyed
            JsonNode firstNode = arrayNode.get(0);

            if (firstNode == null || firstNode.isNull()) {
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


    /**
     * route that show the number of times that the words, passed as post request body,
     * are in the sub domains name of a specified tld
     *
     * @param data json formatted string that contains the Tld name and the words to search.
     *        json body schema:
     *             {
     *                  "tld":"string",
     *                  "words":["string"]
     *             }
     * @return return a json array of WordUsage indicating the domain, the word and the number of matches, ordered by this number
     * @throws ServerException exception launched if something went wrong with https://api.domainsdb.info/v1/info/stat/
     * @throws UserException exception launched if the user put an invalid tld name as input
     *
     */
    @PostMapping(path = "/stats", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ArrayList<WordUsage>> wordStats(@RequestBody String data) {

        ArrayList<WordUsage> allWordUsage = new ArrayList<>();

        WordSelection inputSelection = JSONUtils.RawInputToWordSelection(data);
        String inputTLD = inputSelection.getTld();

        ArrayList<String> inputWords = inputSelection.getWords();

        ArrayList<TldName> validTLDs = listAll().getBody();

        if (!validTLDs.contains(new TldName(inputTLD))) {
            throw new UserException("The selected TLD does Not Exist",
                                    "Use /listAll for a list of all TLDs");
        }

        for (String singleWord : inputWords){

            try {
                int matchesCount = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/domains/search?domain=" + singleWord + "&zone=" + inputTLD).get("total").asInt();

                WordUsage thisWordUsage = new WordUsage(inputTLD, matchesCount, singleWord);
                allWordUsage.add(thisWordUsage);

                } catch (NullPointerException e) {
                    throw new ServerException("The API schema has changed: https://api.domainsdb.info/v1/info/stat/",
                                              "Please contact the server administrator");
            }
        }

        Collections.sort(allWordUsage);
        Collections.reverse(allWordUsage);

        return ResponseEntity.ok(allWordUsage);
    }


    /**
     * route that show relevant information about a tld from a get request
     *
     * @param tld the name of the tld
     * @return return a json serialized TldDescription with the tld name, the count of the sub domain and the description.
     * @throws ServerException exception launched if something went wrong with https://api.domainsdb.info/v1/info/tld/
     * @throws UserException exception launched if the user put an invalid tld name as input
     */
    @GetMapping("/info")
    public ResponseEntity<TldDescription> info(@RequestParam(name = "tld", defaultValue = "null") String tld) {

        ArrayList<TldName> tlds = listAll().getBody();
        assert tlds != null;
        if (tlds.contains(new TldName(tld))) {
            JsonNode jsonNode = JSONUtils.UrlToJsonNode("https://api.domainsdb.info/v1/info/tld/" + tld);
            //jsonNode can be null if the schema of the api changes
            if (jsonNode == null || jsonNode.isNull())
                throw new ServerException("the api schema is changed in: https://api.domainsdb.info/v1/info/tld/",
                        "please contact the server administrator");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return ResponseEntity.ok(objectMapper.treeToValue(jsonNode, TldDescription.class));
            } catch (JsonProcessingException e) {
                //also this exeption can be launched if the api schema changes
                throw new ServerException("the api schema is changed in: https://api.domainsdb.info/v1/info/tld/",
                        "please contact the server administrator");
            }
        }
        throw new UserException("The selected TLD does Not Exist",
                    "use /listAll for a list of all tlds");
    }


    /**
     * route that show statistics about sub domains contained in the tlds.
     * @param count number of tlds to fetch, 10 by default, because the api is slow is better to maintain this number small
     * @return json serialization of TldStats containing the tld with the minimum number of sub-domain,
     *      the tld with the maximum number of sub domains and the average of domains contained in the fetched tlds
     * @throws UserException exception launched if the user input is not a positive integer
     */
    @GetMapping("/stats")
    public ResponseEntity<TldStats> statistic(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {

        if (count <= 0){
            throw new UserException(Integer.toString(count) + " is not a valid number",
                                    "Provide a positive integer as a count");
        }

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
