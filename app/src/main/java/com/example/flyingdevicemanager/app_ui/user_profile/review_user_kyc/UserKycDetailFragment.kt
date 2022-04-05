package com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.app_ui.user_profile.UserViewModel
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.models.*
import com.example.flyingdevicemanager.util.*
import com.example.flyingdevicemanager.util.base.BaseResponse
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class UserKycDetailFragment(private val user: User, private val position: Int) : DialogFragment() {
    
    lateinit var binding: FragmentUserKycDetailBinding
    private val viewModel: UserViewModel by activityViewModels()
    
    var reviewFinishCallback: ((position: Int) -> Unit)? = null
    
    lateinit var sharedPreferences: SharedPreferences
    
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
        binding = FragmentUserKycDetailBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        bindData(user)
        loadData()
        observeData()
    }
    
    private fun observeData() {
        viewModel.userKycLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.getUserKycResponse.collectLatest {
                when (it.code()) {
                    200 -> dismiss()
                    202 -> {
                        bindKycData(it.body()?.data)
                    }
                    else -> errorMessage(it as Response<BaseResponse<Any>>)
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.approveUserKycResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        reviewFinishCallback?.invoke(position)
                        dismiss()
                        Toast.makeText(context, "Approve successfully",Toast.LENGTH_SHORT).show()
                    }
                    else -> errorMessage(it)
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.rejectUserKycResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        reviewFinishCallback?.invoke(position)
                        dismiss()
                        Toast.makeText(context, "Reject successfully",Toast.LENGTH_SHORT).show()
                    }
                    else -> errorMessage(it)
                }
            }
        }
    }
    
    private fun loadData() {
        viewModel.getUserKyc(getToken().toString(), user.id.toString())
    }
    
    private fun bindData(user: User) {
        if (user.avatar != null && user.avatar != "") {
            Picasso.get().load(user.avatar).fit().centerCrop().into(binding.avatar)
        }
        binding.userEmail.text = user.email
        binding.userName.text = user.name
    }
    
    private fun bindKycData(userKyc: UserKyc?) {
        if (userKyc!!.frontImage != null && userKyc.frontImage != "") {
            Picasso.get().load(userKyc.frontImage).fit().centerCrop().into(binding.imageFront)
        }
        if (userKyc.backImage != null && userKyc.backImage != "") {
            Picasso.get().load(userKyc.backImage).fit().centerCrop().into(binding.imageBack)
        }
        binding.idNumber.text = userKyc.nationalId
    }
    
    private fun handleAction() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        binding.approve.setOnClickListener {
            viewModel.approveUserKyc(getToken().toString(), user.id.toString())
        }
        binding.reject.setOnClickListener {
            viewModel.rejectUserKyc(getToken().toString(), user.id.toString())
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