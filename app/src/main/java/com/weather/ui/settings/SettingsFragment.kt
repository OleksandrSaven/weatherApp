package com.weather.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.weather.R
import com.weather.data.repository.PlaceRepository
import com.weather.domain.model.place.Place
import com.weather.domain.util.Resource
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val repository = PlaceRepository()
    private lateinit var searchView: SearchView
    private lateinit var resultTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility  = View.GONE
        searchView = view.findViewById(R.id.searchView)
        resultTextView = view.findViewById(R.id.textView8)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrBlank()) {
                    progressBar?.visibility = View.VISIBLE
                    lifecycleScope.launch {

                        val result = getPlace(query)
                       Thread.sleep(1000)
                        progressBar?.visibility  = View.GONE

                        if (result.data != null) {

                            resultTextView.text =
                                "Weather in " + result.data?.name.toString() +" " + result.data.location.lat + " " +
                                        result.data.location.long
                        } else {
                            resultTextView.text = "Not exist"
                        }
                    }
                }
                    return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
            return view
    }
    private suspend fun getPlace(name: String): Resource<Place> {
        return repository.getPlace(name)
    }
}
