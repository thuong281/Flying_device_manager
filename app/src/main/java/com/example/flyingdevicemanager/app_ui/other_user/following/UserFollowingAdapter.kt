package com.example.flyingdevicemanager.app_ui.other_user.following

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingdevicemanager.databinding.UserItemBinding
import com.example.flyingdevicemanager.models.User
import com.squareup.picasso.Picasso

class UserFollowingAdapter : RecyclerView.Adapter<UserFollowingAdapter.MyViewHolder>() {
    
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
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener {
        fun showDevices(user: User)
    }
}