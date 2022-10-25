package com.jasmeet.e_commerce.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSubmit.setOnClickListener {

            val email = binding.edtEmail.text.toString()

            if (email.isEmpty()){
                binding.edtEmail.error = "Enter your Email !!"
            }
            if (email.isNotEmpty())
                validateEmail(email)
        }

        return binding.root
    }

    private fun validateEmail(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireContext(),"Enter a valid Email id !",Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Toast.makeText(requireContext(),"Email sent successfully to rest your password !",Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(requireContext(),task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

}