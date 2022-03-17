package com.example.flyingdevicemanager.app_ui.other_user

import android.content.*
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.*
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.other_user.search_user.SearchUserFragment
import com.example.flyingdevicemanager.databinding.*
import com.google.android.material.tabs.TabLayoutMediator

class UserFollowFragment : Fragment() {
    
    lateinit var binding: FragmentUserFollowBinding
    private val viewModel: UserFollowViewModel by activityViewModels()
    
    lateinit var sharedPreferences: SharedPreferences
    
    lateinit var adapter: TabAdapter
    private val tabTitles = listOf("Following", "Follow request")
    
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
        binding = FragmentUserFollowBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        handleAction()
    }
    
    private fun handleAction() {
        binding.searchUser.setOnClickListener {
            showDialog(SearchUserFragment())
        }
    }
    
    private fun initViewPager() {
        adapter = TabAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 1
        
        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        
        val root: View = binding.tab.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.grayLine))
            drawable.setSize(2, 1)
            root.dividerPadding = 25
            root.dividerDrawable = drawable
        }
    }
    
    private fun showDialog(fragment: DialogFragment) {
        val fm: FragmentManager? = fragmentManager
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_NoActionBar)
        fragment.show(fm!!, "")
    }
    
}