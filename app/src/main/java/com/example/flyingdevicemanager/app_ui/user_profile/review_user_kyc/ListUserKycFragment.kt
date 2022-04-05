package com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.user_profile.UserViewModel
import com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc.paging.UserPagingAdapter
import com.example.flyingdevicemanager.databinding.FragmentListUserKycBinding
import com.example.flyingdevicemanager.models.User
import com.example.flyingdevicemanager.util.*
import com.example.flyingdevicemanager.util.base.*
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Response

class ListUserKycFragment : BaseDialogFragment<FragmentListUserKycBinding>(
    FragmentListUserKycBinding::inflate
), UserPagingAdapter.ClickListener {
    
//    lateinit var binding: FragmentListUserKycBinding
    private val viewModel: UserViewModel by activityViewModels()
    
    private val adapterUserPaging: UserPagingAdapter by lazy { UserPagingAdapter(this) }
    
//    lateinit var sharedPreferences: SharedPreferences
    
//    override fun onStart() {
//        super.onStart()
//        val dialog: Dialog? = dialog
//        if (dialog != null) {
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
//            val height = ViewGroup.LayoutParams.MATCH_PARENT
//            dialog.window!!.setLayout(width, height)
//        }
//    }
    
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        sharedPreferences = activity!!.getSharedPreferences(
//            "SHARED_PREF",
//            Context.MODE_PRIVATE
//        )
//        // Inflate the layout for this fragment
//        binding = FragmentListUserKycBinding.inflate(layoutInflater)
//        dialog?.setCanceledOnTouchOutside(false)
//        return binding.root
//    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        handleAction()
        loadData()
        observeData()
    }
    
    private fun loadData() {
        viewModel.getUserKycList(getToken().toString())
    }
    
    private fun initRv() {
        binding.rv.adapter = adapterUserPaging
    }
    
    private fun observeData() {
        viewModel.listUserKycLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
        viewModel.pagingFlow.observe(viewLifecycleOwner) {
            adapterUserPaging.submitData(viewLifecycleOwner.lifecycle, it)
        }
        
        lifecycleScope.launchWhenCreated {
            adapterUserPaging.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> binding.loading.visibility = View.VISIBLE
                    is LoadState.Error -> {
                        binding.loading.visibility = View.GONE
                        Toast.makeText(
                            context,
                            (loadStates.refresh as LoadState.Error).error.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> binding.loading.visibility = View.GONE
                }
            }
        }
    }


//    private fun loadData() {
//        viewModel.getAllUserKyc(getToken().toString())
//    }
    
    private fun handleAction() {
        binding.btnRefresh.setOnClickListener {
            adapterUserPaging.refresh()
        }
        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }
    
//    private fun getToken(): String? {
//        return sharedPreferences.getString("token", "")
//    }
    
//    private fun errorMessage(response: Response<BaseResponse<Any>>) {
//        Toast.makeText(
//            context,
//            ErrorUtils.parseMessage(response as Response<Any>).errorMessage,
//            Toast.LENGTH_SHORT
//        ).show()
//    }
    
//    private fun showDialog(fragment: DialogFragment) {
//        val fm: FragmentManager? = fragmentManager
//        fragment.setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Light_NoActionBar)
//        fragment.show(fm!!, "")
//    }
    
    override fun showUserKycDetail(user: User, position: Int) {
        val fragment = UserKycDetailFragment(user, position)
        fragment.reviewFinishCallback = {
            loadData()
//            adapterUserPaging.notifyItemRemoved(position)
            adapterUserPaging.refresh()
        }
        showDialog(fragment)
    }
}