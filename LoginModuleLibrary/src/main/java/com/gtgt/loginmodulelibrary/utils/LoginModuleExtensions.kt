package com.gtgt.loginmodulelibrary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.gtgt.loginmodulelibrary.BuildConfig
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseActivity
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseViewModel
import com.gtgt.utils.OnOneClickListener
import kotlinx.android.synthetic.main.snack_view.view.*
import retrofit2.Call
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.regex.Pattern
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import kotlin.concurrent.thread

val gson = Gson()

fun Any.toJsonTree() = gson.toJsonTree(this)


fun retrievePermanentString(key: String, default: String = ""): String {
    val sp = LoginModuleApplication.sharedPreferencesDontClear
    return sp.getString(key, default)!!
}

fun putPermanentString(key: String, value: String) {
    val sp = LoginModuleApplication.sharedPreferencesDontClear
    sp.edit().putString(key, value).commit()
}

inline fun <reified T> getModel(key: String): T? where T : Any {
    val sp = LoginModuleApplication.sharedPreferences
    return sp.getString(key, null)?.let { gson.fromJson(it) }
}


inline fun <reified VM : LoginModuleBaseViewModel, T> T.viewModel(args: JsonElement? = null): Lazy<VM> where T : LoginModuleBaseActivity {
    return lazy {
        ViewModelProvider(this, ViewModelWithArgumentsFactory(args)).get(VM::class.java)
            .apply {
                activity = this@viewModel
                context = this@viewModel
            }
    }
}


fun View.onOneClick(time: Long = 1000, callback: () -> Unit) {
    //if (background != null)
//    background = getAdaptiveRippleDrawable(background)
    setOnClickListener(object : OnOneClickListener(time) {
        override fun onOneClick(v: View) {
            callback()
        }
    })
}

fun TextView.makeTextUnderline() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

//to launch any activity
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

//to show snack bar
fun Activity.showSnack(message: String) {
    runOnMain {
        if (isRunning()) {
            var isClosed = false
            var isGame = false

            val rootView = findViewById<View>(android.R.id.content).rootView as ViewGroup
            val snackView = layoutInflater.inflate(R.layout.snack_view, null)
            rootView.findViewWithTag<View>("snack")?.let {
                it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.top_exit))
                rootView.removeView(it)
            }

            snackView.message_tv.text = message
            rootView.addView(snackView)

            snackView.mainView.padding(top = getStatusBarHeight())

            snackView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.top_enter))

            Handler().postDelayed({
                if (!isClosed && isRunning() && snackView.isAttachedToWindow)
                    snackView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.top_exit))
                rootView.removeView(snackView)
            }, (if (isGame) 3000L else 5000L))
        }
    }
}

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

private val mainHandler = Handler(Looper.getMainLooper())

fun runOnMain(code: () -> Unit) {
    mainHandler.post {
        code()
    }
}

fun Activity.runOnUiThreadIfRunning(code: () -> Unit) {
    if (isRunning()) {
        Handler(Looper.getMainLooper()).post {
            code()
        }
    }
}

fun Activity?.isRunning(): Boolean = if (this == null) false else !(isDestroyed || isFinishing)

fun View.padding(all: Int = 0, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): View {
    if (all != 0) {
        setPadding(all, all, all, all)
    } else {
        setPadding(left, top, right, bottom)
    }
    return this
}

fun Context.showToast(message: String) {
    runOnMain { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
}

fun Activity.setStatusBarColor(color: Int) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    } else {

    }
}

fun String.isValidPhoneNumber(): Boolean {
    return (android.util.Patterns.PHONE.matcher(
        this
    ).matches() && this.length == 10 && this.startsWith("6") || this.startsWith("7") ||
            this.startsWith("8") || this.startsWith("9"))
}


fun Activity.getServiceProvider(): String {
    val telephonyManager = (this.getSystemService(Context.TELEPHONY_SERVICE)) as TelephonyManager
//    val operatorName = telephonyManager.networkOperatorName
    return telephonyManager.networkOperatorName
}


//method to handle api calls
fun <T> Call<T>.execute(
    activity: LoginModuleBaseActivity? = null,
    showLoading: Boolean = true,
    callback: (T) -> Unit
) {
    val progressBarHandler: LoginModuleProgressBarHandler? =
        activity?.let { if (showLoading) LoginModuleProgressBarHandler(activity) else null }
    thread {
        runOnMain {
            progressBarHandler?.show()
        }
        if (activity != null) {
            (activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo.let { networkInfo ->
                    if (networkInfo == null || !networkInfo.isConnected) {
                        Thread.sleep(500)
                        progressBarHandler?.hide()

                        ShowNetworkErrDialog(
                            activity as Activity
                        ).networkErrorDialog(

                        ) {
                            Handler().postDelayed({
                                execute(activity, showLoading, callback)
                                progressBarHandler?.hide()
                            }, 200)
                        }

                        return@thread
                    }
                }
        }

        val requestInfo = request()

        try {
            val response = execute()

            val code = response.code()

            val headers = response.headers()

            val result: T?

            if (code == 400 || code == 404 || code == 500) {
                val url = requestInfo.url.toString()

//                activity?.showSnack(
//                    "Error in ${url.split("/").let {
//                        val l = it.last()
//                        if (l.isEmpty()) {
//                            it[it.size - 2]
//                        } else {
//                            l
//                        }
//                    }}"
//                )

            }


            when (code) {
                400 -> {
                    runOnMain {
                        progressBarHandler?.hide()
                        ShowServerErrorDialog(
                            activity as Activity,
                            requestInfo,
                            "400 error\n ${response.errorBody()?.string() ?: ""}"
                        ).showServerErrorDialog()
                    }
                    result = null
                }
                500 -> {
                    runOnMain {
                        progressBarHandler?.hide()
                        ShowServerErrorDialog(
                            activity as Activity,
                            requestInfo,
                            "500 error"
                        ).showServerErrorDialog()
                    }
                    result = null
                }
                in 401..404 -> {

                    when (code) {
                        403, 401 -> {
                            runOnMain {
                                val error =
                                    response.errorBody()?.string()?.toJsonTree()!!["detail"].string
                                progressBarHandler?.hide()
                                ShowErrorDialog(
                                    activity!!,
                                    error,
                                    true
                                ).showErrorDialog()
                            }
                        }
                        else -> {
                            response.errorBody()?.string()?.toJsonTree()?.obj?.let {
                                if (it.has("error")) {
                                    it.get("error")?.let {
                                        if (it.string == "insufficient_scope" || it.string == "invalid_token" || it.string.contains(
                                                "unauthorized"
                                            )
                                        ) {
//                                            logout(activity)
                                        } else {
                                            runOnMain {
                                                progressBarHandler?.hide()

                                                ShowServerErrorDialog(
                                                    activity as Activity,
                                                    requestInfo,
                                                    "$it"
                                                ).showServerErrorDialog()

                                            }
                                        }
                                    } ?: runOnMain {
                                        progressBarHandler?.hide()
                                        ShowServerErrorDialog(
                                            activity as Activity,
                                            requestInfo,
                                            "$it"
                                        ).showServerErrorDialog()
                                    }

                                } else {
                                    progressBarHandler?.hide()
                                    ShowServerErrorDialog(
                                        activity as Activity,
                                        requestInfo,
                                        "$it"
                                    ).showServerErrorDialog()
                                }
                            } ?: run {
                                progressBarHandler?.hide()
                                ShowServerErrorDialog(
                                    activity as Activity,
                                    requestInfo,
                                    "Error code: $code"
                                ).showServerErrorDialog()
                            }
                        }
                    }
                    result = null
                }
                in 200..299 -> {
                    val body = response.body()

                    result = if (body == null) {
                        runOnMain {
                            progressBarHandler?.hide()
                            ShowServerErrorDialog(
                                activity as Activity,
                                requestInfo,
                                "Body is null"
                            ).showServerErrorDialog()

                        }
                        null
                    } else {
                        body
                    }
                }
                else -> {
                    runOnMain {
                        progressBarHandler?.hide()
                        ShowServerErrorDialog(
                            activity as Activity,
                            requestInfo,
                            "Error code: $code"
                        ).showServerErrorDialog()
                    }
                    result = null
                }
            }

            runOnMain {
                calculateDiffrence(headers.getDate("Date")!!.time)

                progressBarHandler?.hide()
                result?.let {
                    try {
                        callback(it)
                    } catch (ex: Exception) {
                        val url = requestInfo.url.toString()

                        val snack = "Error in ${url.split("/").let {
                            val l = it.last()
                            if (l.isEmpty()) {
                                it[it.size - 2]
                            } else {
                                l
                            }
                        }}"


//                        activity?.showSnack(snack)

                    }
                }
            }

        } catch (e: SocketTimeoutException) {

            ShowNetworkErrDialog(activity as Activity).networkErrorDialog() {

                Handler().postDelayed({
                    execute(activity, showLoading, callback)
                    progressBarHandler?.hide()
                }, 200)
            }


        } catch (e: SSLHandshakeException) {
            ShowNetworkErrDialog(activity as Activity).networkErrorDialog() {

                Handler().postDelayed({
                    execute(activity, showLoading, callback)
                    progressBarHandler?.hide()
                }, 200)
            }
        } catch (e: SSLException) {
            ShowNetworkErrDialog(activity as Activity).networkErrorDialog() {

                Handler().postDelayed({
                    execute(activity, showLoading, callback)
                    progressBarHandler?.hide()
                }, 200)
            }
        } catch (e: UnknownHostException) {
            ShowNetworkErrDialog(activity as Activity).networkErrorDialog() {

                Handler().postDelayed({
                    execute(activity, showLoading, callback)
                    progressBarHandler?.hide()
                }, 200)
            }
        } catch (e: ConnectException) {
            ShowNetworkErrDialog(activity as Activity).networkErrorDialog() {

                Handler().postDelayed({
                    execute(activity, showLoading, callback)
                    progressBarHandler?.hide()
                }, 200)
            }
        } catch (e: IllegalStateException) {
            /*activity?.networkErrorDialog {
                Handler().postDelayed({
                    execute(activity, showLoading, callback)
                    progressBarHandler?.hide()
                }, 200)
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
            runOnMain {
                progressBarHandler?.hide()
                ShowServerErrorDialog(
                    activity as Activity,
                    requestInfo,
                    e.message
                ).showServerErrorDialog()
            }

            /*activity?.networkErrorDialog {
                progressBarHandler?.hide()
                execute(activity, showLoading, callback)
            }*/
        }
        runOnMain {
            progressBarHandler?.hide()
        }
    }
}

fun clearUserSavedData() {
    LoginModuleApplication.sharedPreferences.edit().clear().apply()
}

var timeDiffWithServer = 0L
fun calculateDiffrence(serverTime: Long) {
    timeDiffWithServer = System.currentTimeMillis() - serverTime
}

//Shared prefs related methods
fun putModel(key: String, value: Any, serializableNulls: Boolean = false) {
    val json = gson.toJson(value)
    val sp = LoginModuleApplication.sharedPreferences
    sp.edit().putString(key, json).commit()
}

fun putString(key: String, value: String) {
    val sp = LoginModuleApplication.sharedPreferences
    sp.edit().putString(key, value).commit()
}

fun putBoolean(key: String, value: Boolean) {
    val sp = LoginModuleApplication.sharedPreferences
    sp.edit().putBoolean(key, value).commit()
}


fun retrieveString(key: String, default: String = ""): String {
    val sp = LoginModuleApplication.sharedPreferences
    return sp.getString(key, default)!!
}


fun String.isValidPassword(): Boolean {
    return Pattern.matches("((?=.*[a-z])(?=.*\\d)(?=.*[@#$%!]).{6,20})", this)
}


@SuppressLint("HardwareIds")
fun uniqueId(applicationContext: Context) {
    val unique_id = Settings.Secure.getString(
        applicationContext.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    Log.e("Device_Id", unique_id)
    putPermanentString("UNIQUE_ID", unique_id)
}

fun log(tag: String, message: String = "__") {
    if (BuildConfig.DEBUG) {
        Log.i(tag, message)
    }
}

fun View.convertToDp(value: Int): Int {

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources
            .displayMetrics
    ).toInt()
}


fun View.margins(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): View {


    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(
            this.convertToDp(left),
            this.convertToDp(top),
            this.convertToDp(right),
            this.convertToDp(bottom)
        )
        requestLayout()
    }
    return this
}

fun retrieveBoolean(key: String, default: Boolean = false): Boolean {
    val sp = LoginModuleApplication.sharedPreferences
    return sp.getBoolean(key, default)
}






