package com.example.flyingdevicemanager.util.base

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.*
import androidx.viewbinding.ViewBinding
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.util.ErrorUtils
import retrofit2.Response
import java.lang.IllegalArgumentException

open class BaseDialogFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : DialogFragment() {
    
    private var _binding: VB? = null
    
    lateinit var sharedPreferences: SharedPreferences
    
    val binding: VB
        get() = _binding!!
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null")
        }
        return binding.root
    }
    
    fun errorMessage(response: Response<BaseResponse<Any>>) {
        Toast.makeText(
            context,
            ErrorUtils.parseMessage(response as Response<Any>).errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
    
    fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }
    
    fun showDialog(fragment: DialogFragment) {
        val fm: FragmentManager? = fragmentManager
        fragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_NoActionBar)
        fragment.show(fm!!, "")
    }
}