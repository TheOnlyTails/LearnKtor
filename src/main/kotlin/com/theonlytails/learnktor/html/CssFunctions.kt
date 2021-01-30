package com.theonlytails.learnktor.html

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.css.CSSBuilder
import kotlinx.css.fontFamily
import kotlinx.css.fontWeight

fun Application.registerCssRouting() {
	routing {
		route("/styles.css") {
			get {
				call.respondCss {
					kotlinx.css.div {
						fontFamily = "sans-serif"
					}
					kotlinx.css.h2 {
						fontWeight = kotlinx.css.FontWeight.normal
					}
				}
			}
		}
	}
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
	this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
