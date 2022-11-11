package com.jasmeet.e_commerce.fragments.shopping

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.e_commerce.activities.MainActivity
import com.jasmeet.e_commerce.activities.NewProductActivity
import com.jasmeet.e_commerce.activities.ShoppingActivity
import com.jasmeet.e_commerce.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

   private lateinit var binding: FragmentProfileBinding
   private lateinit var firebaseAuth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.log.setOnClickListener {
            firebaseAuth.signOut()

            //back to main activity
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.addProducts.setOnClickListener {
            startActivity(Intent(requireContext(), NewProductActivity::class.java))
        }
        return binding.root
    }

  }