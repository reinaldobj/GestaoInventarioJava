package com.Acme.GestaoDeInventario.bdd;

import com.Acme.GestaoDeInventario.GestaoDeInventarioApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = GestaoDeInventarioApplication.class)
@CucumberContextConfiguration
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}
