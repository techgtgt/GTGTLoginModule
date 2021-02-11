package com.gtgt.loginmodulelibrary.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import android.os.StrictMode
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.androidisland.vita.startVita
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.retrofit.LoginModuleApiInterfacePlatform
import com.gtgt.loginmodulelibrary.retrofit.LoginModuleWebServicesPlatform
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

open class LoginModuleApplication : Application(), LifecycleObserver, KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind() from singleton {
            LoginModuleWebServicesPlatform(com.gtgt.loginmodulelibrary.BuildConfig.HOME_URL_PLATFORM).retrofit.create(
                LoginModuleApiInterfacePlatform::class.java
            )
        }

        bind() from singleton {
            this@LoginModuleApplication.applicationContext!!.getSharedPreferences(
                "rummy",
                Context.MODE_PRIVATE
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        startVita()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        instance = this

        appContext = applicationContext

        uniqueId(appContext!!)


        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            Thread {
//                AppManager.sendCrash("", paramThrowable, AppManager.CrashType.OTHER)

                Looper.prepare()
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.app_name) + " has encountered a problem, please restart the app",
                    Toast.LENGTH_LONG
                ).show()
                Looper.loop()
//                AppManager.killApp()
            }.start()
            try {
                Thread.sleep(3000) // Let the Toast display before app will get shutdown
//                AppManager.killApp()
            } catch (e: InterruptedException) {
                e.printStackTrace()
//                AppManager.killApp()
            }
        }

//        getAdId {
//            advertise_id = it!!
//        }


//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            val deviceId = it.token
//            putString("FCM_ID", deviceId)
//            Log.i("fcmId", deviceId)
//        }

        // Branch logging for debugging
//        Branch.enableLogging()

        // Branch object initialization
//        Branch.getAutoInstance(this)
    }

    companion object {
        var isSplashScreenOpen = false

        //for match_remainder_feature
        val TAG = "MyApplication"


        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var instance: LoginModuleApplication? = null
            private set
        var appContext: Context? = null
            private set

        var advertise_id = ""


        val sharedPreferences: SharedPreferences by lazy {
            appContext!!.getSharedPreferences(
                "rummy",
                Context.MODE_PRIVATE
            )
        }

        val sharedPreferencesDontClear: SharedPreferences by lazy {
            appContext!!.getSharedPreferences(
                "rummyPermanent",
                Context.MODE_PRIVATE
            )
        }
    }


}