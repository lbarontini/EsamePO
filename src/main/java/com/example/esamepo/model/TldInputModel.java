package com.example.esamepo.model;

import com.example.esamepo.utils.TldInputDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
// test class for the implementation of the deserializer
@JsonDeserialize(using = TldInputDeserializer.class)
public class TldInputModel {
    private String name;
    private ArrayList<String> includes;
    private ArrayList<String> description;

    public TldInputModel(String name, ArrayList<String> includes, ArrayList<String> description) {
        this.name = name;
        this.includes = includes;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIncludes() {
        return includes;
    }

    public void setIncludes(ArrayList<String> includes) {
        this.includes = includes;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }
}
