package com.gtgt.loginmodulelibrary.loginmodule.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import com.facebook.login.widget.LoginButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.utils.LoginModuleConstants
import com.gtgt.loginmodulelibrary.utils.makeTextUnderline
import com.gtgt.loginmodulelibrary.utils.onOneClick
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.login_dialog.view.*

//to invoke user login popup

class UserLoginPopup(
    var activity: Activity
) {
    lateinit var bottomSheetLogin: BottomSheetDialog

    fun showUserLoginDialog(

        productType: String,
        onProceedBtnClicked: (String, BottomSheetBehavior<FrameLayout>) -> Unit,
        onFbLoginBtnClicked: (BottomSheetBehavior<FrameLayout>, LoginButton) -> Unit,
        onGoogleLoginBtnClicked: (BottomSheetBehavior<FrameLayout>) -> Unit
    ) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.login_dialog, null)

        if (productType == LoginModuleConstants.ProductName.superLit.type) {
            loadSuperLitDialog(dialogView)
        }

        bottomSheetLogin = BottomSheetDialog(activity, R.style.SheetDialog)
        bottomSheetLogin.window?.setWindowAnimations(R.style.DialogAnimation)
        bottomSheetLogin.window?.navigationBarColor = Color.WHITE
        bottomSheetLogin.setContentView(dialogView)
        bottomSheetLogin.show()
        bottomSheetLogin.setCancelable(false)

        dialogView.tv_skip_login.makeTextUnderline()


        dialogView.fb_login_btn.onOneClick {
            onFbLoginBtnClicked(bottomSheetLogin.behavior, dialogView.btn_facebook_login)
        }

        dialogView.btn_google_login.onOneClick {
            onGoogleLoginBtnClicked(bottomSheetLogin.behavior)
        }

        dialogView.btn_proceed.onOneClick {
            checkSmsPermissions {
                onProceedBtnClicked(
                    dialogView.et_enter_mobile_num.text.toString(),
                    bottomSheetLogin.behavior
                )
            }
        }

        dialogView.btn_go_nxt.onOneClick {
            checkSmsPermissions {
                onProceedBtnClicked(
                    dialogView.et_enter_mobile_num.text.toString(),
                    bottomSheetLogin.behavior
                )
            }
        }

        dialogView.et_enter_mobile_num.setOnKeyListener(View.OnKeyListener { v, keycode, event ->
            if (keycode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                onProceedBtnClicked(
                    dialogView.et_enter_mobile_num.text.toString(),
                    bottomSheetLogin.behavior
                )

                return@OnKeyListener true
            }
            false
        })


        dialogView.tv_skip_login.onOneClick {
//            activity.showToast("You are successfully logged into the app")
            activity.finish()
            bottomSheetLogin.dismiss()
        }

        dialogView.btn_proceed_as_guest.onOneClick {
//            activity.showToast("You are successfully logged into the app")
            activity.finish()
            bottomSheetLogin.dismiss()
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadSuperLitDialog(dialogView: View) {
        dialogView.ll_registration.background = activity.getDrawable(R.drawable.superlit_login_bg)
        dialogView.tv_skip_login.visibility = GONE
        dialogView.rl_et_mob_num.background = activity.getDrawable(R.drawable.superlit_et_bg)
        dialogView.btn_go_nxt.visibility = GONE
        dialogView.et_enter_mobile_num.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_phone_superlit,
            0,
            0,
            0
        )
        dialogView.et_enter_mobile_num.setHintTextColor(Color.parseColor("#796b6b"))
        dialogView.btn_proceed_as_guest.visibility = GONE
        dialogView.btn_proceed.visibility = VISIBLE
        dialogView.iv_super_lit_logo_login.visibility = GONE
    }


//    @SuppressLint("UseCompatLoadingForDrawables")
//    private fun loadRummyJacksDialog(dialogView: View) {
//        dialogView.ll_registration.background = activity.getDrawable(R.drawable.superlit_login_bg)
//        dialogView.tv_skip_login.visibility = GONE
//        dialogView.rl_et_mob_num.background = activity.getDrawable(R.drawable.superlit_et_bg)
//        dialogView.btn_go_nxt.visibility = GONE
//        dialogView.et_enter_mobile_num.setCompoundDrawablesWithIntrinsicBounds(
//            R.drawable.ic_phone_superlit,
//            0,
//            0,
//            0
//        )
//        dialogView.et_enter_mobile_num.setHintTextColor(Color.parseColor("#796b6b"))
//        dialogView.btn_proceed.visibility = VISIBLE
//        dialogView.btn_proceed_as_guest.visibility = GONE
//        dialogView.ll_super_lit_logo.visibility = GONE
//
//
//    }

    private fun checkSmsPermissions(onPermissionsGranted: (Boolean) -> Unit) {
        Dexter.withActivity(activity)
            .withPermissions(android.Manifest.permission.RECEIVE_SMS)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    onPermissionsGranted(true)

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }


}