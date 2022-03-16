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
import kotlinx.coroutines.flow.collectLatest

class DeviceFragment : Fragment(), DeviceAdapter.ClickListener {
    
    lateinit var binding: FragmentDeviceBinding
    private val viewModel: DeviceViewModel by activityViewModels()
    
    val adapter: DeviceAdapter by lazy { DeviceAdapter(this) }
    
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
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.deviceList.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data ?: ArrayList()
                    }
                    500 -> {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    
    private fun handleAction() {
        binding.btnAdd.setOnClickListener {
            showDialog(AddDeviceFragment())
        }
        binding.btnReload.setOnClickListener {
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
    
}