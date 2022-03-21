package com.example.flyingdevicemanager.app_ui.other_user.follow_request

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.flyingdevicemanager.app_ui.device.DeviceAdapter
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.models.*
import com.squareup.picasso.Picasso

class UserRequestFollowAdapter(private val listener: ClickListener): RecyclerView.Adapter<UserRequestFollowAdapter.MyViewHolder>() {
    
    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()
    
    var items = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    
    class MyViewHolder(private val binding: UserRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        val approve = binding.approve
        val reject = binding.reject
        val swipe = binding.swipeLayout
        
        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            binding.userEmail.text = "Email: ${user.email}"
            binding.userName.text = "User name: ${user.name}"
            if (user.avatar != null && user.avatar != "") {
                Picasso.get().load(user.avatar).fit().centerCrop().into(binding.avatar)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            UserRequestItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(holder.swipe, items[position].id)
        holder.approve.setOnClickListener {
            listener.onApprove(items[position])
        }
        holder.reject.setOnClickListener {
            listener.onReject(items[position])
        }
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener {
        fun onApprove(user: User)
        fun onReject(user:User)
    }
}