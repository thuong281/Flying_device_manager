package com.example.flyingdevicemanager.authenticate.login

import android.annotation.SuppressLint
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.navigation.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.authenticate.AuthViewModel
import com.example.flyingdevicemanager.databinding.FragmentLoginBinding
import com.example.flyingdevicemanager.util.ErrorUtils
import retrofit2.Response

class LoginFragment : Fragment() {
    
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModels()
    
    lateinit var navController: NavController
    
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        
        handleAction()
        observeData()
    }
    
    private fun observeData() {
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                storeToken(it.body()?.data?.token.toString())
                navController.navigate(R.id.action_loginFragment_to_mainActivity)
            } else {
                Toast.makeText(
                    context,
                    ErrorUtils.parseMessage(it as Response<Any>).errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun handleAction() {
        binding.login.setOnClickListener {
            val email = binding.userNameText.text.toString()
            val password = binding.passwordText.text.toString()
            viewModel.login(email = email, password = password)
        }
        binding.register.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
    
    private fun storeToken(token: String) {
        editor.putString("token", token)
        editor.commit()
    }
    
}