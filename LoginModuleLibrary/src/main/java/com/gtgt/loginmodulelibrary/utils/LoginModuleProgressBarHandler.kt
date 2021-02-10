package com.gtgt.loginmodulelibrary.utils

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.gtgt.loginmodulelibrary.R
import kotlinx.android.synthetic.main.progressbar.view.*

class LoginModuleProgressBarHandler(private val activity: Activity) {
    var handler: Handler? = null
    val hideRunnable = Runnable { hide() }

    init {
        activity.runOnUiThreadIfRunning {
            handler = Handler()
        }
    }

    private lateinit var view: View
    val layout = activity.findViewById<View>(android.R.id.content).rootView as ViewGroup


    fun show() {
        activity.runOnUiThreadIfRunning {
            try {
                handler?.let { it.postDelayed(hideRunnable, 10000) }
                activity.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                view = activity.layoutInflater.inflate(R.layout.progressbar, null)
                view.progress_bar.indeterminateTintList = ColorStateList.valueOf(
                    Color.parseColor(
                        progressBarColor
                    )
                )

                layout.addView(view)
                view.visibility = View.VISIBLE
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hide() {
        activity.runOnUiThreadIfRunning {
            try {
//                handler?.removeCallbacks(hideRunnable)
                handler?.let { it.removeCallbacks(hideRunnable) }
                activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                if (::view.isInitialized)
                    layout.removeView(view)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        var progressBarColor = "#67B648"
    }
}