package com.singularityuniverse.singularity.web

import App
import LocalProjectContext
import ProjectContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val context = ProjectContext(
        webHostUrl = "http://localhost:8081/"
    )

    ComposeViewport(document.body!!) {
        CompositionLocalProvider(LocalProjectContext provides context) {
            App()
        }
    }
}