package me.dio.creditapplicationsystem.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.creditapplicationsystem.model.Address
import me.dio.creditapplicationsystem.model.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Ivalid input") val firstName: String,
    @field:NotEmpty(message = "Ivalid input") val lastName: String,
    @field:NotEmpty(message = "Ivalid input")
    @CPF(message = "This invalid CPF") val cpf: String,
    @field:NotNull(message = "Invalid input") var income: BigDecimal,
    @field:NotEmpty(message = "Ivalid input")
    @field:Email(message = "Invalid email") var email: String,
    @NotEmpty(message = "Ivalid input") var password: String,
    @NotEmpty(message = "Ivalid input") var zipCode: String,
    @NotEmpty(message = "Ivalid input") var street: String
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
