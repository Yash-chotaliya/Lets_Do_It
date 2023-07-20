package com.example.letsconnect.activities.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.letsconnect.databinding.ActivitySignUpBinding

class SignUp1 : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.next.setOnClickListener {
            nextStep()
        }
        binding.signin.setOnClickListener {
            startActivity(Intent(this,SignIn::class.java))
            finish()
        }
    }

    private fun nextStep(){
        val email = binding.email.text.toString()
        val pass = binding.password.text.toString()
        val cPass = binding.cpassword.text.toString()

        if(email.isNotEmpty() && pass.isNotEmpty() && cPass.isNotEmpty()){
            if(pass.length<6 || cPass.length<6){
                Toast.makeText(this,"password must be atLeast 6 character",Toast.LENGTH_SHORT).show()
            }
            if(pass == cPass){
                val intent = Intent(this,SignUp2::class.java)
                intent.putExtra("email",email)
                intent.putExtra("password",pass)
                startActivity(intent)
            }
            else
                Toast.makeText(this,"password is not matching",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"fill all data",Toast.LENGTH_SHORT).show()
        }

    }
}