package com.gtgt.loginmodule.model

import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel


data class VerifyOtpResponse(
    val info: VerifyOTPInfo
): LoginModuleBaseModel()

data class VerifyOTPInfo(
    val user_unique_id: String
)