package me.dio.creditapplicationsystem.repository

import me.dio.creditapplicationsystem.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer, Long>