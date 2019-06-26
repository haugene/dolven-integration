package no.dolven.integration.routes

import org.apache.camel.LoggingLevel
import org.apache.camel.component.file.GenericFileOperationFailedException
import org.apache.camel.spring.SpringRouteBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SftpSyncRoute(
) : SpringRouteBuilder() {

    private final val loggerName = SftpSyncRoute::class.java.toGenericString()


    @Value("\${sftp.hostname}")
    private val hostname: String? = null
    @Value("\${sftp.port}")
    private val port: Int? = null
    @Value("\${sftp.baseDir}")
    private val baseDir: String? = ""
    @Value("\${sftp.username}")
    private val username: String? = null
    @Value("\${sftp.password}")
    private val password: String? = null
    @Value("\${download}")
    private val download: String = "false"
    @Value("\${output.dir}")
    private val outputDir: String = ""

    override fun configure() {

        onException(Exception::class.java)
            .log(LoggingLevel.ERROR, loggerName, "Failed to process file \${headers.CamelFileName}.")
            .log(LoggingLevel.ERROR, loggerName, "\${exception}")
            .handled(true)

        from(
            "sftp://$hostname:$port$baseDir" +
                    "?username=$username" +
                    "&password=$password" +
                    "&recursive=true" +
                    "&delay=10000" + // 10 sec between polls
                    "&download=$download" +
                    "&noop=true" +
                    "&idempotent=true" +
                    "&idempotentRepository=#fileIdempotencyRepo"
        )
            .routeId("sync-files-from-remote-server")
            .choice().`when` { download == "true" }
                .log(LoggingLevel.INFO, loggerName, "Downloading file... \${headers.CamelFileName}")
                .to(
                    "file:$outputDir" +
                            "?fileExist=Ignore"
                )
                .log(LoggingLevel.INFO,loggerName,"Successfully downloaded \${headers.CamelFilePath}, weighing in at \${file:size} bytes"
                )
            .otherwise()
                .log(LoggingLevel.INFO, loggerName, "Skipping download of file \${headers.CamelFileName}, adding to \"processed\" list")
            .end()
    }
}