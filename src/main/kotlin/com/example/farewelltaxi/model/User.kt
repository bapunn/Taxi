package com.example.farewelltaxi.model
import com.example.farewelltaxi.enum.Role
import jakarta.persistence.*


@Entity
@Table(name = "`USERS`")
class User(


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,
    val password: String,
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    var role: Role
    // 다른 공통 속성들...
)