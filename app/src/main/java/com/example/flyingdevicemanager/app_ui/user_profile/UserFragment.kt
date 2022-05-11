package com.example.flyingdevicemanager.app_ui.user_profile

import android.content.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.user_profile.change_password.ChangePasswordFragment
import com.example.flyingdevicemanager.app_ui.user_profile.create_user.CreateUserFragment
import com.example.flyingdevicemanager.app_ui.user_profile.dialog.AvatarFragment
import com.example.flyingdevicemanager.app_ui.user_profile.user_kyc.UserKycFragment
import com.example.flyingdevicemanager.databinding.*
import com.example.flyingdevicemanager.models.User
import com.example.flyingdevicemanager.util.base.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response


class UserFragment : BaseFragment<FragmentUserBinding>(
    FragmentUserBinding::inflate
) {
    
//    lateinit var binding: FragmentUserBinding
    private val viewModel: UserViewModel by activityViewModels()
    
    lateinit var navController: NavController
    
//    lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    
    lateinit var user: User
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    
//    @SuppressLint("CommitPrefEdits")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        sharedPreferences = activity!!.getSharedPreferences(
//            "SHARED_PREF",
//            Context.MODE_PRIVATE
//        )
//        editor = sharedPreferences.edit()
//        // Inflate the layout for this fragment
//        binding = FragmentUserBinding.inflate(inflater, container, false)
//        return binding.root
//    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        handleAction()
        loadData()
        observeData()
        editor = sharedPreferences.edit()
    }
    
    private fun observeData() {
        lifecycleScope.launchWhenCreated {
            viewModel.getUserResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        bindData(it.body()!!.data)
                        user = it.body()!!.data
                        
                        if (user.isAdmin == "1") {
                            binding.createUserAccount.visibility = View.VISIBLE
                        } else {
                            binding.createUserAccount.visibility = View.GONE
                        }
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
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
    
    private fun bindData(user: User) {
        if (user.avatar != null && user.avatar != "") {
            Picasso.get().load(user.avatar).fit().centerCrop().into(binding.avatar)
        }
//        binding.userEmail.text = user.userName
        binding.userName.text = user.name
    }
    
    private fun handleAction() {
//        binding.btnRefresh.setOnClickListener {
//            loadData()
//        }
        
        binding.changePassword.setOnClickListener {
            val dialog = ChangePasswordFragment()
            dialog.updateSuccessCallback = {
                binding.logout.performClick()
            }
            showDialog(dialog)
        }
        
        binding.createUserAccount.setOnClickListener {
            showDialog(CreateUserFragment())
        }
        
        binding.logout.setOnClickListener {
            navController.navigate(R.id.action_userFragment_to_authActivity)
            removeToken()
        }
        
        binding.imageContainer.setOnClickListener {
            val fragment = AvatarFragment(user)
            fragment.updateSuccessCallback = {
                loadData()
            }
            showDialog(fragment)
        }
    }
    
    private fun loadData() {
        viewModel.getUser(getToken().toString())
    }
    
//    private fun getToken(): String? {
//        return sharedPreferences.getString("token", "")
//    }
//
//    private fun errorMessage(response: Response<BaseResponse<Any>>) {
//        Toast.makeText(
//            context,
//            ErrorUtils.parseMessage(response as Response<Any>).errorMessage,
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//
//    private fun showDialog(fragment: DialogFragment) {
//        val fm: FragmentManager? = fragmentManager
//        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_NoActionBar)
//        fragment.show(fm!!, "")
//    }
    
    private fun removeToken() {
        editor.putString("token", "")
        editor.commit()
    }
    
}