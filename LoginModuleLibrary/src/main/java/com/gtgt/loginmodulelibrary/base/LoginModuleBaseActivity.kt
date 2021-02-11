package com.gtgt.loginmodulelibrary.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.github.salomonbrys.kotson.jsonObject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.gtgt.loginmodulelibrary.loginmodule.viewmodel.UserLoginViewModel
import com.gtgt.loginmodulelibrary.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import java.util.*

lateinit var currentActivity: AppCompatActivity

abstract class LoginModuleBaseActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein(LoginModuleApplication.appContext!!)

    private val google_sign_in_req_code = 0//google sign in request code

    private lateinit var googleSignInClient: GoogleSignInClient //google sign in client
    private lateinit var callbackManager: CallbackManager

    private val regViewModel: UserLoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this

        //google and facebook login

        //configure google login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //configure facebook login
        callbackManager = CallbackManager.Factory.create()

        regViewModel.socialLoginDone.observe(this, Observer {
            if (it == LoginModuleConstants.SocialLoginTypes.FACEBOOK.type) {
                LoginManager.getInstance().logOut()
            } else {
                googleSignInClient?.signOut()
            }
        })


    }

    fun fbSignIn(btn_facebook_login: LoginButton) {
        LoginManager.getInstance().logOut()

        var firstName = ""
        var lastName = ""
        var email = ""

        btn_facebook_login.loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
        btn_facebook_login.performClick()

        btn_facebook_login.setReadPermissions(
            Arrays.asList(
                "public_profile",
                "email",
                "first_name",
                "last_name"
            )
        )

        //facebook callback
        btn_facebook_login.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {

                    try {
                        val request = GraphRequest.newMeRequest(
                            result?.accessToken
                        ) { userFbData, response ->

                            val userId = userFbData.getString("id")

                            val profilePicture =
                                "https://graph.facebook.com/$userId/picture?type=large"

                            if (userFbData.has("email"))
                                email = userFbData.getString("email")
                            if (userFbData.has("first_name"))
                                firstName = userFbData.getString("first_name")
                            if (userFbData.has("last_name"))
                                lastName = userFbData.getString("last_name")

                            Log.e("FbData", userFbData.toString())

                            //call facebook login api

                            regViewModel.handleSocialLogin(
                                jsonObject(
                                    "socialId" to userId,
                                    "first_name" to firstName,
                                    "last_name" to lastName,
                                    "email" to email,
                                    "serviceProvider" to getServiceProvider(),
                                    "deviceName" to Build.MODEL,
                                    "osVersion" to Build.VERSION.RELEASE,
                                    "aaid" to LoginModuleApplication.advertise_id,
                                    "product_name" to productName,
                                    "deviceId" to retrievePermanentString("UNIQUE_ID")
                                ),
                                LoginModuleConstants.SocialLoginTypes.FACEBOOK.type
                            )
                        }

                        val parameters = Bundle()
                        parameters.putString("fields", "id,first_name,last_name,email")
                        request.parameters = parameters
                        request.executeAsync()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                    error?.printStackTrace()
                }

            }
        )
    }

    fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, google_sign_in_req_code)
    }

    override fun onResume() {
        super.onResume()
        currentActivity = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        try {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == google_sign_in_req_code) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }

//        if (requestCode == RESOLVE_HINT) {
//            if (resultCode == Activity.RESULT_OK) {
//                val phoneNumber: Credential = data?.getParcelableExtra(Credential.EXTRA_KEY)!!
//                et_enter_mobile_num.setText(phoneNumber.id.removePrefix("+91"))
//                btn_go_nxt.performClick()
//            }
//        }

    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.e("GOOGLE DATA", account.toString())


            if (account != null) {
                regViewModel.handleSocialLogin(
                    jsonObject(
                        "socialId" to account.id,
                        "first_name" to account.displayName,
                        "last_name" to "",
                        "email" to account.email,
                        "pic" to (account.photoUrl?.toString() ?: ""),
                        "product_name" to productName,
                        "deviceId" to retrievePermanentString("UNIQUE_ID")
                    ),
                    LoginModuleConstants.SocialLoginTypes.GOOGLE.type

                )
            } else {
                showSnack("Unable to Login through GOOGLE")
            }


        } catch (e: ApiException) {
            e.printStackTrace()
//            showToast(e.toString())
        }
    }

    companion object {
        var productName: String = ""
        var productType: String = ""
    }


}
