package com.example.flyingdevicemanager.app_ui.add_device.history.paging

import android.annotation.SuppressLint
import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import com.example.flyingdevicemanager.databinding.AddDeviceHistoryItemBinding
import com.example.flyingdevicemanager.models.Device2
import com.example.flyingdevicemanager.util.TimeUtils

class DevicePagingAdapter : PagingDataAdapter<Device2, DevicePagingAdapter.MyViewHolder>(itemDiff) {
    
    class MyViewHolder(private val binding: AddDeviceHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(device: Device2) {
            binding.registerName.text = "Register Id: ${device.registerNationalId}"
            binding.devicePlate.text = "Plate number: ${device.devicePlate}"
            binding.addDate.text =
                "Register date: ${TimeUtils.convertMillisToDate(device.createdDate!!)}"
        }
    }
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AddDeviceHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    companion object {
        val itemDiff = object : DiffUtil.ItemCallback<Device2>() {
            override fun areItemsTheSame(oldItem: Device2, newItem: Device2): Boolean {
                return false
            }
            
            override fun areContentsTheSame(oldItem: Device2, newItem: Device2): Boolean {
                return oldItem == newItem
            }
            
        }
    }
}