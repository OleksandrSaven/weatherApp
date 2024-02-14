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
import com.weather.ui.adapters.HourlyAdapter
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private  val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: HourlyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = HourlyAdapter()
        binding.apply {
            recyclerview.layoutManager = manager
            recyclerview.adapter = adapter
        }

        viewModel.weatherState.observe(viewLifecycleOwner, Observer {
            adapter.weathers = it.weatherByHourly

            binding.cityText.text  = it.city


            if (it.errorMessage != null) {
                Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
            if(it.loadingState){
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
        })

     /*   viewModel.city.observe(viewLifecycleOwner, Observer {
            binding.cityText.text = it
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        })


        viewModel.loadingState.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading) {
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
        })
*/

        viewModel.currentHour.observe(viewLifecycleOwner, Observer {
            binding.apply {
                if (it != null) {
                    imageView.setImageResource(it.weatherType.iconId)

                    val today = it.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                    val todayFormatter = resources.getString(R.string.today_format, today.toString())
                    todayText.text = todayFormatter

                    val temperature = it.temperature
                    val temperatureFormatted = resources.getString(R.string.temperature_format,
                        temperature.toString())
                    temperatureText.text = temperatureFormatted

                    val pressureTextValue = it.pressure
                    val pressureFormatted = resources.getString(R.string.pressure_format,
                        pressureTextValue.toString())
                    pressureText.text = pressureFormatted

                    val humidityTextValue = it.humidity
                    val humidityFormatter = resources.getString(R.string.humidity_format,
                        humidityTextValue.toString())
                    humidityText.text = humidityFormatter

                    val windTextValue = it.windSpeed
                    val windFormatter = resources.getString(R.string.wind_format,
                        windTextValue.toString())
                    windText.text = windFormatter

                    forecastText.text = it.weatherType.description
                }
            }
        })

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
