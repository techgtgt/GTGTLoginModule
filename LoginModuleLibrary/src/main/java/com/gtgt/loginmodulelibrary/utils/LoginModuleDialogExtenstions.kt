package com.gtgt.loginmodulelibrary.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonElement
import com.gtgt.loginmodulelibrary.R
import kotlinx.android.synthetic.main.network_error_dialog.view.*
import kotlinx.android.synthetic.main.server_error_dialog.view.*
import okhttp3.Request
import okio.Buffer
import org.kodein.di.android.x.BuildConfig

class ShowServerErrorDialog(
    var activity: Activity,
    private val requestInfo: Request,
    private val message: String? = null

) {
    fun showServerErrorDialog() {
        try {
            if (!activity.isRunning())
                return
            val dialogView =
                LayoutInflater.from(activity).inflate(R.layout.server_error_dialog, null)
            dialogView.cv_dialog_bg.setCardBackgroundColor(Color.parseColor(cv_backgroundColor))
            dialogView.iv_server_error.setImageResource(error_dialog_img)
            dialogView.btn_ok.background = activity.getDrawable(btnBg)
            dialogView.btn_ok.text = btnText
            dialogView.btn_closeSD.setImageResource(closeDialogBtn)
            val builder = AlertDialog.Builder(activity)
            builder.setView(dialogView)

            val alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            if (!activity.isRunning())
                return
            alertDialog.show()

            if (!BuildConfig.DEBUG) {
                dialogView.btn_ok.onOneClick {
                    alertDialog.dismiss()
                }
            } else {
                dialogView.btn_ok.text = "Share Error"
                val buffer = Buffer()
                requestInfo.body?.writeTo(buffer)
                val payload = buffer.readUtf8()

                val url = requestInfo.url.toString()

                message?.let { dialogView.messageError.text = "$url\n$it" }

                dialogView.btn_ok.onOneClick {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            """
                        Url: $url,
                        
                        Method: ${requestInfo.method},
                         
                        Authorization: ${getModel<JsonElement>("loginInfo")?.let {
                                if (BuildConfig.DEBUG) {
                                    log("Authorization", "Bearer " + it["token"].string)
                                }
                                it["token"].string
                            }
                                ?: ""}
                        
                        DeviceId: ${retrievePermanentString("UNIQUE_ID")}

                        ${if (payload.isEmpty()) "" else "Payload: $payload"}

                        Response: $message
                    """.trimIndent()
                        )
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    activity.startActivity(shareIntent)

                    alertDialog.dismiss()
                }
            }

            dialogView.btn_closeSD.onOneClick {
                alertDialog.dismiss()
            }


        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    companion object {
        var cv_backgroundColor: String = "#242424"
        var error_dialog_img: Int = R.drawable.ic_server_down
        var btnBg: Int = R.drawable.green_button
        var btnText: String = "OK"
        var closeDialogBtn: Int = R.drawable.close_newtork_dialog
    }

}


class ShowNetworkErrDialog(
    var activity: Activity
) {
    fun networkErrorDialog(closeActivity: Boolean = true, refresh: () -> Unit) {
        runOnMain {
            try {
                if (!activity.isRunning())
                    return@runOnMain
                val dialogView =
                    LayoutInflater.from(activity).inflate(R.layout.network_error_dialog, null)

                dialogView.cv_dialog_background.setCardBackgroundColor(
                    Color.parseColor(
                        cv_backgroundColor
                    )
                )
                dialogView.iv_err_img.setImageResource(error_dialog_img)
                dialogView.btn_retry.background = activity.getDrawable(btnBg)
                dialogView.btn_retry.text = btnText
                dialogView.btn_close.setImageResource(closeDialogBtn)

                //Now we need an AlertDialog.Builder object
                val builder = AlertDialog.Builder(activity)
                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView)

                //finally creating the alert dialog and displaying it
                val alertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                if (!activity.isRunning())
                    return@runOnMain
                alertDialog.show()

                dialogView.btn_close.onOneClick {
                    alertDialog.dismiss()
                    if (closeActivity) {
                        activity.finish()
                    }
                }
                dialogView.btn_retry.onOneClick {
                    refresh()
                    alertDialog.dismiss()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    companion object {
        var cv_backgroundColor: String = "#242424"
        var error_dialog_img: Int = R.drawable.ic_network_error
        var btnBg: Int = R.drawable.green_button
        var btnText: String = "Retry"
        var closeDialogBtn: Int = R.drawable.close_newtork_dialog
    }


}

