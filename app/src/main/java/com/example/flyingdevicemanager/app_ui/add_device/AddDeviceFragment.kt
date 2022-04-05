package com.example.flyingdevicemanager.app_ui.add_device

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.add_device.add.AddFragment
import com.example.flyingdevicemanager.app_ui.add_device.history.HistoryFragment
import com.example.flyingdevicemanager.app_ui.device.DeviceViewModel
import com.example.flyingdevicemanager.databinding.FragmentAddDevice2Binding
import com.example.flyingdevicemanager.util.TimeUtils
import com.example.flyingdevicemanager.util.base.BaseFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest

class AddDeviceFragment : BaseFragment<FragmentAddDevice2Binding>(
    FragmentAddDevice2Binding::inflate
), AddFragment.Callback {
    
    private val tabTitles = listOf("Add", "Add history")

    lateinit var adapter: TabAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        adapter = TabAdapter(requireActivity(), this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        val root: View = binding.tab.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.grayLine))
            drawable.setSize(2, 1)
            root.dividerPadding = 20
            root.dividerDrawable = drawable
        }
    }
    
    override fun onAddDeviceSuccess() {
        binding.viewPager.currentItem = 1
    }
}