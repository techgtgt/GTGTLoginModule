package com.gtgt.loginmodulelibrary.loginmodule.model

data class ResendOTP(
    val success: Boolean,
    val errorCode: Int,
    val description: String
)