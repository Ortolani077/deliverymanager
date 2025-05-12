package com.example.gerenciamento.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/open")
    public String openEndpoint() {
        return "Este é um endpoint aberto!";
    }
}
