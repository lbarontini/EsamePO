package com.example.esamepo.model;

import com.example.esamepo.utils.TldDescriptionSerializer;
import com.example.esamepo.utils.TldDescriptionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;

/**
 * model class that extend TldName with more details, it uses a custom serializer and deserializer for generating json output
 * @see TldName
 * @see TldDescriptionSerializer
 * @see TldDescriptionDeserializer
 */
@JsonDeserialize(using = TldDescriptionDeserializer.class)
@JsonSerialize(using = TldDescriptionSerializer.class)
public class TldDescription extends TldName implements Comparable<TldDescription> {


    /**
     * attribute that represent the number of domains included in the tld
     * must be set to -1 for signaling the serializer that should not be printed;
     */
    private int domainsCount;

    /**
     * attribute that represent the list of names of the domains included in the tld
     * must be set to null for signaling the serializer that should not be printed;
     */
    private ArrayList<String> includes;

    /**
     * attribute that represent a description of the tld
     */
    private ArrayList<String> description;


    /**
     * constructor that only accept the tld name and tld description
     *
     * @param name the tld name
     * @param description the array of string representing the tld description
     */
    public TldDescription(String name, ArrayList<String> description) {
        super(name);
        this.description = description;
    }

    /**
     * constructor that only accept tld name and the number of domains included in the tld
     *
     * @param name the tld name
     * @param domainsCount the number of included domains
     */
    public TldDescription(String name, int domainsCount) {
        super(name);
        this.domainsCount = domainsCount;
    }

    /**
     * constructor that accept the name, the names of the included domains and the description of the tld
     * it set domainsCount= -1 for signaling the serializer that domainsCount should not be printed;
     *
     * @param name the tld name
     * @param includes the array of string representing the names of the included domains
     * @param description the array of string representing the tld description
     */
    public TldDescription(String name,ArrayList<String> includes, ArrayList<String> description) {
        super(name);
        this.includes = includes;
        this.domainsCount = -1;
        this.description = description;
    }

    /**
     * constructor that accepts tha name, the count of included domains and the description of the tld
     * it set includes=null for signaling the serializer that includes should not be printed
     * @param name the tld name
     * @param domainsCount the number of included domains
     * @param description the array of string representing the tld description
     */
    public TldDescription(String name, int domainsCount, ArrayList<String> description) {
        super(name);
        this.includes = null;
        this.domainsCount = domainsCount;
        this.description = description;
    }

    /**
     * getter for the includes attribute
     * @return an array of string representing the names of the domains included in the tld
     */
    public ArrayList<String> getIncludes() {
        return includes;
    }

    /**
     * setter for the includes attribute
     * @param includes an array of strings representing the names of the domains included in the tld
     */
    public void setIncludes(ArrayList<String> includes) {
        this.includes = includes;
    }

    /**
     * getter for the attribute domainsCount
     * @return the number of domains included in the tld
     */
    public int getDomainsCount() {
        return domainsCount;
    }

    /**
     * setter for the attribute domainsCount
     * @param domainsCount the number of domains included in the tld
     */
    public void setDomainsCount(int domainsCount) {
        this.domainsCount = domainsCount;
    }

    /**
     * getter for the attribute description
     * @return an array of strings representing the description of the tld
     */
    public ArrayList<String> getDescription() {
        return description;
    }

    /**
     * setter for the attribute description
     * @param description an array of strings representing the description of the tld
     */
    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    /**
     * override of the compareTo method for comparing TldDescription objects by domainsCount
     * @param other the object to compare with
     * @return 0 if the domainsCount is equal between the two TldDescription objects,
     *      a value > 0 if the other domainsCount is bigger,
     *      a value < 0 if the other domainsCount is smaller
     */
    //todo check if other is an instance of TldDescription?
    @Override
    public int compareTo(TldDescription other) {
        return Integer.compare(this.domainsCount, other.getDomainsCount());
    }
}
