package com.example.flyingdevicemanager.app_ui.other_user

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flyingdevicemanager.app_ui.other_user.follow_request.UserRequestFollowFragment
import com.example.flyingdevicemanager.app_ui.other_user.following.UserFollowingFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 2
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserFollowingFragment()
            1 -> UserRequestFollowFragment()
            else -> UserFollowingFragment()
        }
    }
}