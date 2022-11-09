package com.jasmeet.e_commerce.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment() {

    private lateinit var binding: FragmentBaseCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }



}