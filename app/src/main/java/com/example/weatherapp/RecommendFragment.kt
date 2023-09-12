package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentRecommendBinding


class RecommendFragment : Fragment() {

    private lateinit var viewBinding: FragmentRecommendBinding
    lateinit var viewModel : MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRecommendBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        return viewBinding.root
    }

    override fun onStart() {
        viewBinding.temp.text = "최고온도:" + viewModel.atemp.maxTemp + "°C 최저온도: " + viewModel.atemp.mintemp + "°C"
        super.onStart()
    }
}