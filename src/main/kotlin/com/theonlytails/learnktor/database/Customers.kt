package com.theonlytails.learnktor.database

import com.theonlytails.learnktor.database.dao.Dao
import com.theonlytails.learnktor.models.Customer
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Customers : Table() {
	val id = integer("id").autoIncrement().primaryKey()
	val firstName = varchar("firstName", 100)
	val lastName = varchar("lastName", 100)
	val email = varchar("email", 100)

	override val primaryKey = PrimaryKey(id, name = "id")
}

class CustomersDao(val db: Database) : Dao {
	override fun init() = transaction(db) {
		SchemaUtils.create(Customers)
		SchemaUtils.createMissingTablesAndColumns(Customers)
	}

	override fun createCustomer(firstName: String, lastName: String, email: String) {
		transaction(db) {
			Customers.insert {
				it[this.firstName] = firstName
				it[this.lastName] = lastName
				it[this.email] = email
			}
		}
	}

	override fun updateCustomer(id: Int, firstName: String, lastName: String, email: String) {
		transaction(db) {
			Customers.update({ Customers.id eq id }) {
				it[this.firstName] = firstName
				it[this.lastName] = lastName
				it[this.email] = email
			}
		}
	}

	override fun deleteCustomer(id: Int) {
		transaction(db) {
			Customers.deleteWhere { Customers.id eq id }
		}
	}

	override fun getCustomer(id: Int) = transaction(db) {
		Customers.select { Customers.id eq id }.map {
			Customer(it[Customers.id], it[Customers.firstName], it[Customers.lastName], it[Customers.email])
		}.singleOrNull()
	}

	override fun getAllCustomers() = transaction(db) {
		Customers.selectAll().map {
			Customer(it[Customers.id], it[Customers.firstName], it[Customers.lastName], it[Customers.email])
		}
	}

	override fun close() {}
}