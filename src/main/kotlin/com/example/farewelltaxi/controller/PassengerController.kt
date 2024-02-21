package com.example.farewelltaxi.controller

import com.example.farewelltaxi.dto.Call.CallInfoForPassengerDto
import com.example.farewelltaxi.dto.FavoriteLocation.FavoriteLocationResponseDto
import com.example.farewelltaxi.dto.Passenger.PassengerRequestDto
import com.example.farewelltaxi.dto.Passenger.PassengerResponseDto
import com.example.farewelltaxi.model.FavoriteLocation
import com.example.farewelltaxi.model.Passenger
import com.example.farewelltaxi.service.FavoriteLocationService
import org.springframework.http.ResponseEntity
import com.example.farewelltaxi.service.PassengerService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/Passengers")

class PassengerController(
    private val passengerService: PassengerService,
    private val favoriteLocationService: FavoriteLocationService
){

    //승객 생성
    @PostMapping("/create")
    fun createPassenger(@RequestBody passengerRequestDto: PassengerRequestDto): ResponseEntity<PassengerResponseDto> {
        val passengerResponseDto = passengerService.createPassenger(passengerRequestDto)
        return ResponseEntity.ok(passengerResponseDto)
    }
    @PostMapping("/{passengerId}/favorite-locations")
    suspend fun addFavoriteLocation(
        @PathVariable passengerId: Long,
        @RequestParam query: String
    ): ResponseEntity<FavoriteLocationResponseDto> {
        val passenger = passengerService.findPassengerById(passengerId) // Passenger 조회
        val favoriteLocation = favoriteLocationService.addFavoriteLocation(passenger, query)
        return ResponseEntity.ok(favoriteLocation)
    }
    @GetMapping("/by-passenger/{passengerId}")
    fun findCallsByPassengerId(@PathVariable passengerId: Long): ResponseEntity<List<CallInfoForPassengerDto>> {
        val calls = passengerService.findCallsByPassengerId(passengerId)
        return ResponseEntity.ok(calls)
    }


}