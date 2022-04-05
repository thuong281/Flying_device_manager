package com.example.flyingdevicemanager.app_ui.search_device

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.models.*

class SearchAdapter(private val listener: ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    class DeviceViewHolder(private val binding: DeviceItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.item
        
        fun bind(device: Device2) {
            binding.name.text = device.devicePlate
            binding.deviceId.text = device.id
            binding.activeStatus.visibility = View.GONE
        }
    }
    
    class RegisterViewHolder(private val binding: RegisterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(register: Register) {
            binding.registerName.text = "${register.name}  - ${register.deviceCount} device"
            binding.registerNationalId.text = "National ID: ${register.nationalId}"
            binding.registerPhoneNumber.text = "Phone number: ${register.phoneNumber}"
        }
    }
    
    var items = listOf<Any>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Device2 -> R.layout.device_item_2
            is Register -> R.layout.register_item
            else -> throw IllegalArgumentException("Invalid ViewType")
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.device_item_2 -> {
                DeviceViewHolder(
                    DeviceItem2Binding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.register_item -> {
                RegisterViewHolder(
                    RegisterItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid ViewHolder")
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DeviceViewHolder -> {
                holder.bind(items[position] as Device2)
                val device = items[position] as Device2
                holder.item.setOnClickListener {
                    listener.onDeviceClick(device.id.toString())
                }
            }
            is RegisterViewHolder -> {
                holder.bind(items[position] as Register)
            }
        }
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener{
        fun onDeviceClick(id: String)
        fun onRegisterClick(register: Register)
    }
}