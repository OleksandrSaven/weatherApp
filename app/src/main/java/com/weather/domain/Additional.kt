/*
package com.weather.domain

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import java.time.format.DateTimeFormatter


    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WeatherAdapter

binding = ActivityMainBinding.inflate(layoutInflater)
setContentView(binding.root)
viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)


//viewModel.updateWeather()
val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
adapter = WeatherAdapter()
Log.i("MainActivity", viewModel.weather.value.toString())

viewModel.weather.observe(this, Observer{
    adapter.weathers = it
    binding.textView3.text =  buildString {
        append(viewModel.weatherData.value?.temperature.toString())
        append("°C")
    }
})
binding.textView2.setOnClickListener{

    Log.i("listiner", viewModel.weather.value.toString())
}


viewModel.weatherData.value?.weatherType?.let { binding.imageView.setImageResource(it.iconId) }
binding.textView2.text = buildString {
    append("Today ")
    append(viewModel.weatherData.value?.time?.format(DateTimeFormatter.ofPattern("HH:mm")))
}
binding.textView5.text = buildString {
    viewModel.weatherData.value?.let { append(it.pressure) }
    append(" hpa")
}
binding.textView6.text = buildString {
    append(viewModel.weatherData.value?.humidity.toString())
    append(" %")
}
binding.textView7.text = buildString {
    append(viewModel.weatherData.value?.windSpeed.toString())
    append(" km/h")
}
binding.recyclerview.layoutManager = manager
binding.recyclerview.adapter = adapter
}*/
