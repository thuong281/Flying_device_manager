package com.example.flyingdevicemanager.app_ui.search_device.dialog.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.search_device.*
import com.example.flyingdevicemanager.app_ui.search_device.dialog.device.*
import com.example.flyingdevicemanager.databinding.FragmentRegisterDetailDialogBinding
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response


class RegisterDetailDialogFragment(private val registerId: String, private val isOpenFromDevice: Boolean = false) :
    BaseDialogFragment<FragmentRegisterDetailDialogBinding>(
        FragmentRegisterDetailDialogBinding::inflate
    ), SearchAdapter.ClickListener {
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    val adapter: SearchAdapter by lazy { SearchAdapter(this) }
    
    var closeCallBack: (() -> Unit) = {}
    
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
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
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
                        val data = it.body()?.data as MutableList<Device2>
                        if (data.size > 0) {
                            adapter.items = it.body()?.data as MutableList<Device2>
                            binding.noData.visibility = View.GONE
                        } else {
                            binding.noData.visibility = View.VISIBLE
                        }
                        
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.deleteRegisterResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                        closeCallBack?.invoke()
                    }
                    else -> errorMessage(it)
                }
            }
        }
    }
    
    @SuppressLint("SetTextI18n")
    private fun bindData(data: Register) {
        if (data.isOrganization == 1) {
            binding.nameTitle.text = "Organization name: "
            binding.idTitle.text = "Organization ID: "
            binding.nameIcon.setImageResource(R.drawable.ic_baseline_home_work_24)
        } else {
            binding.nameTitle.text = "Register name: "
            binding.idTitle.text = "Citizen card: "
            binding.nameIcon.setImageResource(R.drawable.ic_baseline_person_24)
        }
        binding.registerName.text = data.name
        binding.citizenCard.text = data.registerId
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
        
        binding.btnRefresh.setOnClickListener {
            loadData()
        }
        
        binding.update.setOnClickListener {
            if (currentRegister == null) return@setOnClickListener
            val dialog = UpdateRegisterDialogFragment(currentRegister!!)
            dialog.updateSuccessCallback = {
                loadData()
            }
            showDialog(dialog)
        }
        
        binding.delete.setOnClickListener {
            val confirmDialog = BaseConfirmDialog(
                context!!,
                R.style.Theme_Confirm_Dialog
            )
            confirmDialog.setTitle("Warning")
                .setDialogMessage("Do you really want to remove this register?")
                .setDialogConfirmText("Confirm")
                .setDialogCloseText("Close")
                .show()
            
            confirmDialog.setOnConfirmDialogListener {
                viewModel.deleteRegister(getToken().toString(), registerId)
                confirmDialog.dismiss()
            }
        }
    }
    
    override fun onDeviceClick(id: String) {
        if (isOpenFromDevice) return
        val dialog = DeviceDetailDialogFragment(id)
        dialog.closeCallBack = {
            loadData()
        }
        showDialog(dialog)
    }
    
    override fun onRegisterClick(id: String) {}
    
}