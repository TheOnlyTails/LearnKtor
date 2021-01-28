package com.theonlytails.learnktor.database.dao

import com.theonlytails.learnktor.models.Customer
import java.io.Closeable

interface Dao : Closeable {
	fun init()
	fun createCustomer(firstName: String, lastName: String, email: String)
	fun updateCustomer(id: Int, firstName: String, lastName: String, email: String)
	fun deleteCustomer(id: Int)
	fun getCustomer(id: Int): Customer?
	fun getAllCustomers(): List<Customer>
}