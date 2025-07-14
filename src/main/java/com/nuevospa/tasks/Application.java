package com.nuevospa.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Punto de entrada del Gestor de Tareas.
 *
 * Al ejecutarse, Spring Boot:
 *   1) Arranca el contexto, configura JPA/H2 y Spring Security.
 *   2) Escanea todos los componentes (@Component, @Service, @Repository, @Controller).
 *   3) Expone la API en http://localhost:8080  (Swagger-UI en /swagger-ui.html).
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        //TODO: check
        System.out.println("PASSSS");
        System.out.println(new BCryptPasswordEncoder().encode("admin123"));

    }
}
