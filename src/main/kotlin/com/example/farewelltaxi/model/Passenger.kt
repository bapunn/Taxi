package com.example.farewelltaxi.model
import jakarta.persistence.*

@Entity
@Table(name = "PASSENGER")
class Passenger(

    @OneToOne
    val user: User,

    @Column(name = "NICKNAME", nullable = false)
    var nickname :String,

    @Column(name = "POINT")
    var point : Int =0,


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)
{
    fun charge(amount:Int){
        this.point += amount
    }


    fun deductCallCost(price: Int) {
        if (this.point < price) {
            throw IllegalArgumentException("Not enough points")
        }

        this.point -= price
    }


}