package com.gtgt.loginmodulelibrary.utils

object LoginModuleConstants {

    const val PRODUCT_NAME = "RUMMYJACKS"


    var HEADER_PRODUCT_NAME = "SUPERLIT_RUMMYJACKS"

    //request codes for different screens
    const val LOGIN_MODEL_REQUEST_CODE = 100

    //shared prefs names
    const val IS_USR_LOGGED_IN = "IS_USER_LOGGED_IN"

    enum class SocialLoginTypes(val type: String) {
        GOOGLE("google"),
        FACEBOOK("facebook"),
    }


    enum class ProductName(val type: String) {
        rummyJacks("RUMMYJACKS"),
        superLit("SUPERLIT")
    }


}