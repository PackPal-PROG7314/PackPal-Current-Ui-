package com.example.prog7314poepart2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class FlightAdapter(private var flights: List<FlightOffer>) :
    RecyclerView.Adapter<FlightAdapter.FlightViewHolder>() {

    private val airportToCountry = mapOf(
        // South Africa
        "JNB" to "South Africa",
        "CPT" to "South Africa",
        "DUR" to "South Africa",
        "PLZ" to "South Africa",
        "LHR" to "United Kingdom",
        "LGW" to "United Kingdom",
        "MAN" to "United Kingdom",
        "EDI" to "United Kingdom",
        "BHX" to "United Kingdom",
        "GLA" to "United Kingdom",
        "JFK" to "United States",
        "LAX" to "United States",
        "ORD" to "United States",
        "ATL" to "United States",
        "DFW" to "United States",
        "SFO" to "United States",
        "MIA" to "United States",
        "SEA" to "United States",
        "BOS" to "United States",
        "IAD" to "United States",
        "PHX" to "United States",
        "DEN" to "United States",
        "LAS" to "United States",
        "CLT" to "United States",
        "CDG" to "France",
        "ORY" to "France",
        "NCE" to "France",
        "LYS" to "France",
        "MRS" to "France",
        "FRA" to "Germany",
        "MUC" to "Germany",
        "BER" to "Germany",
        "DUS" to "Germany",
        "HAM" to "Germany",
        "CGN" to "Germany",
        "AMS" to "Netherlands",
        "EIN" to "Netherlands",
        "RTM" to "Netherlands",
        "MAD" to "Spain",
        "BCN" to "Spain",
        "AGP" to "Spain",
        "SVQ" to "Spain",
        "DUB" to "Ireland",
        "SNN" to "Ireland",
        "DXB" to "United Arab Emirates",
        "AUH" to "United Arab Emirates",
        "DOH" to "Qatar",
        "SYD" to "Australia",
        "MEL" to "Australia",
        "BNE" to "Australia",
        "PER" to "Australia",
        "ADL" to "Australia",
        "AKL" to "New Zealand",
        "WLG" to "New Zealand",
        "CHC" to "New Zealand",
        "NRT" to "Japan",
        "HND" to "Japan",
        "KIX" to "Japan",
        "ITM" to "Japan",
        "PEK" to "China",
        "PVG" to "China",
        "CAN" to "China",
        "SZX" to "China",
        "CTU" to "China",
        "DEL" to "India",
        "BOM" to "India",
        "BLR" to "India",
        "MAA" to "India",
        "HYD" to "India",
        "GRU" to "Brazil",
        "GIG" to "Brazil",
        "BSB" to "Brazil",
        "SDU" to "Brazil",
        "CNF" to "Brazil",
        "YYZ" to "Canada",
        "YVR" to "Canada",
        "YUL" to "Canada",
        "YYC" to "Canada",
        "YEG" to "Canada",
        "HKG" to "Hong Kong",
        "SIN" to "Singapore",
        "ICN" to "South Korea",
        "GMP" to "South Korea",
        "BKK" to "Thailand",
        "HKT" to "Thailand",
        "CNX" to "Thailand",
        "MEX" to "Mexico",
        "CUN" to "Mexico",
        "GDL" to "Mexico",
        "LIM" to "Peru",
        "SCL" to "Chile",
        "EZE" to "Argentina",
        "AEP" to "Argentina",
        "JED" to "Saudi Arabia",
        "RUH" to "Saudi Arabia",
        "CAI" to "Egypt",
        "LOS" to "Nigeria",
        "ABV" to "Nigeria",
        "NBO" to "Kenya",
        "DAR" to "Tanzania",
        "ACC" to "Ghana",
        "IST" to "Turkey",
        "SAW" to "Turkey",
        "ATH" to "Greece",
        "HEL" to "Finland",
        "OSL" to "Norway",
        "CPH" to "Denmark",
        "STO" to "Sweden",
        "ZRH" to "Switzerland",
        "GVA" to "Switzerland",
        "VIE" to "Austria",
        "PRG" to "Czech Republic",
        "WAW" to "Poland",
        "KRK" to "Poland"
    )


    private val expandedPositions = mutableSetOf<Int>()
    private var cheapestPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flight_card, parent, false)
        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight = flights[position]
        val firstItinerary = flight.itineraries?.firstOrNull()
        val firstSegment = firstItinerary?.segments?.firstOrNull()
        val lastSegment = firstItinerary?.segments?.lastOrNull()

        val originCode = firstSegment?.departure?.iataCode ?: ""
        val destinationCode = lastSegment?.arrival?.iataCode ?: ""
        val originCountry = airportToCountry[originCode] ?: originCode
        val destinationCountry = airportToCountry[destinationCode] ?: destinationCode

        val depAt = firstSegment?.departure?.at ?: "N/A"
        val retAt = lastSegment?.arrival?.at ?: "N/A"

        val depDate = if (depAt != "N/A") depAt.substring(0,10) else "N/A"
        val depTime = if (depAt != "N/A") depAt.substring(11,16) else "N/A"

        val retDate = if (retAt != "N/A") retAt.substring(0,10) else "N/A"
        val retTime = if (retAt != "N/A") retAt.substring(11,16) else "N/A"

        val priceValue = flight.price?.total?.toFloatOrNull() ?: 0f

        // Format price in Rands with thousands separator
        val formattedPrice = NumberFormat.getCurrencyInstance(Locale("en","ZA")).format(priceValue)

        holder.title.text = "$originCode ($originCountry) → $destinationCode ($destinationCountry)"
        holder.subtitle.text = "Departure: $depDate at $depTime | Return: $retDate at $retTime"
        holder.price.text = formattedPrice

        // Highlight cheapest flight
        if (position == cheapestPosition) {
            holder.card.setCardBackgroundColor(Color.parseColor("#FFF9C4")) // light yellow
            holder.title.text = holder.title.text.toString() + " — Cheapest flight"
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
        }

        // Expanded layout
        holder.expandedLayout.visibility = if (expandedPositions.contains(position)) View.VISIBLE else View.GONE
        holder.flightCarrier.text = "Carrier: ${firstSegment?.carrierCode ?: "N/A"}"
        holder.flightDuration.text = "Duration: ${firstSegment?.duration ?: "N/A"}"

        val segmentsText = firstItinerary?.segments?.joinToString("\n") {
            val dep = it.departure?.iataCode ?: "N/A"
            val arr = it.arrival?.iataCode ?: "N/A"
            val depAtFull = it.departure?.at ?: "N/A"
            val time = if (depAtFull != "N/A") depAtFull.substring(11,16) else "N/A"
            "$dep → $arr at $time"
        } ?: "N/A"
        holder.flightSegments.text = "Segments:\n$segmentsText"

        // Click to expand/collapse
        holder.card.setOnClickListener {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position)
                holder.expandedLayout.visibility = View.GONE
            } else {
                expandedPositions.add(position)
                holder.expandedLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = flights.size

    fun updateData(newFlights: List<FlightOffer>) {
        // Sort flights by price ascending
        flights = newFlights.sortedBy { it.price?.total?.toFloatOrNull() ?: Float.MAX_VALUE }
        // Identify cheapest flight
        cheapestPosition = if (flights.isNotEmpty()) 0 else -1
        expandedPositions.clear()
        notifyDataSetChanged()
    }

    class FlightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.flightCard)
        val title: TextView = itemView.findViewById(R.id.flightTitle)
        val subtitle: TextView = itemView.findViewById(R.id.flightSubtitle)
        val price: TextView = itemView.findViewById(R.id.flightPrice)
        val expandedLayout: LinearLayout = itemView.findViewById(R.id.expandedLayout)
        val flightCarrier: TextView = itemView.findViewById(R.id.flightCarrier)
        val flightDuration: TextView = itemView.findViewById(R.id.flightDuration)
        val flightSegments: TextView = itemView.findViewById(R.id.flightSegments)
    }
}
