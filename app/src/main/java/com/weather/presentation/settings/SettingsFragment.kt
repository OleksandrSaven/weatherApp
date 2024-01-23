package com.weather.presentation.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.weather.R
import com.weather.data.repository.PlaceRepositoryImpl
import com.weather.domain.model.place.Place
import com.weather.domain.util.Resource
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private val repository = PlaceRepositoryImpl()
    private lateinit var searchView: SearchView
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        searchView = view.findViewById(R.id.searchView)
        resultTextView = view.findViewById(R.id.textView8)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(!query.isNullOrBlank()) {
                    lifecycleScope.launch {
                        val result = getPlace(query)
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
