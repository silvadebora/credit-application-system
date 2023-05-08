package me.dio.creditapplicationsystem.model

import jakarta.persistence.*

@Entity
data class Customer(
    // iniciando vazias para não dar error execution ddl
    @Column(nullable = false) var firstName: String = " ",
    @Column(nullable = false) var lastName: String = " ",
    @Column(nullable = false, unique = true) val cpf: String,
    @Column(nullable = false, unique = true) var email: String = " ",
    @Column(nullable = false) var password: String = " ",
    @Column(nullable = false) @Embedded var address: Address = Address(),
    @Column(nullable = false) @OneToMany(fetch = FetchType.LAZY,
        cascade = arrayOf(CascadeType.REMOVE, CascadeType.PERSIST),
    mappedBy = "customer")
    var credits: List<Credit> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null
)
