package com.jasmeet.e_commerce.fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jasmeet.e_commerce.R
import com.jasmeet.e_commerce.activities.TempActivity2
import com.jasmeet.e_commerce.constants.Constants.USER_COLLECTION
import com.jasmeet.e_commerce.databinding.FragmentRegisterBinding
import com.jasmeet.e_commerce.model.UserInfo

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        // button for showing password
        binding.btnShowPassword.setOnClickListener {
            if (binding.btnShowPassword.isChecked){
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.edtConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else
            {
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.edtConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //button for Creating a new account
        binding.btnRegister.setOnClickListener {

            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            val email = binding.edtEmail.text.toString()
            val name = binding.edtName.text.toString()

            if (email.isEmpty()){
                binding.edtEmail.error = "Email is required !"
            }
            if (password.isEmpty()){
                binding.edtPassword.error = "Password is required !"
            }
            if (confirmPassword.isEmpty()){
                binding.edtConfirmPassword.error ="Confirm password is required !"
            }
            if (name.isEmpty()){
                binding.edtName.error= "Name is required !"
            }
            if (confirmPassword !=password && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                Toast.makeText(requireContext(),"Password is not matching !",Toast.LENGTH_SHORT).show()
            }
            else{
                validateInfo(email,password,name,confirmPassword)
            }

        }

        //button  for navigation from registration to login
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_RegisterFragment_to_loginFragment)
        }


        return binding.root
    }

    // function for validating email and password
    private fun validateInfo(email: String, password: String, name: String, confirmPassword: String) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()) {
            Toast.makeText(requireContext(), "Enter a valid Email id !", Toast.LENGTH_SHORT).show()
        }
        if (password.length <= 6 && confirmPassword.length<= 6 && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            Toast.makeText(
                requireContext(),
                "Password length must be greater than 6 characters !!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length>6 && password == confirmPassword && name.isNotEmpty()){

            binding.btnRegister.startAnimation()

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                        saveUserInfo(it.user!!.uid,email,name)
                    binding.edtEmail.text= null
                    binding.edtPassword.text = null
                    binding.edtName.text = null
                    binding.edtConfirmPassword.text = null
                }
                .addOnFailureListener {
                    binding.btnRegister.revertAnimation()
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun saveUserInfo(uid: String, email: String, name: String) {
        val data = UserInfo(
            userEmail = email,
            userName = name
        )
        db.collection(USER_COLLECTION)
            .document(uid)
            .set(data)
            .addOnSuccessListener {
                binding.btnRegister.revertAnimation()
                startActivity(Intent(requireContext(), TempActivity2::class.java))
                requireActivity().finish()

            }.addOnFailureListener {
                binding.btnRegister.revertAnimation()
                Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT).show()
            }

    }

}