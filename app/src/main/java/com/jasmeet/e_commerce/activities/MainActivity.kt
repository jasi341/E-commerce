package com.jasmeet.e_commerce.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.jasmeet.e_commerce.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)
    }

    override fun onStart() {

        // check if user is already logged in
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            // user is already logged in
            val intent = Intent(this, ShoppingActivity::class.java)
            startActivity(intent)
            finish()
        }
//        else{
//            // user is not logged in
//            Toast.makeText(this,"Please Login to continue",Toast.LENGTH_SHORT).show()
//        }
        super.onStart()
    }
}