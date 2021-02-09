package com.gtgt.loginmodulelibrary.loginmodule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.salomonbrys.kotson.jsonObject
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseViewModel
import com.gtgt.loginmodulelibrary.loginmodule.model.LoginModel
import com.gtgt.loginmodulelibrary.loginmodule.model.SetPasswordModel
import com.gtgt.loginmodulelibrary.utils.execute
import com.gtgt.loginmodulelibrary.utils.retrievePermanentString
import com.gtgt.loginmodulelibrary.utils.retrieveString


class SetPasswordViewModel : LoginModuleBaseViewModel() {
    private val _setPasswordInfo: MutableLiveData<SetPasswordModel> = MutableLiveData()
    val setPasswordInfo: LiveData<SetPasswordModel> = _setPasswordInfo

    fun setPassword(userId: String, password: String, isCreateUser: Boolean = false) {
        if (isCreateUser) {
            apiServicesPlatform.setPassword(
                jsonObject(
                    "userId" to userId,
                    "password" to password,
                    "deviceId" to retrievePermanentString("UNIQUE_ID"),
                    "fcmToken" to retrieveString("FCM_TOKEN"),
                    "product_name" to productName
                )
            ).execute(activity) {
                it.let { response ->
                    _setPasswordInfo.value = response
                }
            }
        } else {
            apiServicesPlatform.resetPassword(
                jsonObject(

                    "userId" to userId,
                    "password" to password
                )
            ).execute(activity) {
                it.let { response ->
                    _setPasswordInfo.value = response
                }
            }
        }
    }

    fun skipPassword(userId: String): LiveData<LoginModel> {
        val skipPwdResponse: MutableLiveData<LoginModel> = MutableLiveData()
        apiServicesPlatform.skipPassword(
            jsonObject(
                "userId" to userId,
                "product_name" to productName
            )
        ).execute {
            skipPwdResponse.value = it
        }
        return skipPwdResponse
    }
}