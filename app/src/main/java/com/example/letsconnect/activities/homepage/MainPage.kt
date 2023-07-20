package com.example.letsconnect.activities.homepage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.letsconnect.R
import com.example.letsconnect.activities.homepage.fragments.AllFrag
import com.example.letsconnect.activities.homepage.fragments.CompletedFrag
import com.example.letsconnect.activities.homepage.fragments.PendingFrag
import com.example.letsconnect.activities.register.SignIn
import com.example.letsconnect.activities.removeAcc.RemoveAccount
import com.example.letsconnect.databinding.ActivityMainPageBinding
import com.example.letsconnect.databinding.LogoutDialogueBinding
import com.example.letsconnect.databinding.TaskAddDialogueboxBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainPage : AppCompatActivity() {

    private lateinit var binding : ActivityMainPageBinding
    private lateinit var list : ArrayList<Model>
    private lateinit var dbRef : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        list = arrayListOf()
        auth = FirebaseAuth.getInstance()

        getUserName()
        loadFragment(AllFrag())

        binding.btmnav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.all -> {
                    loadFragment(AllFrag())
                    true
                }
                R.id.completed -> {
                    loadFragment(CompletedFrag())
                    true
                }
                R.id.pending -> {
                    loadFragment(PendingFrag())
                    true
                }
                else->{
                    dialogue()
                    true
                }
            }
        }
    }

    private fun dialogue(){
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid!!)
        val builder = Dialog(this)
        val dialogueBoxBinding : TaskAddDialogueboxBinding = TaskAddDialogueboxBinding.inflate(layoutInflater)
        builder.setContentView(dialogueBoxBinding.root)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()

        dialogueBoxBinding.add.setOnClickListener {
            var taskName = dialogueBoxBinding.task.text.toString().filter { it.isLetterOrDigit().or(it.isWhitespace()) }
            if(taskName.isEmpty())
                taskName = "random"

            val task = Model(taskName,false)
            dbRef.child("Tasks").child(task.task).setValue(task).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,"task added", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"try again",Toast.LENGTH_SHORT).show()
                }
                builder.dismiss()
                loadFragment(AllFrag())
                binding.btmnav.selectedItemId=R.id.all
            }
                .addOnFailureListener {
                    Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
                }
        }
        dialogueBoxBinding.cancel.setOnClickListener {
            builder.dismiss()
            loadFragment(AllFrag())
            binding.btmnav.selectedItemId=R.id.all
        }
    }

    private fun getUserName(){
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid!!)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName = snapshot.child("username").value
                binding.username.text = userName.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

//    private fun resetTask(){
//        val lis = arrayListOf<Model>()
//        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid!!)
//        dbRef.child("Tasks").addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//                    for(i in snapshot.children){
//                        lis.add(0,Model(i.child("task").value.toString(),false))
//                    }
//                    dbRef.child("Tasks").removeValue()
//                    for(i in lis){
//                        dbRef.child("Tasks").child(i.task).setValue(i)
//                    }
//                }
//
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,SignIn::class.java))
        finish()
    }

    private fun logoutdialogue(){
        val builder = Dialog(this)
        val dialogueBoxBinding : LogoutDialogueBinding = LogoutDialogueBinding.inflate(layoutInflater)
        builder.setContentView(dialogueBoxBinding.root)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.show()

        dialogueBoxBinding.yes.setOnClickListener {
            logout()
        }
        dialogueBoxBinding.no.setOnClickListener {
            builder.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.logout ->{
                logoutdialogue()
            }
            R.id.deleteAcc->{
                startActivity(Intent(this,RemoveAccount::class.java))
            }
            R.id.reset->{
//                resetTask()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }
}