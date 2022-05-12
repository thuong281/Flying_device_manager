package com.example.flyingdevicemanager.app_ui.add_device.add

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.add_device.AddDeviceViewModel
import com.example.flyingdevicemanager.databinding.FragmentAddBinding
import com.example.flyingdevicemanager.util.TimeUtils
import com.example.flyingdevicemanager.util.base.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest

class AddFragment : BaseFragment<FragmentAddBinding>(
    FragmentAddBinding::inflate
) {
    private val viewModel: AddDeviceViewModel by activityViewModels()
    
    var callback: Callback? = null
    
    private val registerType = arrayOf("Person", "Organization")
    
    private var isOrganization = 0;
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
        initSpinner()
    }
    
    private fun initSpinner() {
        val adapterSpinner = ArrayAdapter(context!!, R.layout.list_item, registerType)
        (binding.menu.editText as AutoCompleteTextView).setAdapter(adapterSpinner)
        (binding.menu.editText as AutoCompleteTextView).onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                isOrganization = p2
                changeRegisterType(isOrganization)
            }
        (binding.menu.editText as AutoCompleteTextView).setText(registerType[0], false)
    }
    
    private fun changeRegisterType(isOrganization: Int) {
        if (isOrganization == 1) {
            binding.registerName.hint = "Organization name"
            binding.registerNationalId.hint = "Organization id"
            binding.registerPhone.hint = "Organization phone number"
        } else {
            binding.registerName.hint = "Register name"
            binding.registerNationalId.hint = "Register national id"
            binding.registerPhone.hint = "Register phone number"
        }
    }
    
    private fun handleAction() {
        binding.datePicker.setOnClickListener {
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
        
        binding.submitAdd.setOnClickListener {
            viewModel.addDevice(
                getToken().toString(),
                isOrganization,
                binding.registerNameText.text.toString(),
                binding.registerNationalIdText.text.toString(),
                binding.registerPhoneText.text.toString(),
                binding.devicePlateText.text.toString(),
                binding.deviceColorText.text.toString(),
                binding.deviceManufacturerText.text.toString(),
                binding.deviceBuyDate.text.toString()
            )
        }
    }
    
    private fun observeData() {
        viewModel.loadingAddDevice.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.addDeviceResponse.collectLatest {
                when (it.code()) {
                    201 -> {
                        Toast.makeText(context, "Add device Successfully", Toast.LENGTH_SHORT)
                            .show()
                        callback?.onAddDeviceSuccess()
                        clearTextInput()
                    }
                    else -> {
                        errorMessage(it)
                    }
                }
            }
        }
    }
    
    private fun clearTextInput() {
        binding.registerNameText.text!!.clear()
        binding.registerNationalIdText.text!!.clear()
        binding.registerPhoneText.text!!.clear()
        binding.devicePlateText.text!!.clear()
        binding.deviceColorText.text!!.clear()
        binding.deviceManufacturerText.text!!.clear()
        binding.deviceBuyDate.text = "Select device buy date"
    }
    
    interface Callback {
        fun onAddDeviceSuccess()
    }
}