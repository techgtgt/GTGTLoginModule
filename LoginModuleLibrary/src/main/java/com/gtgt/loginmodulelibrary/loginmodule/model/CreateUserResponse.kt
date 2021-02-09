package com.gtgt.loginmodulelibrary.loginmodule.model

import com.gtgt.loginmodulelibrary.base.LoginModuleBaseModel

class CreateUserResponse(val info: CreateUserResponseInfo) : LoginModuleBaseModel() {

    data class CreateUserResponseInfo(
        val user_unique_id: String,
        val token: String,
        val is_registered_earlier: Boolean,
        val bannerList: List<BannerList>,
        val userInfo: UserInfo
    )

    data class BannerList(
        val bannerId: String,
        val bannerUrl: String,
        val priority: Int
    )


    data class UserInfo(
        val email: String? = "",
        val mobile: String? = "",
        val login_mode: String? = "",
        val username: String? = "",
        val profile_pic: String? = ""

    )


}
