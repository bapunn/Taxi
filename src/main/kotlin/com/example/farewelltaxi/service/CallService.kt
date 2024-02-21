package com.example.farewelltaxi.service

import com.example.farewelltaxi.dto.Call.*
import com.example.farewelltaxi.enum.Status
import com.example.farewelltaxi.enum.TaxiLevel
import com.example.farewelltaxi.model.Call
import com.example.farewelltaxi.repository.CallRepository
import com.example.farewelltaxi.repository.DriverRepository
import com.example.farewelltaxi.repository.PassengerRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class CallService (
    private val passengerRepository: PassengerRepository,
    val driverRepository: DriverRepository,
    private val callRepository: CallRepository,
    private val transactionTemplate: TransactionTemplate,
    val kakaoService: KakaoService,

) {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    suspend fun getCallInfo(request: RequestType): CallInfoResponse {
        // 출발지 좌표를 검색합니다.
        val originDetail = kakaoService.getDestinationCoordinates(request.origin)
        // 목적지 좌표를 검색합니다.
        val destinationDetail = kakaoService.getDestinationCoordinates(request.destination)
        // 검색된 좌표를 사용하여 경로 비용을 계산합니다.
        val price = kakaoService.getcalculateRouteCost(
            "${originDetail.x},${originDetail.y}", // 출발지 좌표
            "${destinationDetail.x},${destinationDetail.y}" // 목적지 좌표
        )
        val blueTaxiPrice = price + 5000
        val blackTaxiPrice = price + 10000

        // 필요한 정보를 포함하는 객체를 반환합니다.
        return CallInfoResponse(
            origin = originDetail.address_name,
            destination = destinationDetail.address_name,
            nomaltaxiprice = price,
            bluetaxiprice = blueTaxiPrice,
            blacktaxiprice = blackTaxiPrice,
            originCoordinates = "${originDetail.x},${originDetail.y}",
            destinationCoordinates = "${destinationDetail.x},${destinationDetail.y}"
            // 여기에 다른 필요한 정보를 추가할 수 있습니다.
        )
    }

    @Transactional
    fun createCall(callrequestDto: CallResponse): Call {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name

        // User의 이메일로 Passenger를 찾습니다.
        val passenger = passengerRepository.findByUserEmail(userEmail)
            ?: throw UsernameNotFoundException("Passenger not found with email: $userEmail")
        // Passenger 엔티티에서 id와 nickname을 직접 참조합니다.

        val newCall = Call(
            origin = callrequestDto.origin,
            destination = callrequestDto.destination,
            price = callrequestDto.price,
            taxiLevel = callrequestDto.taxiLevel,
            status = Status.WAIT,
            passengerId = passenger.id,  // Passenger의 ID를 저장
            passengerNickname = passenger.nickname, // Passenger의 닉네임을 저장
            paymentType = callrequestDto.paymentType)

            return callRepository.save(newCall)}





suspend fun checkCallStatus(callId: Long): Boolean {
    try {
        return withTimeout(100 * 1000) {
            var result: Boolean? = null  // 결과를 저장할 변수 선언
            while (result == null) {
                delay(1000L)
                result = transactionTemplate.execute {
                    val call = callRepository.findById(callId).orElseThrow()
                    entityManager.refresh(call)

                    if (call.status == Status.ACCEPT) {
                        true  // 상태가 ACCEPT이면 true 반환
                    } else {
                        null  // 그렇지 않으면 null 반환하여 반복 계속
                    }
                }
            }
            result == true  // 최종 결과 반환
        }
    } catch (e: TimeoutCancellationException) {
        cleanupCall(callId)
        return false
    }
}

    //call삭제  -> 승객입장에서의 call 삭제
    fun cleanupCall(callId: Long) {
        callRepository.deleteById(callId)
    }

    //게시글 조회후 수락,boolean 값 변경 및 운행 상태 변경
    @Transactional
    fun acceptCall(callId: Long): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val driverEmail = authentication.name
        val driver = driverRepository.findByUserEmail(driverEmail)
            ?: throw UsernameNotFoundException("Driver not found with email: $driverEmail")
        val call = callRepository.findById(callId).orElseThrow()
        call.accept(driver)
        return  true
    }

    @Transactional
    fun acceptCancled(callId: Long){
        val call = callRepository.findById(callId).orElseThrow()
        call.acceptCancled()
    }
    @Transactional
    fun canclecleanup(callId: Long){
        val call = callRepository.findById(callId).orElseThrow()
        if (call.acceptcancled){
            cleanupCall(callId)
        }
    }


    @Transactional
    fun startCall(callId: Long): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val driverEmail = authentication.name
        val driver = driverRepository.findByUserEmail(driverEmail)
            ?: throw UsernameNotFoundException("Driver not found with email: $driverEmail")
        val call = callRepository.findById(callId).orElseThrow()
        call.start(driver)
        return true
    }

    @Transactional
    fun endCall(callId: Long,callrequestDto: CallResponse): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val driverEmail = authentication.name
        val driver = driverRepository.findByUserEmail(driverEmail)
            ?: throw UsernameNotFoundException("Driver not found with email: $driverEmail")
        val call = callRepository.findById(callId).orElseThrow()
        call.end(driver, callrequestDto)
        return true
    }
}
