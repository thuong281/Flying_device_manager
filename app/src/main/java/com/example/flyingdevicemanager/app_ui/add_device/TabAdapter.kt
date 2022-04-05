package com.example.flyingdevicemanager.app_ui.add_device

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flyingdevicemanager.app_ui.add_device.add.AddFragment
import com.example.flyingdevicemanager.app_ui.add_device.history.HistoryFragment

class TabAdapter(fragmentActivity: FragmentActivity, private val callback: AddFragment.Callback) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = AddFragment()
                fragment.callback = callback
                fragment
            }
            else -> HistoryFragment()
        }
    }
    
}