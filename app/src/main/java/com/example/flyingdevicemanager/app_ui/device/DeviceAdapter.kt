package com.example.flyingdevicemanager.app_ui.device

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.DeviceItemBinding
import com.example.flyingdevicemanager.models.Device

class DeviceAdapter(private val listener: ClickListener) :
    RecyclerView.Adapter<DeviceAdapter.MyViewHolder>() {
    
    var items = listOf<Device>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    
    class MyViewHolder(private val binding: DeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        val item = binding.item
        
        fun bind(device: Device) {
            binding.name.text = device.deviceName
            binding.deviceId.text = device.deviceId
            if (device.isOnline == 1) {
                binding.activeStatus.setImageResource(R.drawable.ic_online)
            } else {
                binding.activeStatus.setImageResource(R.drawable.ic_offline)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DeviceItemBinding.inflate(
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