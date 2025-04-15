package com.example.gerenciamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"com.example.gerenciamento"})
@EnableJpaRepositories(basePackages = {"com.example.gerenciamento.repositories"}) // Correção aqui
@EntityScan(basePackages = {"com.example.gerenciamento.Entities"})
@SpringBootApplication
public class GerenciamentoDeliveryApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GerenciamentoDeliveryApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GerenciamentoDeliveryApplication.class);
    }
}
