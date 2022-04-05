package com.example.flyingdevicemanager.app_ui.other_user.following.user_device

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.models.Device

class DeviceAdapter(private val listener: ClickListener) :
    RecyclerView.Adapter<DeviceAdapter.MyViewHolder>() {
    
    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()
    
    var items = listOf<Device>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    
    class MyViewHolder(private val binding: DeviceItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        
        val item = binding.item
        
        @SuppressLint("SetTextI18n")
        fun bind(device: Device) {
            binding.name.text = "Name: ${device.deviceName}"
            binding.deviceId.text = "Id: ${device.deviceId}"
            if (device.isOnline == 1) {
                binding.activeStatus.setImageResource(R.drawable.ic_online)
            } else {
                binding.activeStatus.setImageResource(R.drawable.ic_offline)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DeviceItem2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
        holder.item.setOnClickListener {
            if (items[position].isOnline == 0) return@setOnClickListener
            listener.showOnMap(device = items[position])
        }
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener {
        fun showOnMap(device: Device)
    }
    
    
}