package com.gtgt.loginmodulelibrary.loginmodule.model

import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel


data class CreateUserByMobileResponse(
    val info: CreateUserInfo
) : LoginModuleBaseModel()

data class CreateUserInfo(
    val haspassword: Boolean,
    val user_unique_id: String
)