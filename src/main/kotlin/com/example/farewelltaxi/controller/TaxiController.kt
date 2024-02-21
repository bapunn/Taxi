package com.example.farewelltaxi.controller

import com.example.farewelltaxi.dto.Taxi.TaxiCheckResponseDto
import com.example.farewelltaxi.dto.Taxi.TaxiResponseDto
import com.example.farewelltaxi.model.Taxi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.farewelltaxi.service.TaxiService
import com.example.farewelltaxi.enum.TaxiLevel

@RestController
@RequestMapping("/api/taxis")
class TaxiController @Autowired constructor(
    private val taxiService: TaxiService
) {

    // 새로운 택시 생성
    @PostMapping()
    fun createTaxi(@RequestBody taxiLevel: TaxiLevel): ResponseEntity<TaxiResponseDto> {
        val newTaxi = taxiService.createTaxi(taxiLevel)
        return ResponseEntity.ok(newTaxi)
    }

    // 택시에 기사 추가(인원수 제한)
    @PostMapping("/{taxiId}/drivers/{driverId}")
    fun addDriverToTaxi(@PathVariable taxiId: Long, @PathVariable driverId: Long): ResponseEntity<String> {
        taxiService.addDriverToTaxi(driverId, taxiId)
        return ResponseEntity.ok("Driver successfully added to taxi.")
    }
    //택시조회
    @GetMapping("/{taxiId}")
    fun getTaxiById(@PathVariable taxiId: Long): ResponseEntity<TaxiCheckResponseDto> {
        val taxiCheckResponseDto = taxiService.findTaxiById(taxiId)
        return ResponseEntity.ok(taxiCheckResponseDto)
    }
}
