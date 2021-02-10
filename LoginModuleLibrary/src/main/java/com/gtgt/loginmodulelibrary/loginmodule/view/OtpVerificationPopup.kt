package com.gtgt.loginmodulelibrary.loginmodule.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.utils.*
import kotlinx.android.synthetic.main.otp_dialog.view.*

class OtpVerificationPopup(var activity: Activity) {
    fun showOtpVerificationDialog(
        isUserRegistered: Boolean = false,
        mobileNum: String = "",
        productType: String,
        onVerifyBtnClicked: (String, BottomSheetBehavior<FrameLayout>) -> Unit,
        onResendOtpBtnClicked: (BottomSheetBehavior<FrameLayout>, Boolean) -> Unit,
        onPreviousBtnClicked: () -> Unit
    ) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.otp_dialog, null)

        if (productType == LoginModuleConstants.ProductName.superLit.type) {
            loadSuperLitDialog(dialogView)
        }


        val bottomSheetOtpVerification = BottomSheetDialog(activity, R.style.SheetDialog)
        bottomSheetOtpVerification.window?.setWindowAnimations(R.style.DialogAnimation)
        bottomSheetOtpVerification.setContentView(dialogView)
        bottomSheetOtpVerification.show()
        bottomSheetOtpVerification.setCancelable(false)

        dialogView.tv_previous_otp_screen.makeTextUnderline()
        dialogView.tv_resend_otp.makeTextUnderline()

        dialogView.tv_previous_otp_screen.onOneClick {
            bottomSheetOtpVerification.dismiss()
            onPreviousBtnClicked()
        }


        dialogView.tv_entered_mob_num.text = mobileNum

        dialogView.btn_verify_otp.onOneClick {
            val otp = dialogView.otp_box.text.toString()
            if (otp.length == 4) {
                onVerifyBtnClicked(otp, bottomSheetOtpVerification.behavior)
            } else {
                activity.showSnack("OTP you've entered is incorrect, please re-check")
            }
        }

        dialogView.tv_resend_otp.onOneClick {
            onResendOtpBtnClicked(bottomSheetOtpVerification.behavior, isUserRegistered)
        }


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadSuperLitDialog(dialogView: View) {
        dialogView.ll_otp_activity.background = activity.getDrawable(R.drawable.superlit_login_bg)
        dialogView.tv_previous_otp_screen.setTextColor(Color.parseColor("#b95c1f"))
        dialogView.rl_entered_mobile_num.background =
            activity.getDrawable(R.drawable.superlit_et_bg)
        dialogView.iv_phone_icon.apply {
            setImageResource(R.drawable.ic_phone_superlit)
            margins(12, 0, 0, 0)
        }

        dialogView.btn_verify_otp.apply {
            background =
                activity.getDrawable(R.drawable.superlit_btn_bg_gradient)
            margins(0, 0, 0, 20)
        }

        dialogView.iv_super_lit_logo_otp_verification.visibility = View.GONE
    }
}