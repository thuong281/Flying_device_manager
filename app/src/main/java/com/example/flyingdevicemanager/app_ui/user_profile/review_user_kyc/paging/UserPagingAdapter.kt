package com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc.paging

import android.annotation.SuppressLint
import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.UserItemBinding
import com.example.flyingdevicemanager.models.User
import com.squareup.picasso.Picasso

class UserPagingAdapter(private val listener: ClickListener) : PagingDataAdapter<User, UserPagingAdapter.MyViewHolder>(itemDiff) {
    
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
    
    companion object {
        val itemDiff = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }
            
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
            
        }
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
        holder.item.setOnClickListener {
            listener.showUserKycDetail(getItem(position)!!, position)
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
    
    interface ClickListener {
        fun showUserKycDetail(user: User, position: Int)
    }
    
}