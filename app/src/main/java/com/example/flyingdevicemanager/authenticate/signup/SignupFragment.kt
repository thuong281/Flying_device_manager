package com.example.flyingdevicemanager.authenticate.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.*
import androidx.navigation.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.authenticate.AuthViewModel
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.util.ErrorUtils
import retrofit2.Response

class SignupFragment : Fragment() {
    
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: AuthViewModel by activityViewModels()
    
    lateinit var navController: NavController
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        handleAction()
        observeData()
    }
    
    private fun handleAction() {
        binding.login.setOnClickListener {
            navController.navigate(R.id.action_signupFragment_to_loginFragment)
        }
//        binding.register.setOnClickListener {
//            val email = binding.emailText.text.toString()
//            val userName = binding.userNameText.toString()
//            val password = binding.passwordText.text.toString()
//            viewModel.signup(email, userName, password)
//        }
    }
    
    private fun observeData() {
        viewModel.signupResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Toast.makeText(context, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_signupFragment_to_loginFragment)
            } else {
                Toast.makeText(
                    context,
                    ErrorUtils.parseMessage(it as Response<Any>).errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
}