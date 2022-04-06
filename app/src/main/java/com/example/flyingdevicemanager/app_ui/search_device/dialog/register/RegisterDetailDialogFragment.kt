package com.example.flyingdevicemanager.app_ui.search_device.dialog.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.app_ui.search_device.*
import com.example.flyingdevicemanager.app_ui.search_device.dialog.device.*
import com.example.flyingdevicemanager.databinding.FragmentRegisterDetailDialogBinding
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response


class RegisterDetailDialogFragment(private val registerId: String) :
    BaseDialogFragment<FragmentRegisterDetailDialogBinding>(
        FragmentRegisterDetailDialogBinding::inflate
    ), SearchAdapter.ClickListener {
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    val adapter: SearchAdapter by lazy { SearchAdapter(this) }
    
    var closeCallBack: (() -> Unit)? = null
    
    var currentRegister: Register? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        initRv()
        loadData()
        observeData()
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun observeData() {
        viewModel.registerDetailLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getRegisterByIdResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        it.body()?.let { it1 -> bindData(it1.data) }
                        currentRegister = it.body()?.data
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getListDevicesResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data as MutableList<Device2>
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
    }
    
    private fun bindData(data: Register) {
        binding.registerName.text = data.name
        binding.citizenCard.text = data.nationalId
        binding.phoneNumber.text = data.phoneNumber
    }
    
    private fun loadData() {
        viewModel.getRegisterById(getToken().toString(), registerId)
        viewModel.getListDevices(getToken().toString(), registerId)
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            closeCallBack!!.invoke()
            dismiss()
        }
        binding.update.setOnClickListener {
            if (currentRegister == null) return@setOnClickListener
            val dialog = UpdateRegisterDialogFragment(currentRegister!!)
            dialog.updateSuccessCallback = {
                loadData()
            }
            showDialog(dialog)
        }
    }
    
    override fun onDeviceClick(id: String) {
        val dialog = DeviceDetailDialogFragment(id)
        dialog.closeCallBack = {
            loadData()
        }
        showDialog(dialog)
    }
    
    override fun onRegisterClick(id: String) {}
    
}