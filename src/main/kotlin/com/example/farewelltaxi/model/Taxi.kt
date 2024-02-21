package com.example.farewelltaxi.model
import com.example.farewelltaxi.enum.TaxiLevel
import com.example.farewelltaxi.enum.Status
import jakarta.persistence.*


@Entity
@Table(name = "TAXI")
class Taxi (
    @Enumerated(EnumType.STRING)
    @Column(name = "TAXILEVEL", nullable = false)
    var taxiLevel: TaxiLevel,

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    var status : Status = Status.WAIT,

    @OneToMany(mappedBy = "taxi", cascade = [CascadeType.ALL])
    var driverSet: MutableSet<Driver> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    ) {

    fun addDriver(driver: Driver) {
        driver.taxi = this
        this.driverSet.add(driver)
    }
}
