# BM Bot

[![Build Status](https://travis-ci.org/xabgesagtx/bm-bot.svg?branch=master)](https://travis-ci.org/xabgesagtx/bm-bot) [![Telegram](http://trellobot.doomdns.org/telegrambadge.svg)](https://telegram.me/bm20bot)

This is a unofficial telegram bot for the events of bewegungsmelder.org. It uses the JSON API created for the [android application](https://github.com/arnef/bewegungsmelder-androidi).

The bot is built with the awesome Spring Boot 2 and runs on Java 8.

Also, this an application that was mainly built in 2016, afterwards only bugfixes and library updates were made.


## Configuration

All configuration can be done in the application.yml file.

```
config:
  botConfig:
    name: BOTNAME
    token: BOTTOKEN
server:
  port: PORT_OF_DEBUG_INTERFACE
logging:
  level:
    org:
      xabgesagtx: INFO
spring:
  datasource:
    username: MYSQL_USERNAME
    password: MYSQL_PASSWORD
    url: "jdbc:mysql://MYSQL_HOSTNAME/MYSQL_PASSWORD?useSSL=false"

```

## Build

Run maven to build the jar file

```
./mvnw clean package
```

Afterwards you have an executable jar file in `target/bm-bot.jar` that can also [function as an init-script](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html).

## Requirements

* Java 8
* MySQL

## Development

For development purposes a docker-compose.yml is provided. Simply run:

```
docker-compose up -d
```

A mysql database will be a available at port 3306 and an admin interface at port 8180. 

Afterwards you can start your bot via IDE if you set the configuration for the telegram bot properly (name and taken).
