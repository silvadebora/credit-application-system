package me.dio.creditapplicationsystem.controller

import jakarta.validation.Valid
import jakarta.websocket.server.PathParam
import me.dio.creditapplicationsystem.dto.CreditDto
import me.dio.creditapplicationsystem.dto.CreditView
import me.dio.creditapplicationsystem.dto.CreditViewList
import me.dio.creditapplicationsystem.model.Credit
import me.dio.creditapplicationsystem.service.impl.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        val credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode} - Customer " +
                    "${credit.customer?.firstName} saved")
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long):
            ResponseEntity<List<CreditViewList>>{
        val creditViewLists: List<CreditViewList> =
            this.creditService.findAllByCustomer(customerId).stream()
                .map { credit: Credit -> CreditViewList(credit) }
                .collect(
                    Collectors.toList()
                )
        return ResponseEntity.status(HttpStatus.OK).body(creditViewLists)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {

        val credit: Credit = this.creditService.findByCreditCode(creditCode, customerId)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }
}