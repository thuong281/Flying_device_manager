package com.example.flyingdevicemanager.app_ui.search_device

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.app_ui.search_device.dialog.device.DeviceDetailDialogFragment
import com.example.flyingdevicemanager.app_ui.search_device.dialog.register.RegisterDetailDialogFragment
import com.example.flyingdevicemanager.databinding.FragmentSearchBinding
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class SearchFragment : BaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
), SearchAdapter.ClickListener {
    
    enum class SearchType {
        DEVICE, REGISTER
    }
    
    private var searchType: SearchType = SearchType.DEVICE
    
    private val viewModel: SearchViewModel by activityViewModels()
    
    private val adapter: SearchAdapter by lazy { SearchAdapter(this) }
    
    private var searchPhrase: String = ""
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
        initRv()
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
            viewModel.deviceListResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data as MutableList<Device2>
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.registerListResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data as MutableList<Register>
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun handleAction() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.search.clearFocus()
                if (p0!!.length < 2) {
                    Toast.makeText(context, "Enter more than 1 characters", Toast.LENGTH_SHORT)
                        .show()
                    return false
                }
                searchPhrase = p0
                when (searchType) {
                    SearchType.DEVICE -> viewModel.searchDevice(getToken().toString(), p0)
                    SearchType.REGISTER -> viewModel.searchRegister(getToken().toString(), p0)
                }
                return false
            }
            
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        
        binding.radioDevice.setOnClickListener {
            searchType = SearchType.DEVICE
            binding.search.queryHint = "Search device"
        }
        
        binding.radioRegister.setOnClickListener {
            searchType = SearchType.REGISTER
            binding.search.queryHint = "Search register"
        }
    }
    
    override fun onDeviceClick(id: String) {
        val dialog = DeviceDetailDialogFragment(id)
        dialog.closeCallBack = {
            when (searchType) {
                SearchType.DEVICE -> viewModel.searchDevice(getToken().toString(), searchPhrase)
                SearchType.REGISTER -> viewModel.searchRegister(getToken().toString(), searchPhrase)
            }
        }
        showDialog(dialog)
    }
    
    override fun onRegisterClick(id: String) {
        val dialog = RegisterDetailDialogFragment(id)
        dialog.closeCallBack = {
            when (searchType) {
                SearchType.DEVICE -> viewModel.searchDevice(getToken().toString(), searchPhrase)
                SearchType.REGISTER -> viewModel.searchRegister(getToken().toString(), searchPhrase)
            }
        }
        showDialog(dialog)
    }
}