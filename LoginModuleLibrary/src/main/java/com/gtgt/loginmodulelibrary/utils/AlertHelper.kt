package com.gtgt.loginmodulelibrary.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context

class AlertHelper {
    private var dialog: Dialog? = null

    fun alert(context: Context, dialogTitle: String, buttonTitle: String) {
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(dialogTitle)
            builder.setPositiveButton(buttonTitle) { dialog, which ->
                dialog.dismiss()
            }


            builder.setCancelable(false)
            dialog = builder.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }


    companion object {
        private var mInstance: AlertHelper? = null
        val instance: AlertHelper
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance =
                        AlertHelper()
                }
                return mInstance!!
            }
    }
}