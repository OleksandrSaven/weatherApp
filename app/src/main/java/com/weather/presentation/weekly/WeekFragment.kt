package com.weather.presentation.weekly

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.data.adapters.DaysAdapter
import com.weather.databinding.FragmentWeekBinding
import com.weather.presentation.home.HomeViewModel

class WeekFragment : Fragment() {
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentWeekBinding
    private lateinit var adapter: DaysAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DaysAdapter()
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.days.observe(viewLifecycleOwner, Observer {
            adapter.weathers = it
        })
        binding.apply {
            recyclerView.layoutManager = manager
            recyclerView.adapter = adapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeekBinding.inflate(inflater)
        return binding.root
    }
}
