package com.example.esamepo.model;

import com.example.esamepo.utils.TldDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

@JsonDeserialize(using = TldDeserializer.class)
public class TldDescription extends TldName implements Comparable<TldDescription> {

    private int domainsCount;
    private ArrayList<String> includes;
    private ArrayList<String> description;

    @Override
    public int compareTo(TldDescription other) {
        return Integer.compare(this.domainsCount, other.getDomainsCount());
    }

    public TldDescription(String name,ArrayList<String> includes, ArrayList<String> description) {
        super(name);
        this.includes=includes;
        this.description = description;
    }
    public TldDescription(String name, int domainsCount, ArrayList<String> description) {
        super(name);
        this.domainsCount = domainsCount;
        this.description = description;
    }

    public TldDescription(String name, ArrayList<String> description) {
        super(name);
        this.description = description;
    }

    public TldDescription(String name, int domainsCount) {
        super(name);
        this.domainsCount = domainsCount;
    }

    public ArrayList<String> getIncludes() {
        return includes;
    }

    public void setIncludes(ArrayList<String> includes) {
        this.includes = includes;
    }

    public int getDomainsCount() {
        return domainsCount;
    }

    public void setDomainsCount(int domainsCount) {
        this.domainsCount = domainsCount;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

}
