package com.example.geekhub.Model

data class IpLocation (
    val status: String,
    val country: String,
    val regionName: String,
    val city: String,
    val zip: String,
    val lat: Double,
    val lon: Double,
    val query: String
)