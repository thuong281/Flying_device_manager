package com.example.flyingdevicemanager.app_ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.databinding.FragmentDashboardBinding
import com.example.flyingdevicemanager.models.CountSummary
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(
    FragmentDashboardBinding::inflate
) {
    
    private val viewModel: DashBoardViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        loadData()
        observeData()
    }
    
    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getSummaryResponse.collectLatest {
                when (it.code()) {
                    200 -> it.body()?.let { it1 -> bindData(it1.data) }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
    }
    
    private fun bindData(data: CountSummary) {
        binding.totalDevice.text = data.devices.toString()
        binding.totalRegister.text = data.registers.toString()
        binding.countActiveDevice.text = data.activeDevices.toString()
        binding.activePercent.percent = (data.activeDevices!!.toFloat() / data.devices!!.toFloat())
    }
    
    private fun loadData() {
        viewModel.getSummary(getToken().toString())
    }
    
    private fun handleAction() {
        binding.btnRefresh.setOnClickListener {
            loadData()
        }
    }
    
}