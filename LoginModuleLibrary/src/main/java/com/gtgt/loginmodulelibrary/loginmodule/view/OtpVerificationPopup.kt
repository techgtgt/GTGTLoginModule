package com.gtgt.loginmodulelibrary.loginmodule.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.utils.*
import kotlinx.android.synthetic.main.otp_dialog.view.*

class OtpVerificationPopup(var activity: Activity) {

    lateinit var otpDialogView: View


    fun showOtpVerificationDialog(
        isUserRegistered: Boolean = false,
        mobileNum: String = "",
        productType: String,
        registerServiceCallBack: (SmsReceiver) -> Unit,
        onVerifyBtnClicked: (String, BottomSheetBehavior<FrameLayout>) -> Unit,
        onResendOtpBtnClicked: (BottomSheetBehavior<FrameLayout>, Boolean) -> Unit,
        onPreviousBtnClicked: () -> Unit
    ) {
        otpDialogView = LayoutInflater.from(activity).inflate(R.layout.otp_dialog, null)

        if (productType == LoginModuleConstants.ProductName.superLit.type) {
            loadSuperLitDialog(otpDialogView)
        }

        val smsReceiver = SmsReceiver {
            otpDialogView.otp_box.setText(it)
        }

        registerServiceCallBack(smsReceiver)


        val bottomSheetOtpVerification = BottomSheetDialog(activity, R.style.SheetDialog)
        bottomSheetOtpVerification.window?.setWindowAnimations(R.style.DialogAnimation)
        bottomSheetOtpVerification.setContentView(otpDialogView)
        bottomSheetOtpVerification.show()
        bottomSheetOtpVerification.setCancelable(false)

        otpDialogView.tv_previous_otp_screen.makeTextUnderline()
        otpDialogView.tv_resend_otp.makeTextUnderline()

        otpDialogView.tv_previous_otp_screen.onOneClick {
            bottomSheetOtpVerification.dismiss()
            onPreviousBtnClicked()
        }


        otpDialogView.tv_entered_mob_num.text = mobileNum

        otpDialogView.btn_verify_otp.onOneClick {
            val otp = otpDialogView.otp_box.text.toString()
            checkForValidOtp(otp) {

                onVerifyBtnClicked(otp, bottomSheetOtpVerification.behavior)

            }

        }

        otpDialogView.otp_box.doAfterTextChanged {
            checkForValidOtp(it.toString()) {
                onVerifyBtnClicked(it.toString(), bottomSheetOtpVerification.behavior)
            }
        }


        otpDialogView.tv_resend_otp.onOneClick {
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


    private fun checkForValidOtp(otp: String, isOtpValid: () -> Unit) {
        if (otp.length == 4) {
            isOtpValid()

        } else {
            activity.showSnack("OTP you've entered is incorrect, please re-check")
        }
    }



}