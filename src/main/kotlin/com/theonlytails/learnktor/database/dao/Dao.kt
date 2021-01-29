package com.theonlytails.learnktor.database.dao

import com.theonlytails.learnktor.models.User
import java.io.Closeable

interface Dao : Closeable {
	fun init()
	fun createUser(firstName: String, lastName: String, email: String): Int
	fun updateUser(id: Int, firstName: String, lastName: String, email: String)
	fun deleteUser(id: Int)
	fun getUser(id: Int): User?
	fun getAllUsers(): List<User>
}