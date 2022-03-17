package com.example.flyingdevicemanager.app_ui.device

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.device.add_device.AddDeviceFragment
import com.example.flyingdevicemanager.databinding.FragmentDeviceBinding
import com.example.flyingdevicemanager.models.Device
import com.example.flyingdevicemanager.util.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class DeviceFragment : Fragment(), DeviceAdapter.ClickListener {
    
    lateinit var binding: FragmentDeviceBinding
    private val viewModel: DeviceViewModel by activityViewModels()
    
    private val adapter: DeviceAdapter by lazy { DeviceAdapter(this) }
    
    lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        binding = FragmentDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        handleAction()
        observeData()
        initRv()
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun loadData() {
        viewModel.getCurrentUserDevices(getToken().toString())
    }
    
    private fun observeData() {
        viewModel.loadingList.observe(viewLifecycleOwner) {
            binding.freshLayout.isRefreshing = it
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.deviceList.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data ?: ArrayList()
                    }
                    500 -> {
                        Toast.makeText(
                            context,
                            ErrorUtils.parseMessage(it as Response<Any>).errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.userKycResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        val fragment = AddDeviceFragment()
                        fragment.listener = {
                            loadData()
                        }
                        showDialog(fragment)
                    }
                    404, 202, 500 -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.deleteDeviceResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        loadData()
                    }
                    404, 500 -> {
                        errorMessage(it)
                    }
                }
            }
        }
        
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
    }
    
    private fun handleAction() {
        binding.btnAdd.setOnClickListener {
            viewModel.getUserKyc(getToken().toString())
        }
        
        binding.freshLayout.setOnRefreshListener {
            loadData()
        }
        
    }
    
    private fun showDialog(fragment: DialogFragment) {
        val fm: FragmentManager? = fragmentManager
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_NoActionBar)
        fragment.show(fm!!, "")
    }
    
    private fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }
    
    override fun showOnMap(device: Device) {
        Toast.makeText(context, "suc cac", Toast.LENGTH_SHORT).show()
    }
    
    override fun deleteDevice(device: Device) {
        viewModel.deleteDevice(getToken().toString(), device.deviceId.toString())
    }
    
    private fun errorMessage(response: Response<BaseResponse<Any>>) {
        Toast.makeText(
            context,
            ErrorUtils.parseMessage(response as Response<Any>).errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
    
}