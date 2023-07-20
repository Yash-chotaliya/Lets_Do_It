package com.example.letsconnect.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letsconnect.activities.homepage.MainPage
import com.example.letsconnect.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    private lateinit var binding:ActivitySignInBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.signin.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this,MainPage::class.java))
                        finish()
                    }
                }
            }
            else{
                Toast.makeText(this,"insert all data",Toast.LENGTH_SHORT).show()
            }
        }
        binding.signup.setOnClickListener {
            startActivity(Intent(this,SignUp1::class.java))
            finish()
        }
    }
}