package com.gtgt.loginmodulelibrary.loginmodule.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gtgt.loginmodulelibrary.R
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseActivity
import com.gtgt.loginmodulelibrary.loginmodule.model.CreateUserInfo
import com.gtgt.loginmodulelibrary.loginmodule.model.SetPasswordModel
import com.gtgt.loginmodulelibrary.loginmodule.viewmodel.EnterPasswordViewModel
import com.gtgt.loginmodulelibrary.loginmodule.viewmodel.OTPViewModel
import com.gtgt.loginmodulelibrary.loginmodule.viewmodel.SetPasswordViewModel
import com.gtgt.loginmodulelibrary.loginmodule.viewmodel.UserLoginViewModel
import com.gtgt.loginmodulelibrary.utils.*

class RegistrationActivity : LoginModuleBaseActivity() {

//    private val productType by lazy { intent.getStringExtra("PRODUCT_TYPE") ?: "" }

    var resultIntent = Intent()

    private val regViewModel: UserLoginViewModel by viewModel()
    private val enterPasswordViewModel: EnterPasswordViewModel by viewModel()
    private val otpViewModel: OTPViewModel by viewModel()
    private val setPasswordViewModel: SetPasswordViewModel by viewModel()
    var userMobileNum = ""
    lateinit var userLoginInfo: CreateUserInfo
    lateinit var loginBottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    lateinit var enterPwdBottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    lateinit var setPwdBottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    lateinit var otpBottomSheetBehaviour: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initUi()

        UserLoginPopup(this).showUserLoginDialog(productType, { mobileNum, bottomSheetBehaviour ->
            loginBottomSheetBehavior = bottomSheetBehaviour
            userMobileNum = mobileNum
            callCreateUserByMobileApi(mobileNum)

        }, { bottomSheet, fbLoginBtn ->
            loginBottomSheetBehavior = bottomSheet
            fbSignIn(fbLoginBtn)
        }, {
            googleSignIn()
        })

        regViewModel.userMobileCreationResponse.observe(this, Observer { loginInfo ->
            if (loginInfo.success) {
                userLoginInfo = loginInfo.info
                if (loginInfo.info.haspassword) {
                    loginBottomSheetBehavior.peekHeight = 0
                    EnterPasswordPopup(this).showEnterPwdDialog(
                        userMobileNum,
                        productType,
                        {
                            //login Btn clicked
                            callLoginApi(loginInfo.info.user_unique_id, it)
                        },
                        {
                            //forgot btn  clicked
                            enterPwdBottomSheetBehavior = it
                            enterPasswordViewModel.forgotPassword(loginInfo.info.user_unique_id)
                        },
                        {
                            //login with otp clicked
                            enterPwdBottomSheetBehavior = it
                            enterPasswordViewModel.loginWithOtp(
                                loginInfo.info.user_unique_id
                            )
                                .observe(this, Observer {
                                    if (it.success) {
                                        enterPwdBottomSheetBehavior.peekHeight = 0
                                        OtpVerificationPopup(this).showOtpVerificationDialog(
                                            true,
                                            userMobileNum,
                                            productType,
                                            { otp, b ->
                                                //verify btn clicked
                                                enterPasswordViewModel.userLogin(
                                                    loginInfo.info.user_unique_id,
                                                    "",
                                                    true,
                                                    otp = otp
                                                )
                                                otpBottomSheetBehaviour = b
                                            }, { bottomSheetBehavior, isUserRegistered ->
                                                otpBottomSheetBehaviour = bottomSheetBehavior
                                                //resend otp btn clicked
                                                otpViewModel.resendOtp(
                                                    loginInfo.info.user_unique_id,
                                                    isUserRegistered
                                                )

                                            }, {
                                                //previous btn clicked
                                                enterPwdBottomSheetBehavior.peekHeight =
                                                    BottomSheetBehavior.PEEK_HEIGHT_AUTO

                                            }
                                        )

                                    }
                                })
                        }, {
                            //previous btn clicked
                            loginBottomSheetBehavior.peekHeight =
                                BottomSheetBehavior.PEEK_HEIGHT_AUTO
                        })

                } else {
                    hideKeyboard()
                    loginBottomSheetBehavior.peekHeight = 0
                    OtpVerificationPopup(this).showOtpVerificationDialog(
                        false,
                        userMobileNum,
                        productType,
                        { otp, bottomSheet ->
                            otpBottomSheetBehaviour = bottomSheet
                            otpViewModel.verifyOtp(otp, userLoginInfo.user_unique_id, userMobileNum)
                        }, { bottomSheetBehavior, isUserRegistered ->
                            otpBottomSheetBehaviour = bottomSheetBehavior
                            //resend otp btn clicked
                            otpViewModel.resendOtp(
                                loginInfo.info.user_unique_id,
                                isUserRegistered
                            )

                        }, {
                            loginBottomSheetBehavior.peekHeight =
                                BottomSheetBehavior.PEEK_HEIGHT_AUTO
                        }
                    )

                }

            } else {
                showSnack(loginInfo.description)

            }
        })

        enterPasswordViewModel.loginModel.observe(this, Observer {
            if (it.success) {
                putModel("loginInfo", it.info)
                putBoolean(LoginModuleConstants.IS_USR_LOGGED_IN, true)
                setResult(LoginModuleConstants.LOGIN_MODEL_REQUEST_CODE)
//                showToast("You are Successfully Logged in..")
                finish()

            } else {
                showSnack(it.description)
            }
        })

        enterPasswordViewModel.forgotPwdResponse.observe(this, Observer {
            //forgot pwd btn clicked flow
            if (it.success) {
                enterPwdBottomSheetBehavior.peekHeight = 0
                OtpVerificationPopup(this).showOtpVerificationDialog(
                    true,
                    userMobileNum,
                    productType,
                    { otp, bottomSheet ->
                        otpBottomSheetBehaviour = bottomSheet
                        otpViewModel.verifyOtp(otp, userLoginInfo.user_unique_id, userMobileNum)
                    },
                    { bottomSheetBehavior, isUserRegistered ->
                        otpBottomSheetBehaviour = bottomSheetBehavior
                        //resend otp btn clicked
                        otpViewModel.resendOtp(
                            userLoginInfo.user_unique_id,
                            isUserRegistered
                        )
                    }, {
                        enterPwdBottomSheetBehavior.peekHeight =
                            BottomSheetBehavior.PEEK_HEIGHT_AUTO
                    }

                )
            } else {
                showSnack(it.description)
            }


        })

        otpViewModel.verifyOtpResponse.observe(this, Observer {
            Log.e("verifyOtpResponse", it.toString())
            if (it.success) {
                otpBottomSheetBehaviour.peekHeight = 0
                SetPasswordPopup(this).showSetPwdDialog(
                    userLoginInfo.user_unique_id,
                    productType,
                    { password, userUniqueId, isCreateUser ->
                        setPasswordViewModel.setPassword(
                            password = password,
                            userId = userUniqueId,
                            isCreateUser = isCreateUser
                        )
                    }
                    , {
                        //skip pwd btn clicked
                        setPasswordViewModel.skipPassword(it).observe(this, Observer {
                            if (it.success) {
                                putModel("loginInfo", it.info)
                                putString("USER_ID", it.info.user_unique_id)
                                putBoolean(LoginModuleConstants.IS_USR_LOGGED_IN, true)
//                                showToast("SUCCESSFULLY LOGGED IN TO THE APP")
                                setResult(LoginModuleConstants.LOGIN_MODEL_REQUEST_CODE)
                                finish()
                            }
                        })
                    }
                )

            } else {
                showSnack(it.description)

            }
        })

        setPasswordViewModel.setPasswordInfo.observe(this, Observer {
            it?.let {
                onPasswordChangeSuccess(it)
            }
        })


    }


    private fun callCreateUserByMobileApi(mobileNum: String) {
        if (mobileNum.isNotEmpty()) {
            if (mobileNum.isValidPhoneNumber()) {
                regViewModel.createUserByMobile(mobileNum)
            } else {
                showSnack("You have entered invalid mobile number please check")
            }
        } else {
            showSnack("Please Enter Valid Mobile Number to Proceed")
        }


    }

    private fun initUi() {
        supportActionBar?.hide()
        setStatusBarColor(android.R.color.transparent)
    }

    private fun callLoginApi(user_unique_id: String, enteredPwd: String) {
        enterPasswordViewModel.userLogin(
            user_unique_id,
            enteredPwd,
            false,
            ""
        )
    }

    private fun onPasswordChangeSuccess(setPasswordModel: SetPasswordModel) {
        if (setPasswordModel.success) {
            if (setPasswordModel.info["token"].isJsonNull) {
//                showSnack("Some error occured")
            } else {
                putModel("loginInfo", setPasswordModel.info)
                putString("USER_ID", setPasswordModel.info["user_unique_id"].string)
                putBoolean(LoginModuleConstants.IS_USR_LOGGED_IN, true)
//                showToast("You are successfully logged into the app")
                setResult(LoginModuleConstants.LOGIN_MODEL_REQUEST_CODE)
                finish()

            }
        } else {
            AlertHelper.instance.alert(this, setPasswordModel.description, "OK")
        }

    }

}

