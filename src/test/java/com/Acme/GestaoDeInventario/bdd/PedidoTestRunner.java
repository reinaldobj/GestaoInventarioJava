package com.Acme.GestaoDeInventario.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/feature",
        glue = "com.Acme.GestaoDeInventario.bdd",
        tags = "not @ignore"
)
public class PedidoTestRunner {
}
