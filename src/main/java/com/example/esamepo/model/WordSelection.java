package com.example.esamepo.model;

import com.example.esamepo.utils.WordSelectionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * class for handling user selected words to search within a given tld
 * @see com.example.esamepo.controller.Endpoint
 */
@JsonDeserialize(using = WordSelectionDeserializer.class)
public class WordSelection {

    /**
     * string Representing the tld name
     */
    private String tld;


    /**
     * array of string representing the words to search
     */
    private ArrayList<String> words;


    /**
     * constructor
     * @param tld the name of the tld
     * @param words the words to search
     */
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
