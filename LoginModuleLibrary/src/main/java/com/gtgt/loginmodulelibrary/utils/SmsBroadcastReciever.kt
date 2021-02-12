package com.gtgt.loginmodulelibrary.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        context.showToast("action: " + (intent.action ?: "null"))

        if (intent.action === SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            val smsRetrieverStatus =
                extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (smsRetrieverStatus!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                }
                CommonStatusCodes.TIMEOUT -> {
                }
            }
        }
    }

    interface SmsBroadcastReceiverListener {
        fun onSuccess(message: String?)
        fun onFailure()
    }
}