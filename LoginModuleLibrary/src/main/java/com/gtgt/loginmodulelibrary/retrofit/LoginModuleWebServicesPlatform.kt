package com.gtgt.loginmodulelibrary.retrofit

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.gtgt.loginmodulelibrary.retrofit.SocketFactory.sslSocketFactory
import com.gtgt.loginmodulelibrary.retrofit.SocketFactory.trustManager
import com.gtgt.loginmodulelibrary.utils.LoginModuleConstants
import com.gtgt.loginmodulelibrary.utils.getModel
import com.gtgt.loginmodulelibrary.utils.log
import com.gtgt.loginmodulelibrary.utils.retrievePermanentString
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.android.x.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginModuleWebServicesPlatform(baseUrl: String) {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(1000, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.MINUTES)
        .sslSocketFactory(sslSocketFactory!!, trustManager!!)
        .addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .addHeader(
                    "Authorization",
                    getModel<JsonElement>("loginInfo")?.let {
                        if (BuildConfig.DEBUG) {
                            log("Authorization", "Bearer " + it["token"].string)
                        }

                        when {
                            original.url.toUri().path.contains("userService") -> it["token"].string
                            original.url.toUri().path.contains("createUser") -> ""
                            else -> "Token " + it["token"].string
                        }
                    }
                        ?: ""
                )
                .addHeader(
                    "DeviceId",
                    if (original.url.toUri().path.contains(
                            "resendOtp"
                        )
                    ) {
                        ""
                    } else {
                        retrievePermanentString("UNIQUE_ID") ?: ""
                    }
                )
                .addHeader("SourcePlatform", LoginModuleConstants.HEADER_PRODUCT_NAME)
                .method(original.method, original.body)
                .build()

//            log("AddingAuth", "webServices")

            chain.proceed(request)
        }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ").serializeNulls()
                    .create()
            )
        )
        .client(okHttpClient.apply {
            if (com.gtgt.loginmodulelibrary.BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }.build()).build()
}