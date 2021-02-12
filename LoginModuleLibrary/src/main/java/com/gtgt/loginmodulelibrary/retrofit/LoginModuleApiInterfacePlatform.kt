package com.gtgt.loginmodulelibrary.retrofit

import com.google.gson.JsonElement
import com.gtgt.loginmodulelibrary.base.AnyModel
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel
import com.gtgt.loginmodulelibrary.loginmodule.model.CreateUserByMobileResponse
import com.gtgt.loginmodulelibrary.loginmodule.model.LoginModel
import com.gtgt.loginmodulelibrary.loginmodule.model.ResendOTP
import com.gtgt.loginmodulelibrary.loginmodule.model.SetPasswordModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginModuleApiInterfacePlatform {

    //api's related to login


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
