package com.example.flyingdevicemanager.app_ui.search_device.dialog.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.search_device.SearchViewModel
import com.example.flyingdevicemanager.databinding.FragmentUpdateRegisterDialogBinding
import com.example.flyingdevicemanager.models.Register
import com.example.flyingdevicemanager.util.base.BaseDialogFragment
import kotlinx.coroutines.flow.collectLatest

class UpdateRegisterDialogFragment(private val previousRegister: Register) : BaseDialogFragment<FragmentUpdateRegisterDialogBinding>(
    FragmentUpdateRegisterDialogBinding:: inflate
) {
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    var updateSuccessCallback: (() -> Unit)? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        observeData()
        handleAction()
    }
    
    private fun observeData() {
        viewModel.updateRegisterLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility  = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        binding.nameText.addTextChangedListener {
            checkActiveSaveButton()
        }
        binding.citizenCardText.addTextChangedListener {
            checkActiveSaveButton()
        }
        binding.phoneNumberText.addTextChangedListener {
            checkActiveSaveButton()
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.updateRegisterResponse.collectLatest {
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
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        binding.save.setOnClickListener {
            viewModel.updateRegister(
                getToken().toString(),
                previousRegister.id.toString(),
                binding.nameText.text.toString(),
                binding.citizenCardText.text.toString(),
                binding.phoneNumberText.text.toString()
            )
        }
    }
    
    private fun compareData(): Boolean {
        return (binding.nameText.text.toString() == previousRegister.name &&
                binding.citizenCardText.text.toString() == previousRegister.registerId &&
                binding.phoneNumberText.text.toString() == previousRegister.phoneNumber)
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
    
    private fun bindData() {
        binding.nameText.setText(previousRegister.name)
        binding.citizenCardText.setText(previousRegister.registerId)
        binding.phoneNumberText.setText(previousRegister.phoneNumber)
    }
    
}