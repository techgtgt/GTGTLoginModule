package com.gtgt.loginmodulelibrary.loginmodule.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.text.InputType
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.utils.*
import kotlinx.android.synthetic.main.enter_password_dialog.view.*

class EnterPasswordPopup(
    var activity: Activity
) {
    private var isPinHide = false

    fun showEnterPwdDialog(
        mobileNum: String,
        productType: String,
        onLoginBtnClicked: (String) -> Unit,
        onForgotPwdBtnClicked: (BottomSheetBehavior<FrameLayout>) -> Unit,
        onLoginWithOtpBtnClicked: (BottomSheetBehavior<FrameLayout>) -> Unit,
        onPreviousBtnClicked: () -> Unit
    ) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.enter_password_dialog, null)
        val bottomSheetEnterPwd = BottomSheetDialog(activity, R.style.SheetDialog)
        bottomSheetEnterPwd.window?.setWindowAnimations(R.style.DialogAnimation)
        bottomSheetEnterPwd.setContentView(dialogView)
        bottomSheetEnterPwd.show()
        bottomSheetEnterPwd.setCancelable(false)

        dialogView.tv_previous.makeTextUnderline()

        if (productType == LoginModuleConstants.ProductName.superLit.type) {
            loadSuperLitDialog(dialogView)
        }

        dialogView.tv_previous.onOneClick {
            bottomSheetEnterPwd.dismiss()
            onPreviousBtnClicked()
        }


        dialogView.btn_login.onOneClick {
            val userPwd = dialogView.et_user_pwd.text.toString()
            if (userPwd.isNotEmpty()) {
                if (userPwd.isValidPassword()) {

                    onLoginBtnClicked(userPwd)

                } else {
                    AlertHelper.instance.alert(
                        activity,
                        activity.resources.getString(R.string.invalid_password),
                        "OK"
                    )
                }
            } else {
                AlertHelper.instance.alert(
                    activity,
                    "Please enter password",
                    "OK"
                )
            }
        }

        dialogView.et_user_pwd.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val userPwd = dialogView.et_user_pwd.text.toString()
                if (userPwd.isNotEmpty()) {
                    if (userPwd.isValidPassword()) {

                        onLoginBtnClicked(userPwd)

                    } else {
                        AlertHelper.instance.alert(
                            activity,
                            activity.resources.getString(R.string.invalid_password),
                            "OK"
                        )
                    }
                } else {
                    AlertHelper.instance.alert(
                        activity,
                        "Please enter password",
                        "OK"
                    )
                }

                return@setOnKeyListener true
            } else {
                false
            }
        }

        dialogView.tv_forgot_pwd.onOneClick {
            onForgotPwdBtnClicked(bottomSheetEnterPwd.behavior)
        }

        dialogView.tv_login_with_otp.onOneClick {
            onLoginWithOtpBtnClicked(bottomSheetEnterPwd.behavior)
        }

        dialogView.iv_reveal_pwd.onOneClick {
            customizePwdToggle(dialogView, productType)
        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadSuperLitDialog(dialogView: View) {
        dialogView.ll_enter_pwd.background = activity.getDrawable(R.drawable.superlit_login_bg)
        dialogView.tv_previous.setTextColor(Color.parseColor("#b95c1f"))
        dialogView.et_entered_mobile_num.apply {
            background = activity.getDrawable(R.drawable.superlit_et_bg)
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_phone_superlit,
                0,
                0,
                0
            )
        }
        dialogView.rl_enter_pwd.background = activity.getDrawable(R.drawable.superlit_et_bg)
        dialogView.iv_key.setImageResource(R.drawable.ic_password_key_superlit)
        dialogView.iv_reveal_pwd.setImageResource(R.drawable.ic_hide_pin_superlit)
        dialogView.et_user_pwd.setHintTextColor(Color.parseColor("#796b6b"))
        dialogView.tv_forgot_pwd.setTextColor(Color.parseColor("#ff9124"))
        dialogView.btn_login.background = activity.getDrawable(R.drawable.superlit_btn_bg_gradient)
        dialogView.or_left_line.setBackgroundColor(Color.parseColor("#ff9124"))
        dialogView.or_right_line.setBackgroundColor(Color.parseColor("#ff9124"))
        dialogView.tv_login_with_otp.setTextColor(Color.parseColor("#ff9124"))
        dialogView.iv_super_lit_logo_enter_pwd.visibility = View.GONE
    }

    private fun customizePwdToggle(dialogView: View, productType: String) {

        if (productType == LoginModuleConstants.ProductName.rummyJacks.type) {

            if (isPinHide) {
                dialogView.et_user_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                dialogView.iv_reveal_pwd.setImageResource(R.drawable.ic_show_pwd_icon)
                isPinHide = false
            } else {
                dialogView.et_user_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT
                dialogView.iv_reveal_pwd.setImageResource(R.drawable.ic_hide_pwd_icon_green)
                isPinHide = true
            }
        } else {
            if (isPinHide) {
                dialogView.et_user_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                dialogView.iv_reveal_pwd.setImageResource(R.drawable.ic_hide_pin_superlit)
                isPinHide = false
            } else {
                dialogView.et_user_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT
                dialogView.iv_reveal_pwd.setImageResource(R.drawable.ic_show_pwd_superlit)
                isPinHide = true
            }
        }

    }

}