package com.example.backend.conrollers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
@Tag(name = "DemoController", description = "Это то, на чем нужно тестить")
public class DemoController {

    @GetMapping()
    public ResponseEntity<?> sayHello(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
