package com.gtgt.loginmodulelibrary.loginmodule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.salomonbrys.kotson.jsonObject
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseViewModel
import com.gtgt.loginmodulelibrary.loginmodule.model.LoginModel
import com.gtgt.loginmodulelibrary.utils.LoginModuleConstants
import com.gtgt.loginmodulelibrary.utils.execute
import com.gtgt.loginmodulelibrary.utils.retrievePermanentString
import com.gtgt.loginmodulelibrary.utils.retrieveString

class EnterPasswordViewModel : LoginModuleBaseViewModel() {
    private val _loginModel: MutableLiveData<LoginModel> = MutableLiveData()
    val loginModel: LiveData<LoginModel> = _loginModel

    private val _forgotPwdResponse: MutableLiveData<LoginModuleBaseModel> = MutableLiveData()
    val forgotPwdResponse: LiveData<LoginModuleBaseModel> = _forgotPwdResponse

    fun userLogin(userId: String, password: String, otpRequired: Boolean, otp: String) {
        apiServicesPlatform.login(
            jsonObject(
                "userId" to userId,
                "password" to password,
                "deviceId" to retrievePermanentString("UNIQUE_ID"),
                "fcmToken" to retrieveString("FCM_TOKEN"),
                "otp_required" to otpRequired,
                "otp" to otp
            )
        ).execute(activity) {
            _loginModel.value = it
        }
    }

    fun forgotPassword(userId: String) {
        apiServicesPlatform.forgotPassword(
            jsonObject(
                "userId" to userId
            )
        ).execute(activity) {
            _forgotPwdResponse.value = it
        }
    }

    fun loginWithOtp(userId: String): LiveData<LoginModuleBaseModel> {
        val loginWithOtpResponse: MutableLiveData<LoginModuleBaseModel> = MutableLiveData()
        apiServicesPlatform.loginWithOtp(
            jsonObject(
                "user_id" to userId,
                "product" to productName
            )
        ).execute(activity, true) {
            loginWithOtpResponse.value = it
        }

        return loginWithOtpResponse
    }
}
