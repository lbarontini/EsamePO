package com.example.esamepo.model;

/**
 * class for storing the count of domains that contain the given word
 * and serves as json output for the route /stats
 * @see com.example.esamepo.controller.Endpoint
 */
public class WordUsage extends TldName implements Comparable<WordUsage> {

    /**
     * the word searched inside a tld
     */
    private String matchingWord;

    /**
     * the the count of domains that contain the given word
     */
    private int matchesCount;

    /**
     * constructor
     * @param name the name of the tld
     * @param matchesCount the count of matches
     * @param matchingWord the searched word
     */
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

    /**
     * implementation of the compareTo method of the Comparable interface for comparing WordUsage objects by matchesCount
     * @param other the object to compare with
     * @return 0 if the matchesCount is equal between the two WordUsage objects,
     *           a value > 0 if the other matchesCount is bigger,
     *           a value < 0 if the other matchesCount is smaller
     */
    @Override
    public int compareTo(WordUsage other) {
        return Integer.compare(this.matchesCount, other.getMatchesCount());
    }
}
