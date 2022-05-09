package com.example.flyingdevicemanager.app_ui.user_profile.create_user

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.user_profile.UserViewModel
import com.example.flyingdevicemanager.databinding.FragmentCreateUserBinding
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class CreateUserFragment : BaseDialogFragment<FragmentCreateUserBinding>(
    FragmentCreateUserBinding::inflate
) {
    
    private val viewModel: UserViewModel by activityViewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 1).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.72).toInt()
            dialog.window!!.setLayout(width, height)
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
    }
    
    private fun observeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.createUserResponse.collectLatest {
                when (it.code()) {
                    201 -> {
                        Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT)
                            .show()
                        dismiss()
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
        
        binding.register.setOnClickListener {
            val fullName = binding.fullNameText.text.toString()
            val userName = binding.userNameText.text.toString()
            val password = binding.passwordText.text.toString()
            if (binding.checkBox.isChecked) {
                viewModel.createAdmin(userName, fullName, password)
            } else {
                viewModel.createUser(userName, fullName, password)
            }
            
        }
    }
    
}