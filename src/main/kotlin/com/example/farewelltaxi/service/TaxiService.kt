package com.example.farewelltaxi.service

import com.example.farewelltaxi.dto.Driver.DrivercheckResponseDto
import com.example.farewelltaxi.dto.Taxi.TaxiCheckResponseDto
import com.example.farewelltaxi.dto.Taxi.TaxiResponseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.farewelltaxi.model.Taxi
import com.example.farewelltaxi.repository.TaxiRepository
import com.example.farewelltaxi.repository.DriverRepository
import com.example.farewelltaxi.enum.TaxiLevel
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

@Service
class TaxiService(
    private val taxiRepository: TaxiRepository,
    private val driverRepository: DriverRepository
) {

    @Transactional
    fun createTaxi(taxiLevel: TaxiLevel): TaxiResponseDto {
        val taxi = Taxi(taxiLevel = taxiLevel)
        taxiRepository.save(taxi)
        return TaxiResponseDto(taxiLevel = taxi.taxiLevel)
    }

    @Transactional
    fun addDriverToTaxi(driverId: Long, taxiId: Long) {
        val taxi = taxiRepository.findById(taxiId).orElseThrow { NotFoundException() }
        val driver = driverRepository.findById(driverId).orElseThrow { NotFoundException() }
        taxi.addDriver(driver)
    }

    @Transactional(readOnly = true)
    fun findTaxiById(taxiId: Long): TaxiCheckResponseDto {
        val taxi =taxiRepository.findById(taxiId).orElseThrow {Exception("택시를 찾을 수 없습니다.") }
        return TaxiCheckResponseDto(
            id = taxi.id
        )
    }

    @Transactional(readOnly = true)
    fun findDriverById(driverId: Long): DrivercheckResponseDto {
        val driver = driverRepository.findById(driverId).orElseThrow { Exception("기사를 찾을수 없습니다.") }
        return DrivercheckResponseDto(
            id = driver.id,
            email =driver.user.email
        )
    }

}
