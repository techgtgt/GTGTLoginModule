package com.gtgt.loginmodulelibrary.base

import com.google.gson.JsonElement


open class LoginModuleBaseModel {
    val description: String = ""
    val errorCode: Int = 0
    var success: Boolean = false
}

data class AnyModel(val info: JsonElement) : LoginModuleBaseModel()