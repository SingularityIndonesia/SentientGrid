package utils

import io.ktor.client.*

expect fun defaultHttpClient(webHostUrl: String): HttpClient