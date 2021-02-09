package com.gtgt.loginmodulelibrary.loginmodule.model

import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel


data class LoginResponse(
    val info: LoginInfo
) : LoginModuleBaseModel()

data class LoginInfo(
    val token: String
)