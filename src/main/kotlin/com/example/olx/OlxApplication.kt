package com.example.olx

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OlxApplication

fun main(args: Array<String>) {
    runApplication<OlxApplication>(*args)
}