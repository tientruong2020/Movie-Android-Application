package com.group1.movieapplication.ui.changePassword

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.group1.movieapplication.R
import com.group1.movieapplication.ui.view.LoginActivity
import kotlinx.android.synthetic.main.change_password_dialog.*

class ChangePasswordDialog : DialogFragment(), View.OnClickListener {

    var changePasswordValidator = ChangePasswordValidation
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.change_password_dialog, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordViewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_confirm.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            (R.id.btn_confirm) -> {
                handleClickConfirmBtn()
            }
            (R.id.btn_cancel) -> {
                handleClickCancelBtn()
            }
        }
    }

    fun handleClickConfirmBtn() {
        val currentPW = et_current_pw.text.toString()
        val newPW = et_new_pw.text.toString()
        val confirmNewPW = et_confirm_new_pw.text.toString()
        if (changePasswordValidator.notEmptyAllFields(
                currentPW,
                newPW,
                currentPW
            ) && changePasswordValidator.isConfirmPwEqualNewPw(newPW, confirmNewPW)
        ) {
            changePasswordViewModel.changePassword(currentPW, confirmNewPW)
                .observe(viewLifecycleOwner, Observer { result ->
                    if (result) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully Change Password ",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                        toLoginActivity()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Change Password Process is erroring",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            et_confirm_new_pw.error = "Confirm password isn't equal New Password"
        }
    }

    fun handleClickCancelBtn() {
        dismiss()
    }

    fun toLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

}