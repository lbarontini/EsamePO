package com.example.esamepo.model;

import java.util.ArrayList;

public class TLDStats extends TldClass {

    private int matchesCount;
    private ArrayList<String> matchingDomains;
    private String matchingWord;
    public TLDStats(String name, int matchesCount, ArrayList<String> matchingDomains, String matchingWord) {
        super(name);
        this.matchesCount = matchesCount;
        this.matchingDomains = matchingDomains;
        this.matchingWord = matchingWord;
    }

    public int getMatchesCount() {
        return matchesCount;
    }

    public void setMatchesCount(int matchesCount) {
        this.matchesCount = matchesCount;
    }

    public ArrayList<String> getMatchingDomains() {
        return matchingDomains;
    }

    public void setMatchingDomains(ArrayList<String> matchingDomains) {
        this.matchingDomains = matchingDomains;
    }

    public String getMatchingWord() {
        return matchingWord;
    }

    public void setMatchingWord(String matchingWord) {
        this.matchingWord = matchingWord;
    }
}
