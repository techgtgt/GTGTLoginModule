package com.gtgt.loginmodulelibrary.loginmodule.model

import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel


class LoginModel(val info: LoginInfo) : LoginModuleBaseModel() {
    data class LoginInfo(
        val user_unique_id: String,
        val token: String
    )
}