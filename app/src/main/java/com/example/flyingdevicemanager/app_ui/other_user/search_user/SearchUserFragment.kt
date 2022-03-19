package com.example.flyingdevicemanager.app_ui.other_user.search_user

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.other_user.UserFollowViewModel
import com.example.flyingdevicemanager.databinding.FragmentSearchUserBinding
import com.example.flyingdevicemanager.models.UserFollow
import com.example.flyingdevicemanager.util.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class SearchUserFragment : DialogFragment(), SearchUserAdapter.ClickListener {
    
    lateinit var binding: FragmentSearchUserBinding
    private val viewModel: UserFollowViewModel by activityViewModels()
    
    private val adapter: SearchUserAdapter by lazy { SearchUserAdapter(this) }
    
    private lateinit var sharedPreferences: SharedPreferences
    
    private var currentSearchPhrase = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
        initRv()
    }
    
    private fun initRv() {
        binding.rv.adapter = adapter
    }
    
    private fun observeData() {
        viewModel.searchUserLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.searchUserResponse.collectLatest {
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
        
        lifecycleScope.launchWhenCreated {
            viewModel.requestFollowResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        Toast.makeText(context, "Send request successfully", Toast.LENGTH_SHORT)
                            .show()
                        viewModel.searchUser(getToken().toString(), currentSearchPhrase)
                    }
                    else -> {
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
        
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.search.clearFocus()
                if (p0!!.length < 2) {
                    Toast.makeText(context, "Enter more than 1 characters", Toast.LENGTH_SHORT)
                        .show()
                }
                currentSearchPhrase = p0.toString()
                viewModel.searchUser(getToken().toString(), p0.toString())
                return false
            }
            
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
    }
    
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 1).toInt()
            val height = (resources.displayMetrics.heightPixels * 1).toInt()
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
    
    override fun sendRequest(data: UserFollow) {
        viewModel.requestFollow(getToken().toString(), data.userId.toString())
    }
    
}