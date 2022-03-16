package com.example.flyingdevicemanager.app_ui.device.add_device

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.device.DeviceViewModel
import com.example.flyingdevicemanager.databinding.*


class AddDeviceFragment : DialogFragment() {
    
    lateinit var binding: FragmentAddDeviceBinding
    private val viewModel: DeviceViewModel by activityViewModels()
    
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
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.70).toInt()
            dialog.window!!.setLayout(width, height)
        }
    }
    
    
}