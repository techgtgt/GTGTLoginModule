package com.gtgt.loginmodulelibrary.loginmodule.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.utils.*
import kotlinx.android.synthetic.main.set_password_dialog.view.*

class SetPasswordPopup(
    var activity: Activity
) {

    var isPinHide = false

    fun showSetPwdDialog(
        userUniqueId: String = "",
        productType: String,
        onSetPwdBtnClicked: (String, String, Boolean) -> Unit,
        onSkipPwdBtnClicked: (String) -> Unit
    ) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.set_password_dialog, null)
        val bottomSheetSetPwd = BottomSheetDialog(activity, R.style.SheetDialog)
        bottomSheetSetPwd.window?.setWindowAnimations(R.style.DialogAnimation)
        bottomSheetSetPwd.setContentView(dialogView)
        bottomSheetSetPwd.show()
        bottomSheetSetPwd.setCancelable(false)

        dialogView.tv_skip_pwd.makeTextUnderline()

        if (productType == LoginModuleConstants.ProductName.superLit.type) {
            loadSuperLitDialog(dialogView)
        }


        dialogView.btn_set_pwd.onOneClick {
            if (dialogView.et_set_pwd.text.toString().isNotEmpty()) {
                if (dialogView.et_confirm_pwd.text.toString().isNotEmpty()) {
                    if (dialogView.et_set_pwd.text.toString().isValidPassword()) {
                        if (dialogView.et_set_pwd.text.toString() == dialogView.et_confirm_pwd.text.toString()) {
                            onSetPwdBtnClicked(
                                dialogView.et_set_pwd.text.toString(),
                                userUniqueId,
                                true
                            )
                        } else {
                            AlertHelper.instance.alert(
                                activity,
                                "Password should match. Please confirm your password again",
                                "OK"
                            )
                        }
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
                        "Please confirm your password",
                        "OK"
                    )
                }
            } else {
                AlertHelper.instance.alert(activity, "Please enter password", "OK")
            }
        }

        dialogView.tv_skip_pwd.onOneClick {
            onSkipPwdBtnClicked(userUniqueId)
        }

        dialogView.iv_reveal_set_pwd.onOneClick {
            customizedSetPwdToggle(dialogView, productType)
        }

        dialogView.iv_reveal_confirm_pwd.onOneClick {
            customizedConfirmPwdToggle(dialogView, productType)
        }


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadSuperLitDialog(dialogView: View) {
        dialogView.ll_set_pwd.background = activity.getDrawable(R.drawable.superlit_login_bg)
        dialogView.tv_skip_pwd.setTextColor(Color.parseColor("#b95c1f"))
        dialogView.rl_set_pwd.background = activity.getDrawable(R.drawable.superlit_et_bg)
        dialogView.iv_key_set_pwd.apply {
            setImageResource(R.drawable.ic_password_key_superlit)
            margins(12, 0, 0, 0)
        }
        dialogView.et_set_pwd.setHintTextColor(Color.parseColor("#796b6b"))
        dialogView.rl_confirm_pwd.background = activity.getDrawable(R.drawable.superlit_et_bg)
        dialogView.et_confirm_pwd.setHintTextColor(Color.parseColor("#796b6b"))
        dialogView.iv_key_confirm_pwd.apply {
            setImageResource(R.drawable.ic_password_key_superlit)
            margins(12, 0, 0, 0)
        }
        dialogView.btn_set_pwd.background =
            activity.getDrawable(R.drawable.superlit_btn_bg_gradient)
        dialogView.iv_reveal_set_pwd.setImageResource(R.drawable.ic_hide_pin_superlit)
        dialogView.iv_reveal_confirm_pwd.setImageResource(R.drawable.ic_hide_pin_superlit)

        dialogView.iv_super_lit_logo_set_pwd.visibility = GONE
    }


    private fun customizedSetPwdToggle(dialogView: View, productType: String) {

        if (productType == LoginModuleConstants.ProductName.rummyJacks.type) {

            if (isPinHide) {
                dialogView.et_set_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                dialogView.iv_reveal_set_pwd.setImageResource(R.drawable.ic_show_pwd_icon)
                isPinHide = false
            } else {
                dialogView.et_set_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT
                dialogView.iv_reveal_set_pwd.setImageResource(R.drawable.ic_hide_pwd_icon_green)
                isPinHide = true
            }
        } else {
            if (isPinHide) {
                dialogView.et_set_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                dialogView.iv_reveal_set_pwd.setImageResource(R.drawable.ic_hide_pin_superlit)
                isPinHide = false
            } else {
                dialogView.et_set_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT
                dialogView.iv_reveal_set_pwd.setImageResource(R.drawable.ic_show_pwd_superlit)
                isPinHide = true
            }
        }

    }

    private fun customizedConfirmPwdToggle(dialogView: View, productType: String) {

        if (productType == LoginModuleConstants.ProductName.rummyJacks.type) {

            if (isPinHide) {
                dialogView.et_confirm_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                dialogView.iv_reveal_confirm_pwd.setImageResource(R.drawable.ic_show_pwd_icon)
                isPinHide = false
            } else {
                dialogView.et_confirm_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT
                dialogView.iv_reveal_confirm_pwd.setImageResource(R.drawable.ic_hide_pwd_icon_green)
                isPinHide = true
            }
        } else {
            if (isPinHide) {
                dialogView.et_confirm_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                dialogView.iv_reveal_confirm_pwd.setImageResource(R.drawable.ic_hide_pin_superlit)
                isPinHide = false
            } else {
                dialogView.et_confirm_pwd.inputType =
                    InputType.TYPE_CLASS_TEXT
                dialogView.iv_reveal_confirm_pwd.setImageResource(R.drawable.ic_show_pwd_superlit)
                isPinHide = true
            }
        }

    }
}