package com.example.esamepo.model;

public class TldClass {

    private String name;

    public TldClass(String name) {
        this.name = name;
    }
    public TldClass() {
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        } else if ((other == null) || !(other instanceof TldClass)) {
            return false;
        }

        return (this.name.equals(((TldClass) other).getName()));
    }
}
