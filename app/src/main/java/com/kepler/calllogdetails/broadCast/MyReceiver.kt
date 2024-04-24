package com.kepler.calllogdetails.broadCast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {
    private lateinit var context: Context

    override fun onReceive(context: Context, intent: Intent) {
      //  if (intent.action == Intent.ACTION_NEW_OUTGOING_CALL) {
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            Log.d("PhoneNumber", phoneNumber.toString())
            try {
                val tmgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val phoneListener = MyPhoneStateListener(phoneNumber.toString())
                tmgr.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)
            } catch (e: Exception) {
                Toast.makeText(context, "oops!", Toast.LENGTH_SHORT).show()
            }
     //   }
    }

    inner class MyPhoneStateListener(phoneNumber: String) : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    val msg = "This is phone call. Incoming Number: $incomingNumber"
                    Log.d("CallState",msg)
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(context, msg, duration)
                    toast.show()
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    val msg = "This is phone call. Outgoing Number: $ "
                    Log.d("CallState",msg)
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(context, msg, duration)
                    toast.show()
                }
            }
        }
    }
}
