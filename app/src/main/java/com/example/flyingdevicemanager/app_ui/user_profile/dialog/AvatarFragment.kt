package com.example.flyingdevicemanager.app_ui.user_profile.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.example.flyingdevicemanager.R
import com.example.flyingdevicemanager.app_ui.user_profile.UserViewModel
import com.example.flyingdevicemanager.databinding.FragmentAvatarBinding
import com.example.flyingdevicemanager.models.User
import com.example.flyingdevicemanager.util.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import okhttp3.*
import retrofit2.Response
import java.io.File


class AvatarFragment(private val user: User) : DialogFragment() {
    
    lateinit var binding: FragmentAvatarBinding
    val viewModel: UserViewModel by activityViewModels()
    
    var updateSuccessCallback: (() -> Unit)? = null
    
    lateinit var sharedPreferences: SharedPreferences
    
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
            val height = (resources.displayMetrics.heightPixels * 0.50).toInt()
            dialog.window!!.setLayout(width, height)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        binding = FragmentAvatarBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAction()
        observeData()
        bindData()
    }
    
    private fun bindData() {
        if (user.avatar != null && user.avatar != "") {
            Picasso.get().load(user.avatar).fit().centerCrop().into(binding.image)
        }
    }
    
    private fun observeData() {
        viewModel.updateAvatarLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        viewModel.imageUri.observe(viewLifecycleOwner) {
            binding.changeAvatar.isEnabled = it != null
            if (it != null) {
                Picasso.get().load(it).fit().centerCrop().into(binding.image)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.changeAvatarResponse.collectLatest {
                when (it.code()) {
                    204 -> {
                        dismiss()
                        updateSuccessCallback!!.invoke()
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
        binding.imageHolder.setOnClickListener {
            getImage.launch("image/*")
        }
        binding.changeAvatar.setOnClickListener {
            viewModel.changeAvatar(
                getToken().toString(),
                prepareImagesPart("image", viewModel.imageUri.value)
            )
        }
    }
    
    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        viewModel.imageUri.postValue(it)
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
    
    private fun prepareImagesPart(partName: String, fileUri: Uri?): MultipartBody.Part {
        val file = File(getImagePath(fileUri))
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
    
    @SuppressLint("Range")
    fun getImagePath(uri: Uri?): String? {
        var cursor: Cursor = activity?.contentResolver?.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        var documentId = cursor.getString(0)
        documentId = documentId.substring(documentId.lastIndexOf(":") + 1)
        cursor.close()
        cursor = activity!!.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(documentId), null
        )!!
        cursor.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }
}