package utils

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private var httpClient: HttpClient? = null

actual fun defaultHttpClient(webHostUrl: String): HttpClient {
    requireNotNull(httpClient) {
        httpClient = HttpClient(Js) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorHttp: $message")
                    }
                }
                level = LogLevel.ALL
            }

            defaultRequest {
                // Replace with your actual API base URL
                url(webHostUrl)
                headers.append("Accept", ContentType.Application.Json.toString())
                headers.append("Content-Type", ContentType.Application.Json.toString())
            }

            engine {

            }
        }

        return httpClient!!
    }

    return httpClient!!
}