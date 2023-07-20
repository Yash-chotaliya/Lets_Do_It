package com.example.letsconnect.activities.homepage.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.letsconnect.activities.homepage.ITaskAdapter
import com.example.letsconnect.activities.homepage.Model
import com.example.letsconnect.activities.homepage.TaskAdapter
import com.example.letsconnect.databinding.FragmentCompletedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CompletedFrag : Fragment(),ITaskAdapter {

    private lateinit var binding : FragmentCompletedBinding
    private lateinit var list : ArrayList<Model>
    private lateinit var dbRef : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(layoutInflater,container,false)

        list = arrayListOf()
        auth = FirebaseAuth.getInstance()

        getTasks()

        return binding.root
    }

    private fun getTasks(){
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid!!)
        dbRef.child("Tasks").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("completed").value as Boolean){
                            list.add(0,Model(
                                i.child("task").value.toString(),
                                i.child("completed").value as Boolean
                            ))
                        }
                    }
                }
                if(list.isEmpty())
                    binding.notask.visibility = View.VISIBLE
                else{
                    binding.notask.visibility = View.GONE
                    binding.rv.layoutManager = LinearLayoutManager(context)
                    binding.rv.adapter = TaskAdapter(list,this@CompletedFrag)
                }
                binding.pbar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onItemDelete(position:Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid!!)
        dbRef.child("Tasks").child(list[position].task).removeValue()
    }

    override fun onItemConditionChanged(position: Int) {
        if(!list[position].Completed){
            list[position].Completed = true
            getTasks()
        }
        else{
            list[position].Completed = false
        }
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser?.uid!!)
        dbRef.child("Tasks").child(list[position].task).setValue(list[position])

    }

}