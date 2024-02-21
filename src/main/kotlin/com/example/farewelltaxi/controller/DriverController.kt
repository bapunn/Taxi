package com.example.farewelltaxi.controller

import com.example.farewelltaxi.dto.Call.CallInfoForDriverDto
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.farewelltaxi.service.DriverService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import com.example.farewelltaxi.dto.Driver.DriverRequestDto
import com.example.farewelltaxi.dto.Driver.DriverResponseDto
import com.example.farewelltaxi.dto.Driver.DrivercheckResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/api/drivers")
class DriverController(
    private val driverService: DriverService,
) {
    //기사 생성
    @PostMapping("/create")
    fun createDriver(@RequestBody driverRequestDTO: DriverRequestDto): ResponseEntity<DriverResponseDto> {
            val driverResponseDto = driverService.createDriver(driverRequestDTO)
            return ResponseEntity.ok(driverResponseDto)
    }

    // 특정 기사의 일일 수입 계산
    @PreAuthorize("#userId == principal.username or hasRole('DRIVER')")
    @GetMapping("/{driverId}/daily-allowance")
    fun calculateDailyAllowance(@PathVariable driverId: Long): ResponseEntity<Double> {
        val dailyAllowance = driverService.calculateDailyAllowance(driverId)
        return ResponseEntity.ok(dailyAllowance)
    }

    // 특정 기사의 운전 상태 업데이트(조건 1택시당 1명이상 isdriving불가)
    @PreAuthorize("#userId == principal.username or hasRole('DRIVER')")
    @PatchMapping("/{driverId}/driving-status")
    fun updateDrivingStatus(@PathVariable driverId: Long, @RequestParam isDriving: Boolean): ResponseEntity<String> {
        val success = driverService.updateDrivingStatus(driverId, isDriving)
        return if (success) {
            ResponseEntity.ok("Driver's driving status updated successfully.")
        } else {
            ResponseEntity.notFound().build()
        }
    }
    // 특정 기사 조회
    @GetMapping("/{driverId}")
    fun getDriverById(@PathVariable driverId: Long): ResponseEntity<DrivercheckResponseDto> {
        val drivercheckResponseDto = driverService.findDriverById(driverId)
        return ResponseEntity.ok(drivercheckResponseDto)
    }
    @GetMapping("/by-driver/{driverId}")
    fun findCallsByDriverTaxiLevel(@PathVariable driverId: Long): ResponseEntity<List<CallInfoForDriverDto>> {
        val calls = driverService.findCallsByDriverTaxiLevel(driverId)
        return ResponseEntity.ok(calls)
    }

}