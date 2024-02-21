package com.example.farewelltaxi.model
import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Entity
@Table(name = "DRIVER")
class Driver (

    @OneToOne
    val user: User,
    @Column(name = "IS_DRIVING")
    var isDriving: Boolean=false,

    @ManyToOne
    @JoinColumn(name = "Taxi_ID")

    @JsonIgnoreProperties("DRIVER")
    var taxi: Taxi? = null,

    @Column(name = "EARN")
    var earn:Int = 0,

    @Column(name = "NICKNAME")
    var nickname:String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
{

    fun earning(amount:Int){
        this.earn += amount
    }
}





