package me.dio.creditapplicationsystem.dto

import me.dio.creditapplicationsystem.model.Address
import me.dio.creditapplicationsystem.model.Customer
import java.math.BigDecimal

data class CustomerDto(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    var income: BigDecimal,
    var email: String,
    var password: String,
    var zipCode: String,
    var street: String
) {

    fun toEntity(): Customer= Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
    )

}
