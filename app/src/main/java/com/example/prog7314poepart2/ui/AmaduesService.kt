package com.example.prog7314poepart2

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

// OAuth token
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String
)

// Flight data models
data class FlightOfferResponse(val data: List<FlightOffer>?)

data class FlightOffer(
    val id: String?,
    val price: Price?,
    val itineraries: List<Itinerary>?
)

data class Price(val total: String?, val currency: String?)

data class Itinerary(val segments: List<Segment>?)

data class Segment(
    val departure: FlightPoint?,
    val arrival: FlightPoint?,
    val carrierCode: String?,
    val duration: String?
)

data class FlightPoint(
    val iataCode: String?,
    val at: String?
)

// Retrofit interface
interface AmadeusService {

    @FormUrlEncoded
    @POST("v1/security/oauth2/token")
    suspend fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): TokenResponse

    @GET("v2/shopping/flight-offers")
    suspend fun getFlightOffers(
        @Header("Authorization") token: String,
        @Query("originLocationCode") origin: String,
        @Query("destinationLocationCode") destination: String,
        @Query("departureDate") departureDate: String,
        @Query("returnDate") returnDate: String? = null,
        @Query("adults") adults: Int = 1,
        @Query("max") max: Int = 10,
        @Query("currencyCode") currencyCode: String = "ZAR"
    ): FlightOfferResponse
}
