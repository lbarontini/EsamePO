package com.example.esamepo.model;

/**
 * Model class for representing a tld only by its name
 */
public class TldName {

    /**
     * attribute that represent the name of the tld
     */
    private String name;

    /**
     * constructor of the class
     * @param name the name of the tld
     */
    public TldName(String name) {
        this.name = name;
    }

    /**
     * getter of the name attribute
     * @return a string representing the name of the tld
     */
    public String getName() {
        return name;
    }


    /**
     * setter of the name attribute
     * @param name the tld name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * override of the equals method for allowing TldName objects to be compared
     * @param other object to compare
     * @return true if the other object is an instance of this class with the same name
     */
    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        } else if ((other == null) || !(other instanceof TldName)) {
            return false;
        }
        return (this.name.equals(((TldName) other).getName()));
    }
}
