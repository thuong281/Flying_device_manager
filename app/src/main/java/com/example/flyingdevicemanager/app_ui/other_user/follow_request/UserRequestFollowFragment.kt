package com.example.flyingdevicemanager.app_ui.other_user.follow_request

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.flyingdevicemanager.R

class UserRequestFollowFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_request_follow, container, false)
    }
    
}