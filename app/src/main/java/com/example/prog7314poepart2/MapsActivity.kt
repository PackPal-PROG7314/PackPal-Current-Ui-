package com.example.prog7314poepart2

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var destination: String
    private lateinit var tripName: String
    private lateinit var tvDestination: TextView
    private lateinit var tvTripInfo: TextView
    private lateinit var rvAttractions: RecyclerView
    private lateinit var btnFindAttractions: Button

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Get trip data from intent
        destination = intent.getStringExtra("DESTINATION") ?: ""
        tripName = intent.getStringExtra("TRIP_NAME") ?: ""

        initializeViews()
        setupMap()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvDestination = findViewById(R.id.tvDestination)
        tvTripInfo = findViewById(R.id.tvTripInfo)
        rvAttractions = findViewById(R.id.rvAttractions)
        btnFindAttractions = findViewById(R.id.btnFindAttractions)

        tvDestination.text = "Destination: $destination"
        tvTripInfo.text = "Trip: $tripName"

        // Setup RecyclerView for attractions
        rvAttractions.layoutManager = LinearLayoutManager(this)
        rvAttractions.adapter = AttractionsAdapter(emptyList()) { attraction ->
            // Handle attraction click
            Toast.makeText(this, "Selected: ${attraction.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupClickListeners() {
        btnFindAttractions.setOnClickListener {
            if (::googleMap.isInitialized) {
                val currentLocation = googleMap.cameraPosition.target
                loadNearbyAttractions(currentLocation)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        // Enable current location if permission granted
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        // Geocode the destination and move camera
        CoroutineScope(Dispatchers.IO).launch {
            val location = geocodeDestination(destination)
            withContext(Dispatchers.Main) {
                location?.let {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title("Your Destination: $destination")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))

                    // Load nearby attractions automatically
                    loadNearbyAttractions(it)
                } ?: run {
                    // If geocoding fails, show error and use default location
                    Toast.makeText(
                        this@MapsActivity,
                        "Could not find location for $destination. Showing default map.",
                        Toast.LENGTH_LONG
                    ).show()
                    val defaultLocation = LatLng(0.0, 0.0)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 2f))
                }
            }
        }

        // Set map click listener
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
            )
            loadNearbyAttractions(latLng)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (::googleMap.isInitialized) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        googleMap.isMyLocationEnabled = true
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied. Some features may not work properly.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun geocodeDestination(destination: String): LatLng? {
        return try {
            // Try Android Geocoder first
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocationName(destination, 1)
                }

                if (addresses?.isNotEmpty() == true) {
                    return LatLng(addresses[0].latitude, addresses[0].longitude)
                }
            }

            // Fallback to hardcoded locations
            getFallbackLocation(destination)

        } catch (e: Exception) {
            Log.e("MapsActivity", "Geocoding failed: ${e.message}", e)
            getFallbackLocation(destination)
        }
    }

    private fun getFallbackLocation(destination: String): LatLng? {
        return when (destination.lowercase(Locale.ROOT)) {
            "usa", "united states", "united states of america" -> LatLng(38.9072, -77.0369) // Washington DC
            "uk", "united kingdom", "england", "britain" -> LatLng(51.5074, -0.1278) // London
            "france" -> LatLng(48.8566, 2.3522) // Paris
            "germany" -> LatLng(52.5200, 13.4050) // Berlin
            "japan" -> LatLng(35.6762, 139.6503) // Tokyo
            "australia" -> LatLng(-35.2809, 149.1300) // Canberra
            "canada" -> LatLng(45.4215, -75.6972) // Ottawa
            "italy" -> LatLng(41.9028, 12.4964) // Rome
            "spain" -> LatLng(40.4168, -3.7038) // Madrid
            "china" -> LatLng(39.9042, 116.4074) // Beijing
            "india" -> LatLng(28.6139, 77.2090) // New Delhi
            "brazil" -> LatLng(-15.7975, -47.8919) // Brasília
            "south africa" -> LatLng(-25.7461, 28.1881) // Pretoria
            "mexico" -> LatLng(19.4326, -99.1332) // Mexico City
            "russia" -> LatLng(55.7558, 37.6173) // Moscow
            "south korea" -> LatLng(37.5665, 126.9780) // Seoul
            "egypt" -> LatLng(30.0444, 31.2357) // Cairo
            else -> {
                Log.w("MapsActivity", "No fallback location for: $destination")
                LatLng(0.0, 0.0) // Default fallback
            }
        }
    }

    private fun loadNearbyAttractions(location: LatLng) {
        // Mock data for nearby attractions - you can replace this with Google Places API
        val mockAttractions = listOf(
            Attraction("Local Museum", "Cultural site", 4.2, "1.2 km"),
            Attraction("City Park", "Beautiful green space", 4.5, "0.8 km"),
            Attraction("Shopping Mall", "Shopping center", 4.0, "2.1 km"),
            Attraction("Historic Monument", "Historical landmark", 4.7, "1.5 km"),
            Attraction("Art Gallery", "Contemporary art", 4.3, "1.8 km"),
            Attraction("Restaurant District", "Various cuisines", 4.6, "0.5 km"),
            Attraction("Botanical Garden", "Nature and plants", 4.4, "3.2 km"),
            Attraction("Sports Stadium", "Sports venue", 4.1, "4.0 km")
        )

        rvAttractions.adapter = AttractionsAdapter(mockAttractions) { attraction ->
            // Add marker for selected attraction
            val attractionLocation = LatLng(
                location.latitude + (Math.random() - 0.5) * 0.02,
                location.longitude + (Math.random() - 0.5) * 0.02
            )
            googleMap.addMarker(
                MarkerOptions()
                    .position(attractionLocation)
                    .title(attraction.name)
                    .snippet("⭐ ${attraction.rating} • ${attraction.distance}")
            )

            // Move camera to the selected attraction
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(attractionLocation, 14f))

            Toast.makeText(
                this,
                "Showing ${attraction.name} on map",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Show success message
        Toast.makeText(
            this,
            "Found ${mockAttractions.size} nearby attractions",
            Toast.LENGTH_SHORT
        ).show()
    }
}

data class Attraction(
    val name: String,
    val type: String,
    val rating: Double,
    val distance: String = ""
)

class AttractionsAdapter(
    private val attractions: List<Attraction>,
    private val onItemClick: (Attraction) -> Unit
) : RecyclerView.Adapter<AttractionsAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(android.R.id.text1)
        val details: TextView = itemView.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attraction = attractions[position]
        holder.name.text = attraction.name
        holder.details.text = "${attraction.type} • ⭐${attraction.rating} • ${attraction.distance}"

        holder.itemView.setOnClickListener {
            onItemClick(attraction)
        }
    }

    override fun getItemCount() = attractions.size
}