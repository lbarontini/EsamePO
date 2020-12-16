package com.example.esamepo.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

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
}
