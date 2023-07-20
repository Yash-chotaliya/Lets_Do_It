package com.example.letsconnect.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letsconnect.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp2 : AppCompatActivity() {

    private lateinit var binding : ActivitySignUp2Binding
    private lateinit var auth : FirebaseAuth
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.signup.setOnClickListener {
            val username = binding.username.text.toString()
            val email = intent.getStringExtra("email").toString()
            val pass = intent.getStringExtra("password").toString()
            val user = User(username,email,pass)

            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                if(it.isSuccessful){
                    dbRef.child(auth.currentUser?.uid!!).setValue(user)
                    startActivity(Intent(this,SignIn::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this,it.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}