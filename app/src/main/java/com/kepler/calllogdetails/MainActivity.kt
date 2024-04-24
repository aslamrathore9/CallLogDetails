package com.kepler.calllogdetails

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.kepler.calllogdetails.broadCast.MyReceiver
import com.kepler.calllogdetails.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val sharedPreferencesKey = "PHONE_NUMBER"
    private lateinit var sharedPreferences: SharedPreferences
    private var phoneNumber: String? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    private val REQUEST_CODE_PROCESS_OUTGOING_CALLS = 24
    private val REQUEST_CODE_READ_PHONE_STATE = 25


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        sharedPreferences = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)


        /*val r = MyReceiver()
        val i = IntentFilter("android.permission.READ_PHONE_STATE")
        registerReceiver(r, i)*/

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            val phoneNumber = "198"
            sharedPreferences.edit().putString(sharedPreferencesKey, phoneNumber).apply()
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)

        }


        // READ_PHONE_STATE

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                REQUEST_CODE_READ_PHONE_STATE
            )
        } else {
            // Permission is already granted, you can proceed
        }



        // outgoing calls permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.PROCESS_OUTGOING_CALLS)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.PROCESS_OUTGOING_CALLS),
                REQUEST_CODE_PROCESS_OUTGOING_CALLS
            )
        } else {
            Log.d("PermissionOutGoingCall","Permission is already granted")
            // Permission is already granted, you can proceed
        }


    }




   /* private fun readCallLogs() {
        val callLogCursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        callLogCursor?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)

            val cachedName = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberLabel = cursor.getColumnIndex(CallLog.Calls.CACHED_NUMBER_LABEL)
            val dateCall = cursor.getColumnIndex(CallLog.Calls.DATE)
            val getCodedLocation = cursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION)
            val lastModified = cursor.getColumnIndex(CallLog.Calls.LAST_MODIFIED)
            val IS_READ = cursor.getColumnIndex(CallLog.Calls.IS_READ)
            val New = cursor.getColumnIndex(CallLog.Calls.NEW)
            val CACHED_LOOKUP_URI = cursor.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val LAST_MODIFIED = cursor.getColumnIndex(CallLog.Calls.LAST_MODIFIED)

            while (cursor.moveToNext()) {
                val number = cursor.getString(numberIndex)
                val type = cursor.getInt(typeIndex)
                val duration = cursor.getString(durationIndex)


                val cachedName1 = cursor.getString(cachedName)
                val numberLabel1 = cursor.getString(numberLabel)
                val dateCall1 = cursor.getString(dateCall)
                val getCodedLocation1 = cursor.getString(getCodedLocation)
                val lastModified1 = cursor.getString(lastModified)
                val IS_READ1 = cursor.getString(IS_READ)
                val New1 = cursor.getString(New)
                val CACHED_LOOKUP_URI1 = cursor.getString(CACHED_LOOKUP_URI)
                val LAST_MODIFIED1 = cursor.getString(LAST_MODIFIED)

                val callType = when (type) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }

                println("$callType Call: $number, Duration: $duration")
                println("$callType cachedName: $cachedName1, numberLabel: $numberLabel1, dateCall: $dateCall1," +
                        " getCodedLocation: $getCodedLocation1,lastModified: $lastModified1," +
                        "IS_READ: $IS_READ1,New: $New1,CACHED_LOOKUP_URI: $CACHED_LOOKUP_URI1,LAST_MODIFIED: $LAST_MODIFIED1,")
            }
        }
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      if (requestCode == REQUEST_CODE_PROCESS_OUTGOING_CALLS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "OUTGOING Permission granted",Toast.LENGTH_SHORT).show()
                // Permission granted, you can proceed
            } else {
                Toast.makeText(this, "OUTGOING Permission not granted",Toast.LENGTH_SHORT).show()
                // Permission denied, handle accordingly
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
