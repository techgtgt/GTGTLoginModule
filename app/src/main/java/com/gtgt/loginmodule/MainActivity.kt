package com.gtgt.loginmodule

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginmodule.R
import com.gtgt.loginmodulelibrary.base.LoginModuleBaseViewModel
import com.gtgt.loginmodulelibrary.loginmodule.view.RegistrationActivity
import com.gtgt.loginmodulelibrary.utils.LoginModuleConstants
import com.gtgt.loginmodulelibrary.utils.launchActivity
import com.gtgt.loginmodulelibrary.utils.onOneClick
import com.gtgt.loginmodulelibrary.utils.putString
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoginModuleBaseViewModel.productName = "SUPERLIT"

        btn_login.onOneClick {
            launchActivity<RegistrationActivity>(
                LoginModuleConstants.LOGIN_MODEL_REQUEST_CODE
            ) {
                putExtra("PRODUCT_TYPE", LoginModuleConstants.ProductName.superLit.type)
                putString("PRODUCT_NAME", "SUPERLIT")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == LoginModuleConstants.LOGIN_MODEL_REQUEST_CODE) {
            Toast.makeText(this, "SUCCESS FULLY LOGGED IN", Toast.LENGTH_SHORT).show()
        }


    }
}