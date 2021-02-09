package com.gtgt.loginmodulelibrary.retrofit

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.gtgt.loginmodulelibrary.base.AnyModel
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel
import com.gtgt.loginmodulelibrary.loginmodule.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginModuleApiInterfacePlatform {

    //api's related to login

    @POST("userService/createUser")
    fun createUser(@Body data: JsonObject): Call<CreateUserResponse>

    @POST("userService/createUserByMobile")
    fun createUserByMobile(@Body data: JsonElement): Call<CreateUserByMobileResponse>

    @POST("userService/verifyOtp")
    fun verifyOtp(@Body data: JsonElement): Call<AnyModel>

    @POST("userService/resendOtp")
    fun resendOTP(@Body data: JsonElement): Call<ResendOTP>

    @POST("userService/setPassword")
    fun setPassword(@Body data: JsonElement): Call<SetPasswordModel>

    @POST("userService/resetPassword/")
    fun resetPassword(@Body data: JsonElement): Call<SetPasswordModel>

    @POST("userService/login")
    fun login(@Body data: JsonElement): Call<LoginModel>

    @POST("userService/skipPassword")
    fun skipPassword(@Body data: JsonElement): Call<LoginModel>

    @POST("userService/forgotPassword")
    fun forgotPassword(@Body data: JsonElement): Call<LoginModuleBaseModel>

    @POST("userService/loginOTP")
    fun loginWithOtp(@Body data: JsonElement): Call<LoginModuleBaseModel>

    @POST("userService/facebookLogin")
    fun handleFacebookLogin(@Body data: JsonElement): Call<LoginModel>

    @POST("userService/googleLogin")
    fun handleGoogleLogin(@Body data: JsonElement): Call<LoginModel>

}
