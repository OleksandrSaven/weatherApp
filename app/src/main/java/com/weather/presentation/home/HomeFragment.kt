package com.weather.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.data.adapters.WeatherAdapter
import com.weather.databinding.FragmentHomeBinding
import com.weather.presentation.WeatherViewModel
import java.time.format.DateTimeFormatter

const val TODAY = "Today "
const val PATTERN = "HH:mm"
const val HPA = " hpa"
const val SIGN = " %"
const val CELSIUS = " °C"
const val WIND_SPEED = " km.h"


class HomeFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: WeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = WeatherAdapter()
        Log.i("MainActivity", viewModel.weather.value.toString())

        viewModel.weather.observe(viewLifecycleOwner, Observer{
            adapter.weathers = it
        })
        binding.apply {
            viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerview.layoutManager = manager
            recyclerview.adapter = adapter
        }

        viewModel.weatherData.observe(viewLifecycleOwner, Observer {
            viewModel.weatherData.value?.weatherType?.let {
                binding.imageView.setImageResource(it.iconId) }

            binding.apply {
                todayText.text =
                    TODAY + it?.time?.format(DateTimeFormatter.ofPattern(PATTERN))
                temperatureText.text = it?.temperature.toString() + CELSIUS
                pressureText.text = it?.pressure.toString() + HPA
                humidityText.text = it?.humidity.toString() + SIGN
                windText.text = "${it?.windSpeed.toString()}$WIND_SPEED"
            }
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
}
