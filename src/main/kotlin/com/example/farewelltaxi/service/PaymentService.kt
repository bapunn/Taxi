package com.example.farewelltaxi.service

import com.example.farewelltaxi.dto.Payment.AdditionalPayment
import com.example.farewelltaxi.dto.Payment.Payment
import com.example.farewelltaxi.dto.Payment.PaymentDetails
import com.example.farewelltaxi.enum.PaymentType
import com.example.farewelltaxi.model.Call
import com.example.farewelltaxi.model.Driver
import com.example.farewelltaxi.model.Passenger
import com.example.farewelltaxi.repository.CallRepository
import com.example.farewelltaxi.repository.DriverRepository
import com.example.farewelltaxi.repository.PassengerRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jakarta.persistence.EntityNotFoundException
import javax.naming.Context


@Service
class PaymentService(
    @Value("\${apisecret}") val apisecret: String,
    val callrepository: CallRepository,
    val passengerrepository: PassengerRepository,
    val driverrepository: DriverRepository
)   {
        // API 응답을 위한 데이터 클래스
        @Serializable
        data class TokenResponse(
            val accessToken: String,
            val refreshToken: String,

        )
             var client = HttpClient(CIO) { install(JsonFeature) {
                    serializer = KotlinxSerializer(Json { ignoreUnknownKeys = true })
                }
        }
            suspend fun fetchAccessToken(apiSecret: String): String {

                    // API 요청을 보내고 응답을 받음
                    val response: TokenResponse = client.post("https://api.portone.io/login/api-secret") {
                    contentType(ContentType.Application.Json)
                    body = mapOf(apisecret to apiSecret)
                }

                    client.close()
                    return response.accessToken

            }
            suspend fun fetchPaymentDetails(paymentId: String, accessToken: String): PaymentDetails {
                   val httpStatement = client.get<HttpStatement>("https://api.portone.io/payments/$paymentId") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $accessToken")
                    }
                }
                    val response = httpStatement.execute()
                    if (response.status == HttpStatusCode.OK) {
                        val responseBody = response.readText()
                        client.close()
                        return Json.decodeFromString<PaymentDetails>(responseBody)
                    } else {
                        client.close()
                    throw Exception("Failed to fetch payment details")
                }
            }



            suspend fun initialPayment(payment: Payment) {
                val callId = payment.callId
                val driverId = payment.driverId
                val passengerId = payment.passengerId
                val paymentDetails = payment.paymentDetails
                val call = findCall(callId)
                val  driver = findDriver(driverId)
                val passenger = passengerrepository.findById(passengerId)
                    .orElseThrow { EntityNotFoundException("Passenger not found") }
                call.Payment(call,driver,passenger,paymentDetails)
            }


            suspend fun additionalPayment(additionalPayment: AdditionalPayment) {
                val callId = additionalPayment.callId
                val driverId = additionalPayment.driverId
                val passengerId = additionalPayment.passengerId
                val initialPaymentDetails = additionalPayment.initialPaymentDetails
                val additionalPaymentDetails = additionalPayment.additionalPaymentDetails
                val call = findCall(callId)
                val driver = findDriver(driverId)
                val passenger = findPassenger(passengerId)
                val initialpaymentdetails = initialPaymentDetails.amount
                val additionalpaymentdetails = additionalPaymentDetails.amount
                val totalPayment = initialpaymentdetails+additionalpaymentdetails

                call.processAdditionalPayment(call, driver, passenger, totalPayment)
            }

            suspend fun pointcharge(passengerId: Long, paymentDetails: PaymentDetails) {
                val passenger = passengerrepository.findById(passengerId).orElseThrow { EntityNotFoundException("Passenger not found")}
                val chargepoint= paymentDetails.amount
                    passenger.charge(chargepoint)
            }

            private suspend fun findCall(callId: Long): Call =
                callrepository.findById(callId).orElseThrow { EntityNotFoundException("Call not found") }

            private suspend fun findDriver(driverId: Long): Driver =
                driverrepository.findById(driverId).orElseThrow { EntityNotFoundException("Driver not found") }

            private suspend fun findPassenger(passengerId: Long): Passenger =
                passengerrepository.findById(passengerId).orElseThrow { EntityNotFoundException("Passenger not found") }
}
