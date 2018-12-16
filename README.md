# Elastic Cloud example with Spring Boot

[![Build Status](https://travis-ci.org/cgoncas/elastic-example-springboot.svg?branch=master)](https://travis-ci.org/cgoncas/elastic-example-springboot)

Example of how connect elastic cloud with SpringData

## Execute it using bootrun

### Set environment variables

- *ELASTICSEARCH_CLOUD_CREDENTIALS* = user:password
- *ELASTICSEARCH_CLOUD_HOST* = your cluster elastic.co host name
- *ELASTICSEARCH_CLOUD_PORT* = 9343

### Run bootRun

```./gradlew bootRun```

## Execute it using Docker

### Build image

```./gradlew -x test buildMyAppImage```

### Run image

```docker run \
    -p 8080:8080 \
    -e ELASTICSEARCH_CLOUD_CREDENTIALS='elastic:password' \
    -e ELASTICSEARCH_CLOUD_HOST='your host' \
    -e ELASTICSEARCH_CLOUD_PORT=9343 \
    elastic-example-springboot```



