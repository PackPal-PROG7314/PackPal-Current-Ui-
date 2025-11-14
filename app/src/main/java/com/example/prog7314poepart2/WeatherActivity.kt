package com.example.prog7314poepart2

import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val tvTripName = findViewById<TextView>(R.id.tvTripName)
        val tvCountry = findViewById<TextView>(R.id.tvCountry)
        val tvStartDate = findViewById<TextView>(R.id.tvStartDate)
        val tvEndDate = findViewById<TextView>(R.id.tvEndDate)
        val tvNotes = findViewById<TextView>(R.id.tvNotes)
        val tvTripTypes = findViewById<TextView>(R.id.tvTripTypes)
        val tvWeatherCondition = findViewById<TextView>(R.id.tvWeatherCondition)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnPackingList = findViewById<Button>(R.id.btnPackingList)
        val btnShareTrip = findViewById<Button>(R.id.btnShareTrip)
        val btnViewOnMap = findViewById<Button>(R.id.btnViewOnMap) // New Maps button

        var tripIndex = intent.getIntExtra("tripIndex", -1)
        Log.d("WeatherActivity", "Received tripIndex: $tripIndex, trips size: ${TripRepository.trips.size}")

        if (tripIndex == -1 && TripRepository.trips.isNotEmpty()) {
            tripIndex = 0
        }

        if (tripIndex != -1 && tripIndex < TripRepository.trips.size) {
            val trip = TripRepository.trips[tripIndex]

            tvTripName.text = "Trip: ${trip.tripName}"
            tvCountry.text = "Country: ${trip.country}"
            tvStartDate.text = "Start Date: ${trip.startDate}"
            tvEndDate.text = "End Date: ${trip.endDate}"
            tvNotes.text = "Notes: ${trip.notes ?: "None"}"
            tvTripTypes.text = "Trip Types: ${trip.tripTypes.joinToString(", ")}"
            tvWeatherCondition.text = "Weather: ${trip.weatherCondition}"

            val iconResId = when (trip.weatherCondition.lowercase()) {
                "clear" -> R.drawable.ic_sunny
                "clouds" -> R.drawable.ic_cloudy
                "rain" -> R.drawable.ic_rainy
                "snow" -> R.drawable.ic_snowy
                "thunderstorm" -> R.drawable.ic_thunderstorm
                else -> R.drawable.ic_sunny
            }
            weatherIcon.setImageResource(iconResId)

            val tripDays = calculateTripDays(trip.startDate, trip.endDate)

            btnPackingList.setOnClickListener {
                showPackingListDialog(trip.weatherCondition, tripDays)
            }

            btnShareTrip.setOnClickListener {
                shareTripAsPdf(trip, tripDays)
            }

            // New Maps button functionality
            btnViewOnMap.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("DESTINATION", trip.country)
                    putExtra("TRIP_NAME", trip.tripName)
                }
                startActivity(intent)
            }

        } else {
            tvWeatherCondition.text = "Weather: No trip available"
            weatherIcon.setImageResource(R.drawable.ic_sunny)
            btnPackingList.isEnabled = false
            btnViewOnMap.isEnabled = false
            Toast.makeText(this, "No trips available. Create a trip first.", Toast.LENGTH_LONG).show()
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun calculateTripDays(startDate: String, endDate: String): Int {
        val patterns = arrayOf("yyyy-MM-dd", "yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy")

        for (pattern in patterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                val start = sdf.parse(startDate)
                val end = sdf.parse(endDate)
                if (start != null && end != null) {
                    val diff = end.time - start.time
                    return (TimeUnit.MILLISECONDS.toDays(diff).toInt() + 1).coerceAtLeast(1)
                }
            } catch (_: Exception) { }
        }
        return 1
    }

    private fun showPackingListDialog(weatherCondition: String, days: Int) {
        val packingList = generatePackingList(weatherCondition, days)
        val packingText = packingList.joinToString("\n") { "• $it" }

        AlertDialog.Builder(this)
            .setTitle("Packing List ($days days)")
            .setMessage(packingText)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    // ****************************
    // ** SHARE TRIP AS PDF (with packing list)
    // ****************************
    private fun shareTripAsPdf(trip: Trip, days: Int) {

        val pdfFile = File(cacheDir, "Trip_${trip.tripName}.pdf")
        val packingList = generatePackingList(trip.weatherCondition, days)

        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 900, 1).create()
        val page = pdf.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 12f

        var y = 25

        // Title
        paint.textSize = 16f
        canvas.drawText("Trip Summary", 80f, y.toFloat(), paint)

        paint.textSize = 12f
        y += 30

        canvas.drawText("Trip Name: ${trip.tripName}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Country: ${trip.country}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Start Date: ${trip.startDate}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("End Date: ${trip.endDate}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Trip Types: ${trip.tripTypes.joinToString()}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Notes: ${trip.notes ?: "None"}", 10f, y.toFloat(), paint)
        y += 20
        canvas.drawText("Weather: ${trip.weatherCondition}", 10f, y.toFloat(), paint)

        // PACKING LIST
        y += 30
        paint.textSize = 14f
        canvas.drawText("Packing List ($days days):", 10f, y.toFloat(), paint)
        y += 20

        paint.textSize = 12f
        for (item in packingList) {
            canvas.drawText("• $item", 15f, y.toFloat(), paint)
            y += 18
        }

        pdf.finishPage(page)
        pdf.writeTo(FileOutputStream(pdfFile))
        pdf.close()

        val uri = FileProvider.getUriForFile(this, "$packageName.provider", pdfFile)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        startActivity(Intent.createChooser(intent, "Share Trip PDF"))
    }

    // Packing list generator
    private fun generatePackingList(weatherCondition: String, days: Int): List<String> {

        val base = when (weatherCondition.lowercase()) {
            "clear" -> listOf("Sunglasses", "Light clothing", "Sunscreen", "Hat")
            "clouds" -> listOf("Light jacket", "Comfortable clothes", "Umbrella")
            "rain" -> listOf("Rain jacket", "Waterproof shoes", "Umbrella")
            "snow" -> listOf("Winter coat", "Gloves", "Scarf", "Thermal wear")
            "thunderstorm" -> listOf("Waterproof jacket", "Quick-dry clothes", "Power bank")
            else -> listOf("Comfortable clothing", "Shoes", "Water bottle")
        }

        val essentials = listOf(
            "Underwear x$days",
            "Socks x$days",
            "T-shirts x$days",
            "Pants/Shorts x${(days / 2) + 1}",
            "Toiletries",
            "Phone charger",
            "Travel documents"
        )

        return essentials + base
    }
}

// (Andy's Tech Tutorials, 2022)