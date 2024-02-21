package com.example.farewelltaxi.model

import jakarta.persistence.*

@Entity
@Table(name = "favorite_locations")
class FavoriteLocation(
    @Column(name = "location_name")
    var locationName: String,

    @Column(name = "coordinates")
    var coordinates: String,

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    var passenger: Passenger,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)



