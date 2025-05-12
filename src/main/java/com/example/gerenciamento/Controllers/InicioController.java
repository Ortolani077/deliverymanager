package com.example.gerenciamento.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/inicio.html"; // Redireciona para o arquivo estático
    }
}
