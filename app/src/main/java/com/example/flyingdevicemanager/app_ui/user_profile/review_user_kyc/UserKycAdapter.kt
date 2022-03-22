package com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.UserItemBinding
import com.example.flyingdevicemanager.models.User
import com.squareup.picasso.Picasso

class UserKycAdapter(private val listener: ClickListener) : RecyclerView.Adapter<UserKycAdapter.MyViewHolder>() {
    
    var items = listOf<User>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    
    class MyViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.item
        
        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            binding.userEmail.text = "Email: ${user.email}"
            binding.userName.text = "User name: ${user.name}"
            binding.status.visibility = View.GONE
            if (user.avatar != null && user.avatar != "") {
                Picasso.get().load(user.avatar).fit().centerCrop().into(binding.avatar)
            } else {
                binding.avatar.setImageResource(R.drawable.ic_baseline_person_24)
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
        holder.item.setOnClickListener {
            listener.showUserKycDetail(items[position])
        }
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener {
        fun showUserKycDetail(user: User)
    }
}