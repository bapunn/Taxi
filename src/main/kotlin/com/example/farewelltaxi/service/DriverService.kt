package com.example.farewelltaxi.service

import com.example.farewelltaxi.dto.Call.CallInfoForDriverDto
import com.example.farewelltaxi.dto.Driver.DriverRequestDto
import com.example.farewelltaxi.dto.Driver.DriverResponseDto
import com.example.farewelltaxi.dto.Driver.DrivercheckResponseDto
import com.example.farewelltaxi.enum.Role
import com.example.farewelltaxi.enum.TaxiLevel
import com.example.farewelltaxi.model.Driver
import com.example.farewelltaxi.model.User
import com.example.farewelltaxi.repository.CallRepository
import com.example.farewelltaxi.repository.DriverRepository
import com.example.farewelltaxi.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class DriverService(
    private val driverRepository: DriverRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val callRepository: CallRepository

) {

    //기사생성
    @Transactional
    fun createDriver(driverRequestDTO: DriverRequestDto): DriverResponseDto {
        if (userRepository.existsByEmail(driverRequestDTO.email)) {
            throw IllegalStateException("Email is already in use")
        }

        val user = User(
            email = driverRequestDTO.email,
            password = passwordEncoder.encode(driverRequestDTO.password),
            name = driverRequestDTO.name,
            role = Role.DRIVER
        )
        userRepository.save(user)
        val driver = Driver(
            user = user,
            nickname = driverRequestDTO.nickname
        )
        driverRepository.save(driver)

        return DriverResponseDto(
            name = driver.user.name,
            email = driver.user.email,
            password = driver.user.name,
            role = driver.user.role,
            nickname = driver.nickname)
    }


    @Transactional
    fun calculateDailyAllowance(driverId: Long): Double {
        val driver = driverRepository.findById(driverId)
            .orElseThrow { EntityNotFoundException("Driver not found with id: $driverId") }
        val startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        val endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        // Driver 엔티티를 사용하여 일일 수당을 계산합니다.
        return callRepository.calculateDailyEarnings(driver, startOfDay, endOfDay) ?: 0.0
    }


    @Transactional
    fun updateDrivingStatus(driverId: Long, isDriving: Boolean): Boolean {
        val driver = driverRepository.findById(driverId).orElseThrow { Exception("Driver not found with id: $driverId") }

        driver.taxi?.let { taxi ->
            // 이미 운행 중인 다른 기사가 있는지 확인
            if (isDriving) {
                val activeDrivers = driverRepository.findByTaxiAndIsDrivingTrue(taxi)
                if (activeDrivers.isNotEmpty()) {
                    throw IllegalStateException("이미 운행 중인 기사가 있습니다.")
                }
            }

            driver.isDriving = isDriving
            driverRepository.save(driver) // 변경 사항 저장
            return true
        } ?: throw IllegalStateException("기사에 할당된 택시가 없습니다.")
    }



    @Transactional(readOnly = true)
    fun findDriverById(driverId: Long): DrivercheckResponseDto {
        val driver = driverRepository.findById(driverId).orElseThrow { Exception("기사를 찾을수 없습니다.") }
        return DrivercheckResponseDto(
            id = driver.id,
            email =driver.user.email
        )
    }


    fun findCallsByDriverTaxiLevel(driverId: Long): List<CallInfoForDriverDto> {
        val driver = driverRepository.findById(driverId).orElseThrow {
            EntityNotFoundException("Driver not found with id: $driverId")
        }
        val calls = when (driver.taxi?.taxiLevel) {
            TaxiLevel.BLACKTAXI -> callRepository.findByTaxiLevel(TaxiLevel.BLACKTAXI)
            else -> callRepository.findByTaxiLevelIn(listOf(TaxiLevel.TAXI, TaxiLevel.BLUETAXI))
        }
        return calls.map { call ->
            CallInfoForDriverDto(
                origin = call.origin,
                destination = call.destination,
                price = call.price,
                passengerNickname = call.passengerNickname,
                status = call.status
            )
        }
    }
}