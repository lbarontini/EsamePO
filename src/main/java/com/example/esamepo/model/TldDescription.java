package com.example.esamepo.model;

import java.util.ArrayList;

public class TldDescription extends TldClass implements Comparable<TldDescription> {

    private int domainsCount;
    private ArrayList<String> description;

    public int compareTo(TldDescription other) {
        return Integer.compare(this.domainsCount, other.getDomainsCount());
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
