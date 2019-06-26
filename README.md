# Dolven Integration App

Simple Spring Boot + Camel app to synchronize files from an SFTP server to local disk (read only).

Contains a Camel route that connects to the the SFTP server and polls for new files.
The files are left on the server and in order to avoid downloading the same file over and over an idempotent
repository is added in the form of a file. Already downloaded files are added to this file so they can be skipped in the future.

The app is written in/with:
- [Kotlin](https://kotlinlang.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Apache Camel](https://camel.apache.org/)

Then it's designed to run in a Docker container:
- [Docker](https://www.docker.com)

The key parts of the Camel documentation is:
- The [FTP/SFTP component](https://camel.apache.org/ftp2.html)
- The [File component](https://camel.apache.org/file2.html)
- [Idempotent Consumer](https://camel.apache.org/idempotent-consumer.html) (FileIdempotentRepository used - see file compoennt)

## Development Environment / Running Locally

I recommend installing [IntelliJ IDEA](https://www.jetbrains.com/idea/) for development IDE.
The community edition will go a long way. Jetbrains are also the creators of Kotlin - so download a JDK,
set it up and install the plugins for Kotlin then you should be ready to go.

Set up the project "from source" and choose the pom.xml file, then open. A maven project should be imported.
When the IDE is set up you should be able to open the DolvenIntegrationApp.kt file and right-click the main function then "Run"


## Building and running on a server

The project is build using Maven. You don't have to set this up as it is "virtualized" through Docker.
When building the Docker image it will first build the .jar file and then package it in an image with Java installed.

All you should need is IntelliJ IDEA for development and Docker for building and running it.

### Building the Docker image

On a machine with Docker, in the source directory run:
```bash
docker build -t dolven-integration .
```

This will build an image as defined in Dockerfile (in this dir) and tag it with "dolven-integration"

The next step is to run it:
```bash
docker run -d --name sftp-sync dolven-integration
```

Remember to mount the needed directories between host and container:
```bash
docker run -d --name sftp-sync -v /path/to/config/folder/:/opt/dolven/config/ -v /path/to/output/folder/:/opt/dolven/output/ dolven-integration
```

To run it interactively (and delete the container after run):
```bash
docker run --rm -it -v /path/to/config/folder/:/opt/dolven/config/ -v /path/to/output/folder/:/opt/dolven/output/ dolven-integration
```

See the Docker reference for other run options.
