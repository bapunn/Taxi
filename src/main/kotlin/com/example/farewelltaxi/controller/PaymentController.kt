package com.example.farewelltaxi.controller

import com.example.farewelltaxi.dto.Payment.*
import com.example.farewelltaxi.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment")
class PaymentController(val paymentService: PaymentService) {

    @PostMapping("/process")
    suspend fun processPayment(@RequestBody paymentRequest: PaymentRequest): ResponseEntity<String> {
        // 토큰을 이용해 결제 상세 정보를 조회
        val accessToken = paymentService.fetchAccessToken(paymentService.apisecret)
        val paymentDetails = paymentService.fetchPaymentDetails(paymentRequest.paymentId, accessToken)

        // 결제 상세 정보를 포함하여 초기 결제 처리
        paymentService.initialPayment(
            Payment(
                passengerId = paymentRequest.passengerId,
                callId = paymentRequest.callId,
                driverId = paymentRequest.driverId,
                paymentDetails = paymentDetails // 조회한 결제 상세 정보 사용
            )
        )
        return ResponseEntity.ok("결제가 성공적으로 처리되었습니다.")
    }


    // 재결제 처리
    @PostMapping("/additionalPayment")
    suspend fun processAdditionalPayment(
        @RequestBody additionalPaymentRequest: AdditionalPaymentRequest
    ): ResponseEntity<String> {
        val accessToken = paymentService.fetchAccessToken(paymentService.apisecret)
        // 서비스 계층에서 요구하는 결제 상세 정보를 결제 ID를 통해 조회
        val initialPaymentDetails = paymentService.fetchPaymentDetails(additionalPaymentRequest.initialPaymentId, accessToken )
        val additionalPaymentDetails = paymentService.fetchPaymentDetails(additionalPaymentRequest.additionalPaymentId,accessToken)

        // 서비스 계층의 AdditionalPayment 메서드 호출
        paymentService.additionalPayment(
            AdditionalPayment(
                passengerId = additionalPaymentRequest.passengerId,
                callId = additionalPaymentRequest.callId,
                driverId = additionalPaymentRequest.driverId,
                initialPaymentDetails = initialPaymentDetails,
                additionalPaymentDetails = additionalPaymentDetails
            )
        )

        return ResponseEntity.ok("추가 결제가 성공적으로 처리되었습니다.")
    }


    // 포인트 충전
    @PostMapping("/charge")
    suspend fun chargePoints(
        @RequestBody chargeRequest: ChargeRequest
    ): ResponseEntity<String> {
        val accessToken = paymentService.fetchAccessToken(paymentService.apisecret)
        val paymentDetails = paymentService.fetchPaymentDetails(chargeRequest.paymentId, accessToken)

        paymentService.pointcharge(chargeRequest.passengerId, paymentDetails)
        return ResponseEntity.ok("포인트가 성공적으로 충전되었습니다.")
    }
}
