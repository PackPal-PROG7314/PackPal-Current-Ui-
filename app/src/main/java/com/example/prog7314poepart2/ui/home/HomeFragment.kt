package com.example.prog7314poepart2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.prog7314poepart2.R
import com.example.prog7314poepart2.TripRepository
import com.example.prog7314poepart2.WeatherActivity

class HomeFragment : Fragment() {

    private val tripsList = TripRepository.trips
    private lateinit var adapter: TripCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listView)
        adapter = TripCardAdapter(requireContext(), tripsList)
        listView.adapter = adapter

        // Click on trip → open WeatherActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            startActivity(Intent(requireContext(), WeatherActivity::class.java).apply {
                putExtra("tripIndex", position)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
