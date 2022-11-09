package com.jasmeet.e_commerce.fragments.login_register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.activities.ShoppingActivity
import com.jasmeet.e_commerce.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialog:AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentLoginBinding.inflate(layoutInflater)

        // button for showing password
        binding.btnShowPassword.setOnClickListener {
            if (binding.btnShowPassword.isChecked){
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else
            {
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //button for checking the data and login
        binding.btnLogin.setOnClickListener {

            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty()){
                Toast.makeText(requireContext(),"Email id can't be empty !!",Toast.LENGTH_SHORT).show()
            }
            if (password.isEmpty()){
                Toast.makeText(requireContext(),"Password can't be empty !!",Toast.LENGTH_SHORT).show()
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                validateEmail(email, password)
            }

        }

        //button  for navigation from login to registration
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_RegisterFragment)
        }

        //button  for navigation from login to forgot password
        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        return binding.root
    }

    // function for validating data
    private fun validateEmail(email: String, password: String) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter a valid Email id !", Toast.LENGTH_SHORT).show()
        }
        if (password.length <= 6 && password.isNotEmpty()) {
            Toast.makeText(
                requireContext(),
                "Password length must be greater than 6 characters !!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6) {
            binding.btnLogin.startAnimation()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.edtEmail.text= null
                        binding.edtPassword.text = null
                        binding.btnLogin.revertAnimation()
                        startActivity(Intent(requireContext(), ShoppingActivity::class.java))
                        requireActivity().finish()
                    }
                }
                .addOnFailureListener {
                    binding.edtEmail.text= null
                    binding.edtPassword.text = null
                    binding.btnLogin.revertAnimation()
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }

        }
    }
}
