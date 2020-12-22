package com.example.esamepo.model;

public class TldName {

    private String name;

    public TldName(String name) {
        this.name = name;
    }
    public TldName() {
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
        } else if ((other == null) || !(other instanceof TldName)) {
            return false;
        }
        return (this.name.equals(((TldName) other).getName()));
    }
}
