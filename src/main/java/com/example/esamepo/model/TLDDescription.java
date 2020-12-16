package com.example.esamepo.model;

public class TLDDescription extends TldClass implements Comparable<TLDDescription> {

    private int domainsCount;
    private String description;

    public int compareTo(TLDDescription other) {
        return Integer.compare(this.domainsCount, other.getDomainsCount());
    }

    public TLDDescription(String name, int domainsCount, String description) {
        super(name);
        this.domainsCount = domainsCount;
        this.description = description;
    }

    public TLDDescription(String name, int domainsCount) {
        super(name);
        this.domainsCount = domainsCount;
    }

    public int getDomainsCount() {
        return domainsCount;
    }

    public void setDomainsCount(int domainsCount) {
        this.domainsCount = domainsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
