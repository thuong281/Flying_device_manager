package com.example.flyingdevicemanager.app_ui.search_device.dialog.device

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.map.MapsActivity
import com.example.flyingdevicemanager.app_ui.search_device.*
import com.example.flyingdevicemanager.app_ui.search_device.dialog.register.RegisterDetailDialogFragment
import com.example.flyingdevicemanager.databinding.FragmentDeviceDetailDialogBinding
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.TimeUtils
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class DeviceDetailDialogFragment(
    private val deviceId: String
) :
    BaseDialogFragment<FragmentDeviceDetailDialogBinding>(
        FragmentDeviceDetailDialogBinding::inflate
    ) {
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    var closeCallBack: (() -> Unit)? = null
    
    var currentDevice: Device2? = null
    
    var currentRegister: Register? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        loadData()
        observeData()
    }
    
    private fun observeData() {
        viewModel.deviceDetailLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getDeviceByIdResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        bindData(it.body()!!.data)
                        currentDevice = it.body()!!.data
                        viewModel.getRegisterByNationalId(
                            getToken().toString(),
                            currentDevice!!.registerNationalId!!
                        )
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getRegisterByNationalIdResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        currentRegister = it.body()?.data
                        binding.registerName.text = currentRegister?.name
                        binding.citizenCard.text = currentRegister?.nationalId
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.deleteDeviceResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        Toast.makeText(context, it.body()?.msg, Toast.LENGTH_SHORT).show()
                        closeCallBack?.invoke()
                        dismiss()
                    }
                    else -> {
                        errorMessage(it)
                    }
                }
            }
        }
    }
    
    private fun bindData(data: Device2) {
        binding.deviceColor.text = data.deviceColor
        binding.deviceManufacturer.text = data.deviceManufacturer
        binding.txtTitle.text = data.devicePlate
        binding.buyDate.text = data.buyDate
        binding.createdDate.text = data.createdDate?.let { TimeUtils.convertMillisToDateDetail(it) }
        binding.createdUser.text = data.createdUser
        if (data.createdDate != data.lastUpdated) {
            binding.lastUpdate.text =
                data.lastUpdated?.let { TimeUtils.convertMillisToDateDetail(it) }
            binding.updateUser.text = data.updatedUser
        }
        if (data.isActive == true) {
            binding.online.visibility = View.VISIBLE
            binding.offline.visibility = View.GONE
        } else {
            binding.online.visibility = View.GONE
            binding.offline.visibility = View.VISIBLE
        }
        
    }
    
    private fun loadData() {
        viewModel.getDeviceById(getToken().toString(), deviceId)
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            closeCallBack!!.invoke()
            dismiss()
        }
        
        binding.btnRefresh.setOnClickListener {
            loadData()
        }
        
//        binding.register.setOnClickListener {
//            if (currentRegister == null) return@setOnClickListener
//            val dialog = RegisterDetailDialogFragment(currentRegister?.id.toString(), true)
//            dialog.closeCallBack = {}
//            showDialog(dialog)
//        }
        
        binding.update.setOnClickListener {
            if (currentDevice == null) return@setOnClickListener
            val dialog = UpdateDeviceDialogFragment(currentDevice!!)
            showDialog(dialog)
        }
        
        binding.showOnMap.setOnClickListener {
            if (currentDevice == null) return@setOnClickListener
            if (currentDevice!!.isActive == true) {
                val intent = Intent(context, MapsActivity::class.java).apply {
                    putExtra("deviceId", deviceId)
                }
                startActivity(intent)
            } else {
                Toast.makeText(context, "GPS is not active", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.delete.setOnClickListener {
            
            val confirmDialog = BaseConfirmDialog(
                context!!,
                R.style.Theme_Confirm_Dialog
            )
            confirmDialog.setTitle("Warning")
                .setDialogMessage("Do you really want to remove this device")
                .setDialogConfirmText("Confirm")
                .setDialogCloseText("Close")
                .show()
            
            confirmDialog.setOnConfirmDialogListener {
                viewModel.deleteDevice(getToken().toString(), deviceId)
                confirmDialog.dismiss()
            }
        }
    }
    
}