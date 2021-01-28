package com.theonlytails.learnktor.database

import com.theonlytails.learnktor.database.dao.Dao
import com.theonlytails.learnktor.models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
	val id = integer("id").autoIncrement()
	val firstName = varchar("firstName", 100)
	val lastName = varchar("lastName", 100)
	val email = varchar("email", 100)

	override val primaryKey = PrimaryKey(id, name = "id")
}

class UserDatabase(private val db: Database) : Dao {
	override fun init() = transaction(db) {
		SchemaUtils.create(Users)
	}

	override fun createUser(firstName: String, lastName: String, email: String) {
		transaction(db) {
			Users.insert {
				it[this.firstName] = firstName
				it[this.lastName] = lastName
				it[this.email] = email
			}
		}
	}

	override fun updateUser(id: Int, firstName: String, lastName: String, email: String) {
		transaction(db) {
			Users.update({ Users.id eq id }) {
				it[this.firstName] = firstName
				it[this.lastName] = lastName
				it[this.email] = email
			}
		}
	}

	override fun deleteUser(id: Int) {
		transaction(db) {
			Users.deleteWhere { Users.id eq id }
		}
	}

	override fun getUser(id: Int) = transaction(db) {
		Users.select { Users.id eq id }.map {
			User(it[Users.id], it[Users.firstName], it[Users.lastName], it[Users.email])
		}.singleOrNull()
	}

	override fun getAllUsers() = transaction(db) {
		Users.selectAll().map {
			User(it[Users.id], it[Users.firstName], it[Users.lastName], it[Users.email])
		}
	}

	override fun close() {}
}