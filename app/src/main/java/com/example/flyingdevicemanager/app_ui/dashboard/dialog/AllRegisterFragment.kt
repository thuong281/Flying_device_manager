package com.example.flyingdevicemanager.app_ui.dashboard.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.dashboard.DashBoardViewModel
import com.example.flyingdevicemanager.app_ui.search_device.*
import com.example.flyingdevicemanager.app_ui.search_device.dialog.device.DeviceDetailDialogFragment
import com.example.flyingdevicemanager.app_ui.search_device.dialog.register.RegisterDetailDialogFragment
import com.example.flyingdevicemanager.databinding.FragmentAllRegisterBinding
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class AllRegisterFragment : BaseDialogFragment<FragmentAllRegisterBinding>(
    FragmentAllRegisterBinding::inflate
),SearchAdapter.ClickListener {
    val adapter: SearchAdapter by lazy { SearchAdapter(this) }
    
    private val viewModel: DashBoardViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
        loadData()
        initRv()
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun loadData() {
        viewModel.getAllRegisters(getToken().toString())
    }
    
    private fun observeData() {
        viewModel.loadingAllRegisters.observe(viewLifecycleOwner) {
            binding.freshLayout.isRefreshing = it
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getAllRegisterResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data as List<Register>
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        binding.freshLayout.setOnRefreshListener {
            loadData()
        }
    }
    
    override fun onDeviceClick(id: String) {}
    
    override fun onRegisterClick(id: String) {
        val dialog = RegisterDetailDialogFragment(id)
        dialog.closeCallBack = {
            loadData()
        }
        showDialog(dialog)
    }
}