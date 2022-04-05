package com.example.flyingdevicemanager.app_ui.search_device.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.app_ui.search_device.SearchViewModel
import com.example.flyingdevicemanager.databinding.FragmentDeviceDetailDialogBinding
import com.example.flyingdevicemanager.models.Device2
import com.example.flyingdevicemanager.util.TimeUtils
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class DeviceDetailDialogFragment(private val deviceId: String) :
    BaseDialogFragment<FragmentDeviceDetailDialogBinding>(
        FragmentDeviceDetailDialogBinding::inflate
    ) {
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    var closeCallBack: (() -> Unit)? = null
    
    var currentDevice: Device2? = null
    
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
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
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
            binding.lastUpdate.text = data.lastUpdated?.let { TimeUtils.convertMillisToDateDetail(it) }
            binding.updateUser.text = data.updatedUser
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
        binding.update.setOnClickListener {
            if (currentDevice == null) return@setOnClickListener
            val dialog = UpdateDeviceDialogFragment(currentDevice!!)
            dialog.updateSuccessCallback  = {
                loadData()
            }
            showDialog(dialog)
        }
    }
    
}