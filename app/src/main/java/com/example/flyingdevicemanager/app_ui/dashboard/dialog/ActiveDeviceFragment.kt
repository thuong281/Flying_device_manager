package com.example.flyingdevicemanager.app_ui.dashboard.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.example.flyingdevicemanager.app_ui.dashboard.DashBoardViewModel
import com.example.flyingdevicemanager.app_ui.search_device.SearchAdapter
import com.example.flyingdevicemanager.app_ui.search_device.dialog.device.DeviceDetailDialogFragment
import com.example.flyingdevicemanager.databinding.FragmentActiveDeviceBinding
import com.example.flyingdevicemanager.models.Device2
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response


class ActiveDeviceFragment : BaseDialogFragment<FragmentActiveDeviceBinding>(
    FragmentActiveDeviceBinding::inflate
), SearchAdapter.ClickListener {
    val adapter: SearchAdapter by lazy { SearchAdapter(this) }
    
    var callback: (() -> Unit)? = null
    
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
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }
    
    private fun loadData() {
        viewModel.getActiveDevices(getToken().toString())
    }
    
    private fun observeData() {
        viewModel.loadingActiveDevice.observe(viewLifecycleOwner) {
            binding.freshLayout.isRefreshing = it
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getActiveDeviceResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data as List<Device2>
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            callback?.invoke()
            dismiss()
        }
        binding.freshLayout.setOnRefreshListener {
            loadData()
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