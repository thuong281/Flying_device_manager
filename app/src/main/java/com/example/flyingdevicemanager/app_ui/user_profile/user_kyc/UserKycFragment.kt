package com.example.flyingdevicemanager.app_ui.user_profile.user_kyc

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
import com.example.flyingdevicemanager.databinding.FragmentUserKycBinding
import com.example.flyingdevicemanager.models.UserKyc
import com.example.flyingdevicemanager.util.ErrorUtils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import okhttp3.*
import retrofit2.Response
import java.io.File


class UserKycFragment : DialogFragment() {
    
    
    lateinit var binding: FragmentUserKycBinding
    private val viewModel: UserKycViewModel by activityViewModels()
    
    lateinit var sharedPreferences: SharedPreferences
    
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = activity!!.getSharedPreferences(
            "SHARED_PREF",
            Context.MODE_PRIVATE
        )
        // Inflate the layout for this fragment
        binding = FragmentUserKycBinding.inflate(layoutInflater)
        dialog?.setCanceledOnTouchOutside(false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        getUserKyc()
        handleAction()
        binding.submit.isEnabled = false
    }
    
    private fun getUserKyc() {
        viewModel.getUserKyc(getToken().toString())
    }
    
    private fun observeData() {
        viewModel.frontImageUri.observe(viewLifecycleOwner) {
            if (viewModel.backImageUri.value != null) binding.submit.isEnabled = true
            Picasso.get().load(it).fit().centerCrop().into(binding.imageFrontUpload)
        }
        viewModel.backImageUri.observe(viewLifecycleOwner) {
            if (viewModel.frontImageUri.value != null) binding.submit.isEnabled = true
            Picasso.get().load(it).fit().centerCrop().into(binding.imageBackUpload)
        }
        lifecycleScope.launchWhenCreated {
            viewModel.submitResponse.collectLatest {
                if (it.isSuccessful) {
                    Toast.makeText(context, "upload success", Toast.LENGTH_SHORT).show()
                    getUserKyc()
                } else {
                    Toast.makeText(
                        context,
                        ErrorUtils.parseMessage(it as Response<Any>).errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            
            viewModel.userKycResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        val userKyc = it.body()?.data
                        bindData(userKyc!!)
                        binding.uploadScreen.visibility = View.GONE
                        binding.showScreen.visibility = View.VISIBLE
                        binding.statusPending.visibility = View.GONE
                        binding.statusVerified.visibility = View.VISIBLE
                    }
                    202 -> {
                        val userKyc = it.body()?.data
                        bindData(userKyc!!)
                        binding.uploadScreen.visibility = View.GONE
                        binding.showScreen.visibility = View.VISIBLE
                        binding.statusPending.visibility = View.VISIBLE
                        binding.statusVerified.visibility = View.GONE
                    }
                    404 -> {
                        binding.uploadScreen.visibility = View.VISIBLE
                        binding.showScreen.visibility = View.GONE
                    }
                    500 -> {
                        Toast.makeText(context, "Invalid token", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        
        lifecycleScope.launchWhenCreated {
            viewModel.userKycResponse.collectLatest {
                when (it.code()) {
                    200 -> {
                        val userKyc = it.body()?.data
                        bindData(userKyc!!)
                        binding.uploadScreen.visibility = View.GONE
                        binding.showScreen.visibility = View.VISIBLE
                        binding.statusPending.visibility = View.GONE
                        binding.statusVerified.visibility = View.VISIBLE
                    }
                    202 -> {
                        val userKyc = it.body()?.data
                        bindData(userKyc!!)
                        binding.uploadScreen.visibility = View.GONE
                        binding.showScreen.visibility = View.VISIBLE
                        binding.statusPending.visibility = View.VISIBLE
                        binding.statusVerified.visibility = View.GONE
                    }
                    404 -> {
                        binding.uploadScreen.visibility = View.VISIBLE
                        binding.showScreen.visibility = View.GONE
                    }
                    500 -> {
                        Toast.makeText(context, "Invalid token", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }
        }
        
    }
    
    private val getFrontImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        viewModel.frontImageUri.postValue(it)
    }
    
    private val getBackImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        viewModel.backImageUri.postValue(it)
    }
    
    private fun handleAction() {
        binding.imageFrontUpload.setOnClickListener {
            getFrontImage.launch("image/*")
        }
        binding.imageBackUpload.setOnClickListener {
            getBackImage.launch("image/*")
        }
        binding.submit.setOnClickListener {
            val listImage = ArrayList<MultipartBody.Part>()
            listImage.add(prepareImagesPart("image", viewModel.frontImageUri.value))
            listImage.add(prepareImagesPart("image", viewModel.backImageUri.value))
            
            val requestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.nationalIdText.text.toString()
            )
            
            viewModel.submitUserKyc(getToken().toString(), listImage, requestBody)
        }
        binding.btnRefresh.setOnClickListener {
            getUserKyc()
        }
        binding.btnBack.setOnClickListener {
            dismiss()
        }
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
    
    private fun getToken(): String? {
        return sharedPreferences.getString("token", "")
    }
    
    private fun bindData(userKyc: UserKyc) {
        binding.userId.text = userKyc.nationalId
        Picasso.get().load(userKyc.frontImage).fit().centerCrop().into(binding.imageFront)
        Picasso.get().load(userKyc.backImage).fit().centerCrop().into(binding.imageBack)
    }
}