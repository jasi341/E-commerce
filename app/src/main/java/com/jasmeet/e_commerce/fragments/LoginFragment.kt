package com.jasmeet.e_commerce.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.activities.TempActivity2
import com.jasmeet.e_commerce.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btnLogin.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty()){
                binding.edtEmail.error = "Enter your email !!"
            }
            if (password.isEmpty()){
                binding.edtPassword.error = "Enter password !!"
            }
            if (email.isNotEmpty() && password.isNotEmpty()){
                binding.btnLogin.startAnimation()

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            binding.btnLogin.revertAnimation()
                            startActivity(Intent(requireContext(),TempActivity2::class.java))
                            requireActivity().finish()
                        }
                        else{
                            binding.btnLogin.revertAnimation()

                            Toast.makeText(requireContext(),it.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        binding.btnLogin.revertAnimation()
                        Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_RegisterFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        return binding.root
    }

}