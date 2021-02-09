package com.gtgt.loginmodulelibrary.utils

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseActivity
import kotlinx.android.synthetic.main.error_dialog.view.*

class ShowErrorDialog(
    var activity: LoginModuleBaseActivity,
    private var errMsg: String = "",
    var shouldLogout: Boolean = false

) {

    fun showErrorDialog(
    ) {
        try {
            if (!activity.isRunning())
                return
            val errorDialogView = LayoutInflater.from(activity).inflate(R.layout.error_dialog, null)
            //customizing errorDialogView
            errorDialogView.cv_error_dialog.setCardBackgroundColor(
                Color.parseColor(
                    cv_backgroundColor
                )
            )
            errorDialogView.iv_error_dialog.setImageResource(error_dialog_img)
            errorDialogView.btn_okErrorDialog.background = activity.getDrawable(btnBg)
            errorDialogView.btn_okErrorDialog.text = btnText
            errorDialogView.btn_close_err_dialog.setImageResource(closeDialogBtn)

            val builder = AlertDialog.Builder(activity)
            builder.setView(errorDialogView)

            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            if (!activity.isRunning())
                return
            alertDialog.show()

            errorDialogView.tv_msgError.text = errMsg

            errorDialogView.btn_okErrorDialog.onOneClick {
                alertDialog.dismiss()

                if (shouldLogout) {
                    clearUserSavedData()
                    activity.finishAffinity()
                } else {
                    activity.finishAffinity()
                }
            }

            errorDialogView.btn_close_err_dialog.onOneClick {
                if (shouldLogout) {

                    clearUserSavedData()
                    activity.finishAffinity()
                } else {
                    alertDialog.dismiss()
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    activity. startActivity(intent)
//                    clearUserSavedData()
                    activity.finishAffinity()
                }
            }

            /*dialogView.btn_close_dialog.setOnClickListener {
                alertDialog.dismiss()
                finish()
            }*/
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        var cv_backgroundColor: String = "#242424"
        var error_dialog_img: Int = R.drawable.ic_error_svw
        var btnBg: Int = R.drawable.green_button
        var btnText: String = "OK"
        var closeDialogBtn: Int = R.drawable.close_newtork_dialog
    }


}

