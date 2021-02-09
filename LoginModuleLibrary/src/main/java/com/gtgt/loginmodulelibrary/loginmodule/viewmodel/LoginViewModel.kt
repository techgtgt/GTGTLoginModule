package com.gtgt.loginmodulelibrary.loginmodule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonElement
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseViewModel
import com.gtgt.loginmodulelibrary.loginmodule.model.CreateUserByMobileResponse
import com.gtgt.loginmodulelibrary.utils.*

class UserLoginViewModel : LoginModuleBaseViewModel() {

    private var _userMobileCreationResponse: MutableLiveData<CreateUserByMobileResponse> =
        MutableLiveData()
    val userMobileCreationResponse: LiveData<CreateUserByMobileResponse> =
        _userMobileCreationResponse

    private val _socialLoginDone: MutableLiveData<String> = MutableLiveData()
    val socialLoginDone: LiveData<String> = _socialLoginDone


    fun handleSocialLogin(data: JsonElement, type: String) {
        if (type == LoginModuleConstants.SocialLoginTypes.GOOGLE.type) {
            apiServicesPlatform.handleGoogleLogin(data)
        } else {
            apiServicesPlatform.handleFacebookLogin(data)
        }.execute(activity, true) {
            if (it.success) {
                putModel("loginInfo", it.info)
                putString("loginMode", "Social")
                putString("USER_ID", it.info.user_unique_id)
                putBoolean(LoginModuleConstants.IS_USR_LOGGED_IN, true)

                activity?.apply {

//                    activity?.showToast("Successfully Logged in using $type")

                }

                _socialLoginDone.value = type
            } else {
                activity?.showSnack(it.description)
            }
        }

    }


    fun createUserByMobile(userMobileNumber: String) {
        val payload = jsonObject(
            "mobile" to userMobileNumber,
            "deviceId" to retrievePermanentString("UNIQUE_ID"),
            "fcmToken" to retrieveString("FCM_ID"),
            "productName" to productName
        )

        apiServicesPlatform.createUserByMobile(payload).execute(activity, true) {
            _userMobileCreationResponse.value = it
        }

    }

}