package com.jasmeet.e_commerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroBinding.inflate(layoutInflater)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }


        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_RegisterFragment)
        }
        return(binding.root)


    }
}