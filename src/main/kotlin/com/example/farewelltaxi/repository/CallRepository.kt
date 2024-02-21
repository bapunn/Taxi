package com.example.farewelltaxi.repository

import com.example.farewelltaxi.enum.TaxiLevel
import com.example.farewelltaxi.model.Call
import com.example.farewelltaxi.model.Driver
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param


interface CallRepository : CrudRepository<Call, Long> {

    @Query("SELECT SUM(c.price) FROM Call c WHERE c.driver = :driver AND c.endAt BETWEEN :startOfDay AND :endOfDay AND c.status = 'STOPRUNNING'")
    fun calculateDailyEarnings(
        @Param("driver") driver: Driver,
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime
    ): Double

    fun findByTaxiLevel(taxiLevel: TaxiLevel): List<Call>
    fun findByTaxiLevelIn(taxiLevels: List<TaxiLevel>): List<Call>
    fun findByPassengerId(passengerId: Long): List<Call>

}
