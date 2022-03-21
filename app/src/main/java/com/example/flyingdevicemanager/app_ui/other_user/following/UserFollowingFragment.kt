package com.example.flyingdevicemanager.app_ui.other_user.following

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.app_ui.other_user.UserFollowViewModel
import com.example.flyingdevicemanager.databinding.FragmentUserFollowingBinding
import com.example.flyingdevicemanager.util.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class UserFollowingFragment : Fragment() {
    
    lateinit var binding: FragmentUserFollowingBinding
    private val viewModel: UserFollowViewModel by activityViewModels()
    
    private val adapter: UserFollowingAdapter by lazy { UserFollowingAdapter() }
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        binding = FragmentUserFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        loadData()
        handleAction()
        observeData()
    }
    
    private fun observeData() {
        viewModel.listFollowingLoading.observe(viewLifecycleOwner) {
            binding.freshLayout.isRefreshing = it
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.listFollowingResponse.collectLatest {
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
    
    private fun handleAction() {
        binding.freshLayout.setOnRefreshListener {
            loadData()
        }
    }
    
    private fun loadData() {
        viewModel.getListFollowing(getToken().toString())
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
    
}