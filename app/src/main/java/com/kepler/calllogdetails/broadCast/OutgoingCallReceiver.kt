package com.kepler.calllogdetails.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class OutgoingCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)

            Toast.makeText(context, "$phoneNumber", Toast.LENGTH_SHORT).show()
            val sharedPreferencesKey = "PHONE_NUMBER"
            val storedNumber = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).getString(sharedPreferencesKey, "")

            if (phoneNumber == storedNumber) {
                Log.d("User has started call", "CRMCallo $phoneNumber")
                try {
                    val tmgr =
                        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val phoneListener = MyPhoneStateListener()
                    tmgr.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)
                } catch (e: Exception) {
                    Toast.makeText(context, "oops!", Toast.LENGTH_SHORT).show()
                }

            } else {
                Log.d("User has started call", "OwnCall $phoneNumber")
            }
        }
    }


    inner class MyPhoneStateListener() : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String) {
            println("phoneNumber : $phoneNumber")
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    val msg = "Incoming Number: $phoneNumber"
                    Log.d("CallState", msg)
                }

                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    val msg = "Outgoing Number: $phoneNumber "
                    Log.d("CallState", msg)
                }

                TelephonyManager.CALL_STATE_IDLE -> {
                    val msg = "End Call: $phoneNumber "
                    Log.d("CallState", msg)
                }
            }
        }
    }


}
