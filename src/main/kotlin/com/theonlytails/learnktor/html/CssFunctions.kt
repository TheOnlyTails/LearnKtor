package com.theonlytails.learnktor.html

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.css.*

fun Application.registerCssRouting() {
	routing {
		route("/styles.css") {
			get {
				call.respondCss {
					body {
						backgroundColor = Color("#1f1f23")
						color = Color("#e8e8e9")
					}

					div {
						fontFamily = "sans-serif"
					}

					h2 {
						fontWeight = FontWeight.normal
					}
				}
			}
		}
	}
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
	this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
