package com.example.flyingdevicemanager.app_ui.add_device.history

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.flyingdevicemanager.app_ui.add_device.AddDeviceViewModel
import com.example.flyingdevicemanager.app_ui.add_device.history.paging.DevicePagingAdapter
import com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc.paging.UserPagingAdapter
import com.example.flyingdevicemanager.databinding.FragmentHistoryBinding
import com.example.flyingdevicemanager.models.User
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(
    FragmentHistoryBinding::inflate
), UserPagingAdapter.ClickListener {
    
    private val viewModel: AddDeviceViewModel by activityViewModels()
    
    private val adapter: DevicePagingAdapter by lazy { DevicePagingAdapter() }
    
    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        loadData()
        observeData()
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun loadData() {
        viewModel.getAddDeviceHistoryList(getToken().toString())
    }
    
    private fun observeData() {
        viewModel.loadingAddDeviceHistory.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        viewModel.pagingAddDeviceHistory.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
            binding.rv.smoothScrollToPosition(0)
        }
        
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> binding.loading.visibility = View.VISIBLE
                    is LoadState.Error -> {
                        binding.loading.visibility = View.GONE
                        Toast.makeText(
                            context,
                            (loadStates.refresh as LoadState.Error).error.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> binding.loading.visibility = View.GONE
                }
            }
        }
    }
    
    override fun showUserKycDetail(user: User, position: Int) {
        TODO("Not yet implemented")
    }
    
}