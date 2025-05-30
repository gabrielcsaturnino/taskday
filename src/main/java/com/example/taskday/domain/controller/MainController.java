package com.example.taskday.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/main")
public class MainController {
     @GetMapping("/")
     public String getMethodName(@RequestParam String param) {
         return new String();
     }
     
}
