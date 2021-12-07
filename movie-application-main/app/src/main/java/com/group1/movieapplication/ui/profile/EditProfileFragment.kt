package com.group1.movieapplication.ui.profile


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.group1.movieapplication.R
import com.group1.movieapplication.data.repository.FirebaseRTDBRepository
import com.group1.movieapplication.model.user.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class EditProfileFragment : Fragment(), View.OnClickListener {

    lateinit var editProfileViewmodel: EditProfileViewmodel
    var localImgUri: Uri? = null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var storageReference: StorageReference
    lateinit var currentUser: User
    val updateProfileValidation = UpdateProfileValidation
    lateinit var loadingDialog: LoadingDialog

    companion object {
        const val REQUEST_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileViewmodel = ViewModelProvider(this).get(EditProfileViewmodel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference()
            .child(FirebaseRTDBRepository.AVATAR_BUCKET_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfile()
        btn_change_avatar.setOnClickListener(this)
        btn_back_toprofile.setOnClickListener(this)
        btn_save_profile.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btn_change_avatar) -> {
                checkAndRequestPermission()
            }

            (R.id.btn_back_toprofile) -> {
                backToProfile()
            }

            (R.id.btn_save_profile) -> {
                handleSaveClick()
            }
        }
    }

    fun backToProfile() {
        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
    }


    private fun openGallery() {
        resultPickerImage.launch("image/*")
    }

    private fun checkAndRequestPermission() {
        if (!isGrandReadExternalStoragePermission()) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                }
            }
        }
    }

    fun isGrandReadExternalStoragePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )) == PackageManager.PERMISSION_GRANTED
    }


    var resultPickerImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imv_edit_avatar.setImageURI(uri)
            localImgUri = uri
        }

    fun getProfile() {
        editProfileViewmodel.getUserInfor().observe(viewLifecycleOwner, Observer { user ->
            setUserInfoToView(user)
        })
    }

    fun setUserInfoToView(user: User) {
        currentUser = user
        et_first_name.text = Editable.Factory.getInstance().newEditable(user.firstname)
        et_last_name.text = Editable.Factory.getInstance().newEditable(user.lastname)
        if (user.avatar_uri != null) {
            val imgUri = Uri.parse(user.avatar_uri)
            Picasso.get().load(imgUri).into(imv_edit_avatar)
        } else {
            imv_edit_avatar.setImageResource(R.drawable.defaultavatar)
        }
    }


    @SuppressLint("LogNotTimber")
    fun uploadAvatar(localImgUri: Uri) {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        editProfileViewmodel.uploadAvatar(localImgUri)
            .observe(viewLifecycleOwner, { result ->
                if (result) {
                    loadingDialog.dismissDialog()
                }
            })
    }

    fun updateProfileInfo() {
        val firstname = et_first_name.text.toString()
        val lastname = et_last_name.text.toString()
        if (updateProfileValidation.isChangedProfileInfo(currentUser, firstname, lastname)) {
            if (!updateProfileValidation.isEmptyFields(firstname, lastname)) {
                editProfileViewmodel.updateProfile(firstname, lastname)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Firstname Or LastName Fields is empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun handleSaveClick() {
        updateProfileInfo()
        localImgUri?.let { uploadAvatar(it) }
    }


    override fun onDestroy() {
        super.onDestroy()
        localImgUri = null
    }


}

