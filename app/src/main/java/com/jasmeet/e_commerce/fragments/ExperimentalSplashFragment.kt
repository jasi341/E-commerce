package com.jasmeet.e_commerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.databinding.FragmentExperimentalSplashBinding

class ExperimentalSplashFragment : Fragment() {

    private lateinit var binding: FragmentExperimentalSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExperimentalSplashBinding.inflate(layoutInflater)

        binding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.action_experimentalSplashFragment_to_introFragment)
        }

        return binding.root
    }
}