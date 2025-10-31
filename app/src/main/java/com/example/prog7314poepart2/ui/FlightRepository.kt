package com.example.prog7314poepart2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlightRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://test.api.amadeus.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(AmadeusService::class.java)

    suspend fun getFlights(
        clientId: String,
        clientSecret: String,
        origin: String,
        destination: String,
        departureDate: String,
        returnDate: String? = null
    ): List<FlightOffer> {
        return try {
            val tokenResponse = api.getAccessToken(clientId = clientId, clientSecret = clientSecret)
            val token = "Bearer ${tokenResponse.accessToken}"
            val flightResponse = api.getFlightOffers(token, origin, destination, departureDate, returnDate)
            flightResponse.data ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
