package com.example.flyingdevicemanager.app_ui.other_user.following.user_device

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.map.MapsActivity
import com.example.flyingdevicemanager.app_ui.other_user.UserFollowViewModel
import com.example.flyingdevicemanager.databinding.FragmentUserDeviceBinding
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.*
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class UserDeviceFragment(private val user: User) : DialogFragment(), DeviceAdapter.ClickListener {
    
    lateinit var binding: FragmentUserDeviceBinding
    private val viewModel: UserFollowViewModel by activityViewModels()
    
    private val adapter: DeviceAdapter by lazy { DeviceAdapter(this) }
    
    lateinit var sharedPreferences: SharedPreferences
    
    lateinit var navController: NavController
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        binding = FragmentUserDeviceBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host)
        bindData()
        loadData()
        observeData()
        initRv()
        handleAction()
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        binding.btnRefresh.setOnClickListener {
            loadData()
        }
        binding.freshLayout.setOnRefreshListener {
            loadData()
        }
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun observeData() {
        viewModel.listUserDevicesLoading.observe(viewLifecycleOwner) {
            binding.freshLayout.isRefreshing = it
        }
    
        lifecycleScope.launchWhenCreated {
            viewModel.listUserDeviceResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        if (it.body()?.data?.size == 0) {
                            binding.noData.visibility = View.VISIBLE
                        } else {
                            binding.noData.visibility = View.GONE
                        }
                        adapter.items = it.body()?.data ?: ArrayList()
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
    }
    
    private fun loadData() {
        viewModel.getListUserDevices(getToken().toString(), user.id.toString())
    }
    
    @SuppressLint("SetTextI18n")
    private fun bindData() {
        binding.txtTitle.text = "${user.userName} 's devices"
    }
    
    private fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }
    
    private fun errorMessage(response: Response<BaseResponse<Any>>) {
        Toast.makeText(
            context,
            ErrorUtils.parseMessage(response as Response<Any>).errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
    
    override fun showOnMap(device: Device) {
        val intent = Intent(context, MapsActivity::class.java).apply {
            putExtra("deviceId", device.deviceId)
        }
        startActivity(intent)
    }
    
}