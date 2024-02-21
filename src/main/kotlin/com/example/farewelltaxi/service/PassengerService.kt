package com.example.farewelltaxi.service
import com.example.farewelltaxi.dto.Call.CallInfoForPassengerDto
import com.example.farewelltaxi.dto.Passenger.PassengerRequestDto
import com.example.farewelltaxi.dto.Passenger.PassengerResponseDto
import com.example.farewelltaxi.enum.Role
import com.example.farewelltaxi.model.Passenger
import com.example.farewelltaxi.model.User
import com.example.farewelltaxi.repository.CallRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import com.example.farewelltaxi.repository.UserRepository
import com.example.farewelltaxi.repository.PassengerRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Duration


@Service
class PassengerService(
    private val passwordEncoder: PasswordEncoder,
    private val passengerRepository: PassengerRepository,
    private val userRepository: UserRepository,
    private val callRepository: CallRepository
){
    //승객 생성
    @Transactional
    fun createPassenger(passengerRequestDto: PassengerRequestDto): PassengerResponseDto {
        val user = User(
            email = passengerRequestDto.email,
            password = passwordEncoder.encode(passengerRequestDto.password),
            name = passengerRequestDto.name,
            role = Role.PASSENGER
        )
        userRepository.save(user)

        val passenger = Passenger(
            user = user,
            nickname = passengerRequestDto.nickname
        )
        passengerRepository.save(passenger)
        return PassengerResponseDto(email = passenger.user.email, password = passenger.user.password, name = passenger.user.name, role = passenger.user.role, nickname = passenger.nickname)
    }

    fun findPassengerById(passengerId: Long): Passenger {
        return passengerRepository.findById(passengerId).orElseThrow {
            throw EntityNotFoundException("Passenger not found with id: $passengerId")
        }
    }

    fun findCallsByPassengerId(passengerId: Long): List<CallInfoForPassengerDto> {
        val calls = callRepository.findByPassengerId(passengerId)

        return calls.map { call ->
            val duration = if (call.startAt != null && call.endAt != null) {
                val duration = Duration.between(call.startAt, call.endAt)
                val minutes = duration.toMinutes()
                val seconds = duration.seconds % 60
                "${minutes}m ${seconds}s"
            } else {
                null
            }
            CallInfoForPassengerDto(
                endAt = call.endAt,
                duration = duration,
                taxiLevel = call.taxiLevel,
                origin = call.origin,
                destination = call.destination,
                price = call.price
            )
        }
    }


}



