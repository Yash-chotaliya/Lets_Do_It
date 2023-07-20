package com.example.letsconnect.activities.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letsconnect.R
import com.example.letsconnect.databinding.RvMainpageListBinding

class TaskAdapter(private val list:ArrayList<Model>,private val listener:ITaskAdapter) : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewHolder(RvMainpageListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

        binding.isCompleted.setOnClickListener {
            listener.onItemConditionChanged(binding.adapterPosition)
        }
        binding.delete.setOnClickListener {
            listener.onItemDelete(binding.adapterPosition)
        }
        return binding
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskName.text = list[position].task
        if(!list[position].Completed){
            holder.isCompleted.setImageResource(R.drawable.red_button)
        }
        else{
            holder.isCompleted.setImageResource(R.drawable.green_button)
        }
    }

    class ViewHolder(itemBinding: RvMainpageListBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val taskName : TextView = itemBinding.taskname
        val isCompleted : ImageButton = itemBinding.isCompleted
        val delete : ImageView = itemBinding.delete
    }
}

interface ITaskAdapter{
    fun onItemDelete(position:Int)
    fun onItemConditionChanged(position: Int)
}