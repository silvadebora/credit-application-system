package me.dio.creditapplicationsystem.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.unmockkAll
import io.mockk.verify
import me.dio.creditapplicationsystem.exception.BusinessException
import me.dio.creditapplicationsystem.model.Credit
import me.dio.creditapplicationsystem.model.Customer
import me.dio.creditapplicationsystem.repository.CreditRepository
import me.dio.creditapplicationsystem.repository.CustomerRepository
import me.dio.creditapplicationsystem.service.impl.CreditService
import me.dio.creditapplicationsystem.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        creditService = CreditService(creditRepository, customerService)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should create credit `() {
        //given
        val credit: Credit = buildCredit()
        val customerId: Long = 1L

        every { customerService.findById(customerId) } returns credit.customer!!
        every { creditRepository.save(credit) } returns credit
        //when
        val actual: Credit = this.creditService.save(credit)
        //then
        verify(exactly = 1) { customerService.findById(customerId) }
        verify(exactly = 1) { creditRepository.save(credit) }

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit)
    }

    @Test
    fun `should not create credit when invalid day first installment`() {

        val invalidDayFirstInstallment: LocalDate = LocalDate.now().plusMonths(3)
        val credit: Credit = buildCredit(dayFirstInstallment = invalidDayFirstInstallment)

        every { creditRepository.save(credit) } answers { credit }

        Assertions.assertThatThrownBy { creditService.save(credit) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("Invalid Date")

        verify(exactly = 0) { creditRepository.save(any()) }
    }

    @Test
    fun `should return list of credits for a customer`() {
        val customerId: Long = 1L
        val expectedCredits: List<Credit> = listOf(buildCredit(), buildCredit(), buildCredit())

        every { creditRepository.findAllByCustomerId(customerId) } returns expectedCredits

        val actual: List<Credit> = creditService.findAllByCustomer(customerId)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(expectedCredits)

        verify(exactly = 1) { creditRepository.findAllByCustomerId(customerId) }
    }

    @Test
    fun `should return credit for a valid customer and credit code`() {
        //given
        val customerId: Long = Random().nextLong()
        val creditCode: UUID = UUID.randomUUID()
        val credit: Credit = buildCredit(customer = Customer(id = customerId))

        every { creditRepository.findByCreditCode(creditCode) } returns credit
        //when
        val actual: Credit = creditService.findByCreditCode(creditCode ,customerId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(credit)

        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    @Test
    fun `should throw BusinessException for invalid credit code`() {
        //given
        val customerId: Long = 1L
        val invalidCreditCode: UUID = UUID.randomUUID()

        every { creditRepository.findByCreditCode(invalidCreditCode) } returns null
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(invalidCreditCode ,customerId) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("Creditcode $invalidCreditCode not found")
        //then
        verify(exactly = 1) { creditRepository.findByCreditCode(invalidCreditCode) }
    }

    @Test
    fun `should throw IllegalArgumentException for different customer ID`() {

        val customerId: Long = 1L
        val creditCode: UUID = UUID.randomUUID()
        val credit: Credit = buildCredit(customer = Customer(id = 2L))

        every { creditRepository.findByCreditCode(creditCode) } returns credit

        Assertions.assertThatThrownBy { creditService.findByCreditCode(creditCode, customerId) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Contact admin")

        verify { creditRepository.findByCreditCode(creditCode) }
    }


    companion object {
        private fun buildCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(100.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(2L),
            numberOfInstallments: Int = 15,
            customer: Customer = CustomerServiceTest.buildCustomer()
        ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customer = customer
        )
    }
}