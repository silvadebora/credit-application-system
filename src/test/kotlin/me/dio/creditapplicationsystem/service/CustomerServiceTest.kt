package me.dio.creditapplicationsystem.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import me.dio.creditapplicationsystem.exception.BusinessException
import me.dio.creditapplicationsystem.model.Address
import me.dio.creditapplicationsystem.model.Customer
import me.dio.creditapplicationsystem.repository.CustomerRepository
import me.dio.creditapplicationsystem.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    // não acessa o repository
    @MockK
    lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    lateinit var customerService: CustomerService

    @Test
    fun `should create customer`() {
        // given
        val fakeCustomer: Customer = buildCustomer()
        // método do mockk - qualquer customer passado no método save, retorna um fakeCustomer
        every { customerRepository.save(any()) } returns fakeCustomer

        // when - aqui verifica se está salvando no db
        val actual: Customer = customerService.save(fakeCustomer)

        // then - verifica se é igual a null
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify ( exactly = 1 ) {customerRepository.save(fakeCustomer)}

    }

    @Test
    fun `should find customer by id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every{customerRepository.findById(fakeId)} returns Optional.of(fakeCustomer)

        //when
        val actual = customerService.findById(fakeId)

        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `should not find customer by invalid id and throw BusinessException`(){
        //given
        val fakeId: Long = Random().nextLong()
        every{customerRepository.findById(fakeId)} returns Optional.empty()

        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findById(fakeId) }
            .withMessage("Id $fakeId not found")
        verify(exactly = 1) { customerRepository.findById(fakeId) }

    }

    @Test
    fun `should delete customer by id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCustomer: Customer = buildCustomer(id = fakeId)
        every{customerRepository.findById(fakeId)} returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs

        //when
        customerService.delete(fakeId)

        //then
        verify(exactly = 1) { customerRepository.findById(fakeId) }
        verify (exactly = 1){ customerRepository.delete(fakeCustomer) }

    }

    // método de build
    companion object {
        fun buildCustomer(
            firstName: String = "Debora",
            lastName: String = "Silva",
            cpf: String = "28475934625",
            email: String = "debora@gmail.com",
            password: String = "12345",
            zipCode: String = "12345",
            street: String = "Rua da Silva",
            income: BigDecimal = BigDecimal.valueOf(1000.0),
            id: Long = 1L
        ) = Customer(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            password = password,
            address = Address(
                zipCode = zipCode,
                street = street,
            ),
            income = income,
            id = id
        )
    }


}