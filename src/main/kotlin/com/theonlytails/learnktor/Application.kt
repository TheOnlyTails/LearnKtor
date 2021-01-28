package com.theonlytails.learnktor

import com.theonlytails.learnktor.database.UserDatabase
import com.theonlytails.learnktor.database.Users
import com.theonlytails.learnktor.routes.registerUserRoutes
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

val database = UserDatabase(Database.connect("jdbc:h2:./database/test", "org.h2.Driver"))

fun main() {
	embeddedServer(Netty, port = 8080) {
		install(CallLogging)
		install(ContentNegotiation) {
			gson {
				setPrettyPrinting()
			}
		}

		database.init()

		transaction {
			SchemaUtils.create(Users)
		}

		install(Routing) {
			registerUserRoutes(database)
		}
	}.start(wait = true)
}
