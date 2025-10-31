package com.example.prog7314poepart2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlightViewModel : ViewModel() {

    private val repository = FlightRepository()
    private val _flights = MutableStateFlow<List<FlightOffer>>(emptyList())
    val flights: StateFlow<List<FlightOffer>> = _flights

    fun searchFlights(
        clientId: String,
        clientSecret: String,
        origin: String,
        destination: String,
        departureDate: String,
        returnDate: String? = null
    ) {
        viewModelScope.launch {
            _flights.value = repository.getFlights(clientId, clientSecret, origin, destination, departureDate, returnDate)
        }
    }
}
