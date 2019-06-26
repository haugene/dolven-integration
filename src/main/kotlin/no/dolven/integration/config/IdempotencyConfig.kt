package no.dolven.integration.config

import org.apache.camel.processor.idempotent.FileIdempotentRepository
import org.apache.camel.spi.IdempotentRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File

@Configuration
class IdempotencyConfig {

    @Value("\${idempotency.file}")
    private val repoFile: String = ""

    @Bean
    fun fileIdempotencyRepo(): IdempotentRepository<String> {
        return FileIdempotentRepository.fileIdempotentRepository(File(repoFile))
    }
}