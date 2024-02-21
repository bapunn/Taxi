package com.example.farewelltaxi.model
import com.example.farewelltaxi.dto.Call.CallInfoResponse
import com.example.farewelltaxi.dto.Call.CallResponse
import com.example.farewelltaxi.dto.Payment.PaymentDetails
import jakarta.persistence.*
import com.example.farewelltaxi.enum.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Table(name = "`CALL`")
class Call(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "ORIGIN", nullable = false)
    var origin: String,

    @Column(name = "DESTINATION", nullable = false)
    var destination: String,

    @Column(name = "PRICE", nullable = false)
    var price: Int,

    @Column(name = "passenger_nickname")
    var passengerNickname: String? = null,

    @Column(name = "passenger_id")
    var passengerId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "TAXI_ID")
    var taxi: Taxi? = null, // Call 수락 시 설정

    @ManyToOne
    @JoinColumn(name = "DRIVER_ID")
    var driver: Driver? = null,// Call 수락 시 설정

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_TYPE")
    var paymentType: PaymentType,

    @Enumerated(EnumType.STRING)
    @Column(name = "TAXI_LEVEL")
    var taxiLevel: TaxiLevel,

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    var status: Status?=null,

    @Column(name = "START_AT")
    var startAt: LocalDateTime?=null,

    @Column(name = "ACCEPTED_AT")
    var acceptedAt: LocalDateTime?=null,

    @Column(name = "END_AT")
    var endAt: LocalDateTime?=null,

    @Column(name = "OPERATING_TIME")
    var operatingtime: String?=null,

    @Column(name = "ACCEPT_CALCLED")
    var acceptcancled : Boolean=false
) {

    //단위 테스트
    fun accept(driver: Driver): Boolean {
        if (this.status == Status.WAIT && driver.taxi != null) {
            this.driver = driver
            this.taxi = driver.taxi
            this.status = Status.ACCEPT
            this.acceptedAt = LocalDateTime.now()
            return true
        } else {
            throw IllegalStateException("이미 사람 있다.")
        }
    }


    //단위테스트
    fun acceptCancled(): Boolean {
        if (this.status == Status.ACCEPT && this.taxi != null) {
            this.acceptcancled = true
            return true
        } else {
            throw IllegalStateException("이미 사람 있다.")
        }
    }

    //단위 테스트
    fun start(driver: Driver): Boolean {
        if (this.status == Status.ACCEPT && driver.taxi != null) {
            this.status = Status.RUNNING
            this.startAt = LocalDateTime.now()
        }
        return true
    }

    //단위 테스트
    fun end(driver: Driver, callrequestDto: CallResponse): Boolean {
        if (this.status == Status.RUNNING && this.driver?.id == driver.id) {
            this.endAt = LocalDateTime.now()
            this.price = callrequestDto.price
            this.status = Status.STOPRUNNING
            this.startAt?.let { startAt ->
                val operatingTime = Duration.between(this.startAt, this.endAt)
                val minutes = operatingTime.toMinutes()
                val seconds = operatingTime.seconds % 60 // 총 시간에서 분을 제외한 나머지 초
                this.operatingtime = "${minutes}m ${seconds}s"
            }
            return true
        } else {
            throw Exception("ㅇㅅㅇ")
        }
    }


    fun Payment(call: Call, driver: Driver, passenger: Passenger, paymentDetails: PaymentDetails) {
        if (paymentDetails.amount == call.price) {
            when (call.paymentType) {
                PaymentType.Point -> {
                    passenger.deductCallCost(call.price)
                    driver.earning(call.price)
                }

                else -> {
                    driver.earning(call.price)
                }
            }
        } else {
            throw Exception("Payment amount mismatch")
        }
    }

    fun processAdditionalPayment(call: Call, driver: Driver, passenger: Passenger, totalPayment: Int) {
        if (totalPayment == call.price) {
            if (call.paymentType == PaymentType.Point && passenger.point >= call.price) {
                passenger.deductCallCost(totalPayment)
                driver.earning(totalPayment)
            } else if (call.paymentType != PaymentType.Point) {
                driver.earning(totalPayment)
            } else {
                throw Exception("재결제 금액 불일치")
            }
        } else {
            throw Exception("재결제 금액 불일치")
        }
    }
}








