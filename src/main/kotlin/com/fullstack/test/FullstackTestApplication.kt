package com.fullstack.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class FullstackTestApplication

fun main(args: Array<String>) {
    runApplication<FullstackTestApplication>(*args)
}
