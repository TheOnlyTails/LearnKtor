package com.theonlytails.learnktor.routes

import com.theonlytails.learnktor.database.UserDatabase
import com.theonlytails.learnktor.models.User
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

fun Route.userRouting(database: UserDatabase) {
	route("/User") {
		get {
			if (database.getAllUsers().isNotEmpty()) {
				call.respond(database.getAllUsers())
			} else {
				call.respondText("No users found.", status = HttpStatusCode.NotFound)
			}
		}

		get("{id}") {
			val id = call.parameters["id"] ?: return@get call.respondText(
				"Missing or malformed ID: ${call.parameters["id"]}",
				status = HttpStatusCode.BadRequest
			)

			val user = database.getUser(id.toInt()) ?: return@get call.respondText(
				"User $id not found.",
				status = HttpStatusCode.NotFound
			)

			call.respond(user)
		}

		post {
			val user = call.receive<User>()

			database.createUser(user.firstName, user.lastName, user.email)

			call.respondText("User ${user.id} stored correctly", status = HttpStatusCode.Accepted)
		}

		put {
			val user = call.receive<User>()

			if (database.getUser(user.id) != null) {
				database.updateUser(user.id, user.firstName, user.lastName, user.email)

				call.respondText("User ${user.id} updated correctly", status = HttpStatusCode.Accepted)
			} else {
				call.respondText("User ${user.id} not found.", status = HttpStatusCode.NotFound)
			}
		}

		delete {
			if (database.getAllUsers().isNotEmpty()) {
				database.getAllUsers().forEach { database.deleteUser(it.id) }
				call.respondText("All users removed successfully", status = HttpStatusCode.Accepted)
			} else {
				call.respondText("No users found.", status = HttpStatusCode.NotFound)
			}
		}

		delete("{id}") {
			val id = call.parameters["id"]

			if (id != null) {
				if (database.getUser(id.toInt()) != null) {
					database.deleteUser(id.toInt())
					call.respondText("User $id removed successfully", status = HttpStatusCode.Accepted)
				} else {
					call.respondText("User $id not found.", status = HttpStatusCode.NotFound)
				}
			} else {
				call.respondText(
					"Missing or malformed ID $id",
					status = HttpStatusCode.BadRequest
				)
			}
		}
	}
}

fun Application.registerUserRoutes(database: UserDatabase) {
	routing {
		userRouting(database)
	}
}