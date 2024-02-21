package com.example.farewelltaxi.controller
import com.example.farewelltaxi.dto.Call.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import com.example.farewelltaxi.service.CallService
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/call")
class CallController(
    val callService: CallService,
) {

    //call가격 출,목적지 반환(생성용 request)
    @PostMapping("/call-info")
    suspend fun getCallInfo(@RequestBody request: RequestType): CallInfoResponse {
        return callService.getCallInfo(request)
    }
    //call생성
    @PostMapping("/create")
    fun createcall(@RequestBody callResponse: CallResponse): Boolean {
        val call = callService.createCall(callResponse)
        val result = runBlocking {
            callService.checkCallStatus(call.id!!)
        }
        return result
    }

    @PostMapping("/{callId}/driveracceptCancelled")
    @PreAuthorize("#call.driver.email == authentication.principal.username")
    fun driveracceptCancelled(callId: Long) :ResponseEntity<String>{
        callService.acceptCancled(callId)
        callService.canclecleanup(callId)
        return ResponseEntity.ok("Call with ID $callId has been deleted.")
    }

    @PostMapping("/{callId}/acceptCancelled")
    @PreAuthorize("#call.passenger.email == authentication.principal.username")
    fun passengeracceptCancelled(callId: Long):ResponseEntity<String> {
        callService.acceptCancled(callId)
        callService.canclecleanup(callId)
        return ResponseEntity.ok("Call with ID $callId has been deleted.")
    }

        // 호출 수락
        @PostMapping("/{callId}/accept")
        fun acceptCall(@PathVariable callId: Long): ResponseEntity<String> {
            callService.acceptCall(callId)
            return ResponseEntity.ok("Call accepted successfully.")
        }

        // 운행 시작
        @PostMapping("/{callId}/start")
        fun startCall(@PathVariable callId: Long): ResponseEntity<String> {
            callService.startCall(callId)
            return ResponseEntity.ok("Call started successfully.")
            }

        // 운행 종료
        @PostMapping("/{callId}/end")
        fun endCall(@PathVariable callId: Long,@RequestBody callResponse: CallResponse): ResponseEntity<String> {
            callService.endCall(callId,callResponse)
            return ResponseEntity.ok("Call ended successfully.")
        }

        //본인조건에 맞는 call검색(기사)--responsedto 변경해서 보여주기



    }