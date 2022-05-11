package com.example.flyingdevicemanager.app_ui.user_profile.change_password

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.user_profile.UserViewModel
import com.example.flyingdevicemanager.databinding.FragmentChangePasswordBinding
import com.example.flyingdevicemanager.util.base.BaseDialogFragment
import kotlinx.coroutines.flow.collectLatest

class ChangePasswordFragment : BaseDialogFragment<FragmentChangePasswordBinding>(
    FragmentChangePasswordBinding::inflate
) {
    
    private val viewModel: UserViewModel by activityViewModels()
    
    var updateSuccessCallback: (() -> Unit)? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 1).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.7).toInt()
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
            viewModel.updatePasswordResponse.collectLatest {
                when (it.code()) {
                    201 -> {
                        Toast.makeText(context, "Password changed", Toast.LENGTH_SHORT).show()
                        updateSuccessCallback?.invoke()
                    }
                    else -> errorMessage(it)
                }
            }
        }
    }
    
    private fun handleAction() {
        binding.changePassword.setOnClickListener {
            if (binding.newPassword.error != null || binding.repeatNewPassword.error != null || binding.repeatNewPasswordText.text.isNullOrEmpty()) {
                Toast.makeText(context, "Please fill all the field correctly!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.updatePassword(
                getToken().toString(),
                binding.oldPasswordText.text.toString(),
                binding.newPasswordText.text.toString()
            )
        }
        
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        
        binding.newPasswordText.addTextChangedListener {
            if (binding.newPasswordText.text.toString() == binding.oldPasswordText.text.toString()) {
                binding.newPassword.error = "New password is the same as old password"
            } else {
                binding.newPassword.error = null
            }
        }
    
        binding.repeatNewPasswordText.addTextChangedListener {
            if (binding.newPasswordText.text.toString() != binding.repeatNewPasswordText.text.toString()) {
                binding.repeatNewPassword.error = "New password does not match"
            } else {
                binding.repeatNewPassword.error = null
            }
        }
    }
    
    
}