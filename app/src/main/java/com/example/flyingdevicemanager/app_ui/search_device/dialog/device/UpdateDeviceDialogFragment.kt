package com.example.flyingdevicemanager.app_ui.search_device.dialog.device

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.search_device.SearchViewModel
import com.example.flyingdevicemanager.databinding.FragmentUpdateDeviceDialogBinding
import com.example.flyingdevicemanager.models.Device2
import com.example.flyingdevicemanager.util.TimeUtils
import com.example.flyingdevicemanager.util.base.BaseDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest

class UpdateDeviceDialogFragment(private val previousDevice: Device2) :
    BaseDialogFragment<FragmentUpdateDeviceDialogBinding>(
        FragmentUpdateDeviceDialogBinding::inflate
    ) {
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    var updateSuccessCallback: (() -> Unit)? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        observeData()
        handleAction()
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    
        binding.deviceBuyDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
        
            datePicker.show(parentFragmentManager, "date")
        
            datePicker.addOnPositiveButtonClickListener {
                binding.deviceBuyDate.text = TimeUtils.convertMillisToDate(it)
            }
        }
        
        binding.save.setOnClickListener {
            viewModel.updateDevice(
                getToken().toString(),
                previousDevice.id.toString(),
                binding.devicePlateText.text.toString(),
                binding.deviceColorText.text.toString(),
                binding.deviceManufacturerText.text.toString(),
                binding.deviceBuyDate.text.toString()
            )
        }
    }
    
    private fun observeData() {
        viewModel.updateDeviceLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        binding.devicePlateText.addTextChangedListener {
            checkActiveSaveButton()
        }
        binding.deviceColorText.addTextChangedListener {
            checkActiveSaveButton()
        }
        binding.deviceManufacturerText.addTextChangedListener {
            checkActiveSaveButton()
        }
        binding.deviceBuyDate.addTextChangedListener {
            checkActiveSaveButton()
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.updateDeviceResponse.collectLatest {
                when (it.code()) {
                    204 -> {
                        updateSuccessCallback!!.invoke()
                        dismiss()
                    }
                    else -> errorMessage(it)
                }
            }
        }
    }
    
    private fun compareData(): Boolean {
        return (binding.devicePlateText.text.toString() == previousDevice.devicePlate &&
                binding.deviceColorText.text.toString() == previousDevice.deviceColor &&
                binding.deviceManufacturerText.text.toString() == previousDevice.deviceManufacturer &&
                binding.deviceBuyDate.text.toString() == previousDevice.buyDate)
    }
    
    private fun checkActiveSaveButton() {
        if (compareData()) {
            binding.save.isEnabled = false
            binding.save.setTextColor(resources.getColor(R.color.grayLine))
        } else {
            binding.save.isEnabled = true
            binding.save.setTextColor(resources.getColor(R.color.white))
        }
    }
    
    fun bindData() {
        binding.devicePlateText.setText(previousDevice.devicePlate)
        binding.deviceColorText.setText(previousDevice.deviceColor)
        binding.deviceManufacturerText.setText(previousDevice.deviceManufacturer)
        binding.deviceBuyDate.text = previousDevice.buyDate
    }
    
}