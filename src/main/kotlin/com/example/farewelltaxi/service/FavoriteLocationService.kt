package com.example.farewelltaxi.service

import com.example.farewelltaxi.dto.FavoriteLocation.FavoriteLocationResponseDto
import com.example.farewelltaxi.model.FavoriteLocation
import com.example.farewelltaxi.model.Passenger
import com.example.farewelltaxi.repository.FavoriteLocationRepository
import org.springframework.stereotype.Service

@Service
class FavoriteLocationService(
    private val favoriteLocationRepository: FavoriteLocationRepository,
    private val kakaoService: KakaoService
) {

    suspend fun addFavoriteLocation(passenger: Passenger, query: String): FavoriteLocationResponseDto {
        // KakaoService를 사용하여 주소 좌표 얻기
        val addressDetail = kakaoService.getDestinationCoordinates(query)
        // 좌표 정보를 포함하는 FavoriteLocation 객체 생성
        val favoriteLocation = FavoriteLocation(
            locationName = addressDetail.address_name,
            coordinates = "${addressDetail.x},${addressDetail.y}",
            passenger = passenger
        )
        favoriteLocationRepository.save(favoriteLocation)
        // FavoriteLocation 저장
        return FavoriteLocationResponseDto(locationName = favoriteLocation.locationName, coordinates = favoriteLocation.coordinates,passenger =favoriteLocation.passenger)
    }
}
