package me.dio.creditapplicationsystem.service

import me.dio.creditapplicationsystem.model.Credit
import java.util.UUID

interface ICreditService {

    fun save(credit: Credit): Credit

    fun findAllByCustomer(customerId: Long): List<Credit>

    fun findByCreditCode(creditCode: UUID, creditId: Long): Credit
}