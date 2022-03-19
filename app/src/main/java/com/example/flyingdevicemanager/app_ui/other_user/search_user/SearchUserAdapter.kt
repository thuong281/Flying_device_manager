package com.example.flyingdevicemanager.app_ui.other_user.search_user

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingdevicemanager.databinding.UserItemBinding
import com.example.flyingdevicemanager.models.UserFollow

class SearchUserAdapter(private val listener: ClickListener) : RecyclerView.Adapter<SearchUserAdapter.MyViewHolder>() {
    
    var items = listOf<UserFollow>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    
    class MyViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val click = binding.status
        
        @SuppressLint("SetTextI18n")
        fun bind(userFollow: UserFollow) {
            binding.userEmail.text = "Email: ${userFollow.email}"
            binding.userName.text = "User name: ${userFollow.userName}"
            when (userFollow.status) {
                0 -> {
                    binding.status.text = "Send request"
                    binding.status.setBackgroundColor(Color.parseColor("#01904a"))
                }
                1 -> {
                    binding.status.text = "Pending"
                    binding.status.setBackgroundColor(Color.parseColor("#DDDD06"))
                }
                2 -> {
                    binding.status.text = "Followed"
                    binding.status.setBackgroundColor(Color.parseColor("#8F8D8D"))
                }
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
        holder.click.setOnClickListener {
            if (items[position].status != 0) return@setOnClickListener
            listener.sendRequest(items[position])
        }
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener {
        fun sendRequest(data: UserFollow)
    }
}