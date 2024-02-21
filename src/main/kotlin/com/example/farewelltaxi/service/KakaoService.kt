package com.example.farewelltaxi.service
import com.example.farewelltaxi.dto.Kakao.AddressDetail
import com.example.farewelltaxi.dto.Kakao.AddressSearchResponse
import com.example.farewelltaxi.dto.Kakao.RouteCostResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import kotlinx.serialization.json.Json



@Service
class KakaoService(
    @Value("\${apikey}") private val apiKey: String
) {

    var client = HttpClient(CIO) {
        install(DefaultRequest) {
            header("Authorization", "KakaoAK $apiKey")
        }
    }

    val json = Json { ignoreUnknownKeys = true }

    suspend fun getDestinationCoordinates(query: String): AddressDetail {
        val response: HttpResponse = client.get("https://dapi.kakao.com/v2/local/search/address.json") {
            parameter("query", query)
        }
        val responseBody = response.readText()
        val addressSearchResponse = json.decodeFromString<AddressSearchResponse>(responseBody)
        // 첫 번째 문서의 주소 정보를 반환
        return addressSearchResponse.documents.firstOrNull() ?: throw Exception("No address found")
    }

    suspend fun getcalculateRouteCost(
        origin: String,
        destination: String,
        waypoints: String = "",
        priority: String = "RECOMMEND",
        carFuel: String = "GASOLINE",
        carHipass: Boolean = false,
        alternatives: Boolean = false,
        roadDetails: Boolean = false
    ): Int {
        val url = "https://apis-navi.kakaomobility.com/v1/directions"
        val response: HttpResponse = client.get(url) {
            parameter("origin", origin)
            parameter("destination", destination)
            if (waypoints.isNotEmpty()) parameter("waypoints", waypoints)
            parameter("priority", priority)
            parameter("car_fuel", carFuel)
            parameter("car_hipass", carHipass)
            parameter("alternatives", alternatives)
            parameter("road_details", roadDetails)
        }
        val responseBody = response.readText()
        // 구성된 Json 객체를 사용하여 응답을 파싱
        val routeCostResponse = json.decodeFromString<RouteCostResponse>(responseBody)
        // 첫 번째 경로의 택시 요금을 반환
        return routeCostResponse.routes.firstOrNull()?.summary?.fare?.taxi ?: 0
    }

}
