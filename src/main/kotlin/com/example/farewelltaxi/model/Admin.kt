package com.example.farewelltaxi.model
import jakarta.persistence.*



@Entity
@Table(name = "ADMIN")
class Admin (

    @OneToOne
    val user: User,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)





