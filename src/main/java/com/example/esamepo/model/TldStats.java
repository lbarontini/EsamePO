package com.example.esamepo.model;

public class TldStats extends TldClass implements Comparable<TldStats> {

    private int matchesCount;
    private String matchingWord;

    public int compareTo(TldStats other) {
        return Integer.compare(this.matchesCount, other.getMatchesCount());
    }

    public TldStats(String name, int matchesCount, String matchingWord) {
        super(name);
        this.matchesCount = matchesCount;
        this.matchingWord = matchingWord;
    }

    public int getMatchesCount() {
        return matchesCount;
    }

    public void setMatchesCount(int matchesCount) {
        this.matchesCount = matchesCount;
    }

    public String getMatchingWord() {
        return matchingWord;
    }

    public void setMatchingWord(String matchingWord) {
        this.matchingWord = matchingWord;
    }
}
