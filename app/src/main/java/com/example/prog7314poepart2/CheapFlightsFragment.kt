package com.example.prog7314poepart2

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prog7314poepart2.databinding.FragmentCheapFlightsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class CheapFlightsFragment : Fragment() {

    private var _binding: FragmentCheapFlightsBinding? = null
    private val binding get() = _binding!!
    private val flightViewModel: FlightViewModel by viewModels()
    private lateinit var adapter: FlightAdapter

    // Replace with your Amadeus credentials
    private val CLIENT_ID = "eDk68xUiy1ASjc2zvC3Tzk9129vdMjTY"
    private val CLIENT_SECRET = "HQTmbzmrgJxXDgso"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheapFlightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = FlightAdapter(emptyList())
        binding.flightsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.flightsRecycler.adapter = adapter

        // Show DatePicker on click
        binding.dateInput.setOnClickListener { showDatePicker(binding.dateInput) }
        binding.returnDateInput.setOnClickListener { showDatePicker(binding.returnDateInput) }

        binding.searchButton.setOnClickListener {
            val origin = binding.originInput.text.toString()
            val destination = binding.destinationInput.text.toString()
            val departureDate = binding.dateInput.text.toString()
            val returnDate = binding.returnDateInput.text.toString().ifEmpty { null }

            flightViewModel.searchFlights(CLIENT_ID, CLIENT_SECRET, origin, destination, departureDate, returnDate)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            flightViewModel.flights.collectLatest { flights ->
                adapter.updateData(flights)
            }
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val monthString = String.format("%02d", selectedMonth + 1)
            val dayString = String.format("%02d", selectedDay)
            editText.setText("$selectedYear-$monthString-$dayString")
        }, year, month, day)

        datePicker.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
