package com.example.flyingdevicemanager.app_ui.device.add_device

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.device.DeviceViewModel
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.util.*
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response


class AddDeviceFragment : DialogFragment() {
    
    lateinit var binding: FragmentAddDeviceBinding
    private val viewModel: DeviceViewModel by activityViewModels()
    
    var listener: (()->Unit)? = null
    
    lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
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
        binding = FragmentAddDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
    }
    
    private fun observeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.addDeviceResponse.collectLatest {
                when (it.code()) {
                    201 -> {
                        Toast.makeText(context, "Add device success", Toast.LENGTH_SHORT).show()
                        listener?.invoke()
                        dismiss()
                    }
                    401, 500 -> {
                        errorMessage(it)
                    }
                }
            }
        }
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        
        binding.add.setOnClickListener {
            viewModel.addDevice(getToken().toString(), binding.deviceNameText.text.toString())
        }
    }
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
            dialog.window!!.setLayout(width, height)
        }
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
}