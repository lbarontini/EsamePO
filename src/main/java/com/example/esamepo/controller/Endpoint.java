package com.example.esamepo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Endpoint {
    @GetMapping("/fetchall")
    public String FetchAll(@RequestParam(name = "test", defaultValue = "itworks") String test){
        return test;
    }

}
