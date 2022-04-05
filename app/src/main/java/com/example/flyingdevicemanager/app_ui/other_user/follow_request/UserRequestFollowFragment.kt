package com.example.flyingdevicemanager.app_ui.other_user.follow_request

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.app_ui.other_user.UserFollowViewModel
import com.example.flyingdevicemanager.databinding.FragmentUserRequestFollowBinding
import com.example.flyingdevicemanager.models.User
import com.example.flyingdevicemanager.util.*
import com.example.flyingdevicemanager.util.base.BaseResponse
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class UserRequestFollowFragment : Fragment(), UserRequestFollowAdapter.ClickListener {
    
    lateinit var binding: FragmentUserRequestFollowBinding
    private val viewModel: UserFollowViewModel by activityViewModels()
    
    private val adapter: UserRequestFollowAdapter by lazy { UserRequestFollowAdapter(this) }
    
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
        binding = FragmentUserRequestFollowBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        observeData()
        loadData()
        handleAction()
    }
    
    private fun handleAction() {
        binding.freshLayout.setOnRefreshListener {
            loadData()
        }
    }
    
    private fun loadData() {
        viewModel.getListRequestFollow(getToken().toString())
    }
    
    private fun observeData() {
        viewModel.responseRequestFollowLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        viewModel.getListRequestFollowLoading.observe(viewLifecycleOwner) {
            binding.freshLayout.isRefreshing = it
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.responseRequestFollowResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        Toast.makeText(context, it.body()?.msg, Toast.LENGTH_SHORT).show()
                        loadData()
                    }
                    else -> {
                        errorMessage(it)
                    }
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.listRequestFollowResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        adapter.items = it.body()?.data ?: ArrayList()
                    }
                    else -> {
                        errorMessage(it as Response<BaseResponse<Any>>)
                    }
                }
            }
        }
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
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
    
    override fun onApprove(user: User) {
        viewModel.responseRequestFollow(getToken().toString(), 1, user.id.toString())
    }
    
    override fun onReject(user: User) {
        viewModel.responseRequestFollow(getToken().toString(), 0, user.id.toString())
    }
    
}