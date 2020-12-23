package com.example.esamepo.model;

import com.example.esamepo.utils.WordSelectionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

@JsonDeserialize(using = WordSelectionDeserializer.class)
public class WordSelection {

    private String tld;
    private ArrayList<String> words;

    public WordSelection(String tld, ArrayList<String> words) {
        this.tld = tld;
        this.words = words;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

}
