package com.example.esamepo.model;

public class WordUsage extends TldName implements Comparable<WordUsage> {

    private int matchesCount;
    private String matchingWord;

    public int compareTo(WordUsage other) {
        return Integer.compare(this.matchesCount, other.getMatchesCount());
    }

    public WordUsage(String name, int matchesCount, String matchingWord) {
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
