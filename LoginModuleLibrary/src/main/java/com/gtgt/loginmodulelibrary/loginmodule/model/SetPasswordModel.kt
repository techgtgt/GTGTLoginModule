package com.gtgt.loginmodulelibrary.loginmodule.model

import com.google.gson.JsonElement
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel

data class SetPasswordModel(
    val info: JsonElement
) : LoginModuleBaseModel()