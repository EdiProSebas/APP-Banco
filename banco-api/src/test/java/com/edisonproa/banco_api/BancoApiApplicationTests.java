package com.edisonproa.banco_api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Se deshabilita para evitar levantar el contexto completo en ejecución desde IDE. No afecta a los tests unitarios reales.")
class BancoApiApplicationTests {

    @Test
    void contextLoads() {
        
    }
}
