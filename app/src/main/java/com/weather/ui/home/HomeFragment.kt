package com.weather.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.R
import com.weather.databinding.FragmentHomeBinding
import com.weather.domain.model.weather.WeatherState
import com.weather.ui.adapters.HourlyAdapter
import java.time.format.DateTimeFormatter

class HomeFragment: Fragment() {
    private  val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HourlyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        searchCityBar()
        viewModel.weatherState.observe(viewLifecycleOwner, Observer {
            setupUi(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun searchCityBar() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(city: String?): Boolean {
                if(!city.isNullOrBlank()) {
                    viewModel.loadLocation(city)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = HourlyAdapter()
        binding.apply {
            recyclerview.layoutManager = manager
            recyclerview.adapter = adapter
        }
    }

    private fun setupUi(state: WeatherState) {
        adapter.weathers = state.weatherByHourly
        binding.cityText.text  = state.city
        binding.apply {
            if (state.currentValue != null) {
                imageView.setImageResource(state.currentValue!!.weatherType.iconId)

                val today = state.currentValue!!.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                val todayFormatter = resources.getString(R.string.today_format, today.toString())
                todayText.text = todayFormatter

                val temperature = state.currentValue!!.temperature
                val temperatureFormatted = resources.getString(
                    R.string.temperature_format,
                    temperature.toString())
                temperatureText.text = temperatureFormatted

                val pressureTextValue = state.currentValue!!.pressure
                val pressureFormatted = resources.getString(
                    R.string.pressure_format,
                    pressureTextValue.toString())
                pressureText.text = pressureFormatted

                val humidityTextValue = state.currentValue!!.humidity
                val humidityFormatter = resources.getString(
                    R.string.humidity_format,
                    humidityTextValue.toString())
                humidityText.text = humidityFormatter

                val windTextValue = state.currentValue!!.windSpeed
                val windFormatter = resources.getString(
                    R.string.wind_format,
                    windTextValue.toString())
                windText.text = windFormatter

                forecastText.text = state.currentValue!!.weatherType.description
            }
            showMessage(state.errorMessage)
        }
        loadingAnimation(state.loadingState)
    }

    private fun showMessage(errorMessage: String?) {
        if (errorMessage != null) {
            Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    private fun loadingAnimation(load: Boolean) {
        if (load) {
            binding.progressBar4.visibility = View.VISIBLE
            binding.searchView.visibility = View.GONE
            binding.frameLayout.visibility = View.GONE
            binding.recyclerview.visibility = View.GONE
        } else {
            binding.progressBar4.visibility = View.GONE
            binding.searchView.visibility = View.VISIBLE
            binding.frameLayout.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.VISIBLE
        }
    }
}
