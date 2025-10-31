package com.example.prog7314poepart2.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.prog7314poepart2.R
import com.example.prog7314poepart2.Trip
import java.text.SimpleDateFormat
import java.util.Locale

class TripCardAdapter(private val context: Context, private val trips: List<Trip>) : BaseAdapter() {

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)

    override fun getCount(): Int = trips.size
    override fun getItem(position: Int): Any = trips[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.trip_card_item, parent, false)

        val trip = trips[position]

        // Bind the views
        view.findViewById<TextView>(R.id.tripTitle).text = trip.tripName
        view.findViewById<TextView>(R.id.tripCountry).text = "Country: ${trip.country}"

        val startDateFormatted = outputFormat.format(inputFormat.parse(trip.startDate)!!)
        val endDateFormatted = outputFormat.format(inputFormat.parse(trip.endDate)!!)

        view.findViewById<TextView>(R.id.tripStartDate).text = "Start: $startDateFormatted"
        view.findViewById<TextView>(R.id.tripEndDate).text = "End: $endDateFormatted"

        return view
    }
}
