package com.theonlytails.learnktor.html

import com.theonlytails.learnktor.models.User
import kotlinx.html.*

fun DIV.createUser(user: User) {
	link(rel = "stylesheet", href = "/styles.css", type = "text/css")

	h2 { +"${user.firstName} ${user.lastName}" }

	a {
		title = "ID: ${user.id}"
		+"${user.firstName} ${user.lastName} ("
	}

	a("mailto:${user.email}") {
		title = "ID: ${user.id}"
		+user.email
	}

	a { +")" }
}

fun HTML.createUserList(users: List<User>) {
	body {
		link(rel = "stylesheet", href = "/styles.css", type = "text/css")

		div {
			h1 { +"Users" }

			for (user in users) {
				createUser(user)
			}
		}
	}
}