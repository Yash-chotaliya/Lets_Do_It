package com.example.letsconnect.activities.removeAcc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letsconnect.activities.register.SignIn
import com.example.letsconnect.databinding.ActivityRemoveAccountBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class RemoveAccount : AppCompatActivity() {

    private lateinit var binding: ActivityRemoveAccountBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.removeAcc.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty()){
                auth = FirebaseAuth.getInstance()
                val user = auth.currentUser!!

                val credential = EmailAuthProvider
                    .getCredential(email,pass)

                user.reauthenticate(credential)
                    .addOnCompleteListener {
                        user.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this,"Account removed", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this,SignIn::class.java))
                                    finish()
                                }
                            }
                    }
            }
        }
    }
}