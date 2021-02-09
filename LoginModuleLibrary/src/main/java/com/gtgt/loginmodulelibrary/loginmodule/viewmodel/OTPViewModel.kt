package com.gtgt.loginmodulelibrary.loginmodule.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.gtgt.loginmodulelibrary.base.AnyModel
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseViewModel
import com.gtgt.loginmodulelibrary.utils.execute
import com.gtgt.loginmodulelibrary.utils.showSnack

class OTPViewModel : LoginModuleBaseViewModel() {

    private var _verifyOtpResponse: MutableLiveData<AnyModel> = MutableLiveData()
    val verifyOtpResponse: LiveData<AnyModel> = _verifyOtpResponse
    fun verifyOtp(otp: String, user_unique_id: String, mobile_number: String) {
        val jsonInput = JsonObject()
        jsonInput.addProperty("otp", otp)
        jsonInput.addProperty("data", mobile_number)
        jsonInput.addProperty("userId", user_unique_id)
        apiServicesPlatform.verifyOtp(jsonInput).execute(activity, true) {
            _verifyOtpResponse.value = it
        }
    }

    fun resendOtp(user_unique_id: String, isUserRegistered: Boolean) {
        val jsonInput = JsonObject()
        jsonInput.addProperty("userId", user_unique_id)
        jsonInput.addProperty("productName", productName)
        apiServicesPlatform.resendOTP(jsonInput).execute(activity, true) {
            activity?.showSnack(it.description)
        }
    }
}