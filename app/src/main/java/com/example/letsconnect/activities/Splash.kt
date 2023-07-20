package com.example.letsconnect.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.letsconnect.activities.homepage.MainPage
import com.example.letsconnect.activities.register.SignIn
import com.example.letsconnect.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class Splash : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if(auth.currentUser==null){
                startActivity(Intent(this,SignIn::class.java))
                finish()
            }
            else{
                startActivity(Intent(this,MainPage::class.java))
                finish()
            }
        },4000)

    }
}