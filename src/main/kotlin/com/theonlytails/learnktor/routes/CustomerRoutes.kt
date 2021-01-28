package com.theonlytails.learnktor.routes

import com.theonlytails.learnktor.database.CustomersDao
import com.theonlytails.learnktor.models.Customer
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

fun Route.customerRouting(dao: CustomersDao) {
	route("/customer") {
		get {
			if (dao.getAllCustomers().isNotEmpty()) {
				call.respond(dao.getAllCustomers())
			} else {
				call.respondText("No customers found.", status = HttpStatusCode.NotFound)
			}
		}

		get("{id}") {
			val id = call.parameters["id"] ?: return@get call.respondText(
				"Missing or malformed ID: ${call.parameters["id"]}",
				status = HttpStatusCode.BadRequest
			)

			val customer = dao.getCustomer(id.toInt()) ?: return@get call.respondText(
				"Customer $id not found.",
				status = HttpStatusCode.NotFound
			)

			call.respond(customer)
		}

		post {
			val customer = call.receive<Customer>()

			dao.createCustomer(customer.firstName, customer.lastName, customer.email)

			call.respondText("Customer ${customer.id} stored correctly", status = HttpStatusCode.Accepted)
		}

		put {
			val customer = call.receive<Customer>()

			if (dao.getCustomer(customer.id) != null) {
				dao.updateCustomer(customer.id, customer.firstName, customer.lastName, customer.email)

				call.respondText("Customer ${customer.id} updated correctly", status = HttpStatusCode.Accepted)
			} else {
				call.respondText("Customer ${customer.id} not found.", status = HttpStatusCode.NotFound)
			}
		}

		delete {
			if (dao.getAllCustomers().isNotEmpty()) {
				dao.getAllCustomers().forEach { dao.deleteCustomer(it.id) }
				call.respondText("All customers removed successfully", status = HttpStatusCode.Accepted)
			} else {
				call.respondText("No customers found.", status = HttpStatusCode.NotFound)
			}
		}

		delete("{id}") {
			val id = call.parameters["id"]

			if (id != null) {
				if (dao.getCustomer(id.toInt()) != null) {
					dao.deleteCustomer(id.toInt())
					call.respondText("Customer $id removed successfully", status = HttpStatusCode.Accepted)
				} else {
					call.respondText("Customer $id not found.", status = HttpStatusCode.NotFound)
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

fun Application.registerCustomerRoutes(dao: CustomersDao) {
	routing {
		customerRouting(dao)
	}
}