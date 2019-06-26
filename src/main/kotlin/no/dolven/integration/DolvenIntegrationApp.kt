package no.dolven.integration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DolvenIntegrationApp

fun main(args: Array<String>) {
    runApplication<DolvenIntegrationApp>(*args)
}
