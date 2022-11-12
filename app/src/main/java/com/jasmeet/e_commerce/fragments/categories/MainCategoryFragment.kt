package com.jasmeet.e_commerce.fragments.categories

import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.jasmeet.e_commerce.adapter.SpecialProducts
import com.jasmeet.e_commerce.databinding.FragmentMainCategoryBinding
import com.jasmeet.e_commerce.model.Product

class MainCategoryFragment:Fragment() {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductRvAdapter :SpecialProducts
    private var fireStore= FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialRv()
        fetchSpecialProducts()
    }

    private fun fetchSpecialProducts() {
        fireStore.collection("products").whereEqualTo("category","Special Products").get().addOnSuccessListener {
            binding.spinKit.visibility=View.GONE
            val products = it.toObjects(Product::class.java)
            specialProductRvAdapter.differ.submitList(products)
        }.addOnFailureListener {
            binding.spinKit.visibility=View.GONE
            Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpecialRv() {
        specialProductRvAdapter = SpecialProducts()

        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductRvAdapter
        }
    }
}