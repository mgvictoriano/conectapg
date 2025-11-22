package com.conectapg;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

/**
 * Classe base para testes de integração Spring Boot.
 * 
 * Fornece contexto Spring completo para testes que necessitam
 * de beans reais, configurações e integração com banco de dados.
 * 
 * Uso:
 * <pre>
 * class MeuServiceTest extends ServerTest {
 *     // Testes com contexto Spring completo
 * }
 * </pre>
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
public abstract class ServerTest {
    // Classe base para testes de integração
    // Subclasses herdam o contexto Spring completo
}
