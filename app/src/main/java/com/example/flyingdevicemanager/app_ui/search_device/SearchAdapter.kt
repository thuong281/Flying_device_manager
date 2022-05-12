package com.example.flyingdevicemanager.app_ui.search_device

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.models.*

class SearchAdapter(private val listener: ClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    class DeviceViewHolder(private val binding: DeviceItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.item
        
        @SuppressLint("SetTextI18n")
        fun bind(device: Device2) {
            binding.name.text = "Device Plate: ${device.devicePlate}"
            binding.deviceId.text = "ID: ${device.id}"
            if (device.isActive == true) {
                binding.activeStatus.setImageResource(R.drawable.ic_online)
            } else {
                binding.activeStatus.setImageResource(R.drawable.ic_offline)
            }
        }
    }
    
    class RegisterViewHolder(private val binding: RegisterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.item
        
        @SuppressLint("SetTextI18n")
        fun bind(register: Register) {
            binding.registerName.text = "${register.name}  - ${register.listDeviceId?.size} device"
            binding.registerPhoneNumber.text = "Phone number: ${register.phoneNumber}"
            if (register.isOrganization == 0) {
                binding.avatar.setImageResource(R.drawable.ic_baseline_person_24)
                binding.registerNationalId.text = "Citizen ID: ${register.registerId}"
            } else {
                binding.avatar.setImageResource(R.drawable.ic_baseline_home_work_24)
                binding.registerNationalId.text = "Organization ID: ${register.registerId}"
                binding.imageContainer.radius = 0f
            }
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
                val register = items[position] as Register
                holder.item.setOnClickListener {
                    listener.onRegisterClick(register.id.toString())
                }
            }
        }
    }
    
    override fun getItemCount() = items.size
    
    interface ClickListener {
        fun onDeviceClick(id: String)
        fun onRegisterClick(id: String)
    }
}