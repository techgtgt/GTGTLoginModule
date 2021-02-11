package com.gtgt.loginmodulelibrary.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.gtgt.loginmodulelibrary.retrofit.LoginModuleApiInterfacePlatform
import com.gtgt.loginmodulelibrary.utils.LoginModuleApplication
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

abstract class LoginModuleBaseViewModel : ViewModel(), KodeinAware {
    override val kodein by kodein(LoginModuleApplication.appContext!!)

    var activity: LoginModuleBaseActivity? = null
    var context: Context? = null
    protected val apiServicesPlatform: LoginModuleApiInterfacePlatform by instance()

    companion object {
        var productName: String = ""
    }

}