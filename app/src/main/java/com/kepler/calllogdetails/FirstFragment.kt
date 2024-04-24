package com.kepler.calllogdetails

import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kepler.calllogdetails.databinding.FragmentFirstBinding
import java.util.Date

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val REQUEST_READ_CALL_LOG = 123
    private val PHONE_NUMBERS = arrayOf("198") // Replace with your desired phone numbers

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)





        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // read call log
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_CALL_LOG),
                REQUEST_READ_CALL_LOG
            )
        } else {
            // readCallLogs()

            val callHistory = getCallHistory(requireContext())
            binding.call.append(callHistory)
        }
    }

    fun getCallHistory(context: Context): String {
        val sb = StringBuilder()

        val sortOrder = "${CallLog.Calls.DATE} DESC"


        val contentResolver: ContentResolver = context.contentResolver
        val cursor: Cursor? = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, sortOrder)
        val numberColumn = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
        val typeColumn = cursor?.getColumnIndex(CallLog.Calls.TYPE)
        val dateColumn = cursor?.getColumnIndex(CallLog.Calls.DATE)
        val durationColumn = cursor?.getColumnIndex(CallLog.Calls.DURATION)

        cursor?.use {
            while (cursor.moveToNext()) {
                val phNumber = cursor.getString(numberColumn!!)
                val callDate = Date(cursor.getLong(dateColumn!!))
                val callDuration = cursor.getString(durationColumn!!)
              //  Log.d("fjsdkfjksdf",phNumber.toString())

                val dir: String = when (val callType = typeColumn?.let {
                    cursor.getInt(it) }) {
                    CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> "MISSED"
                    else -> "UNKNOWN"
                }


                sb.append("\nPhone Number:--- $phNumber \nCall Type:--- $dir \nCall Date:--- $callDate \nCall duration in sec:--- $callDuration")
                sb.append("\n----------------------------------")
            }
        }
        return sb.toString()
    }


    /*    private fun readCallLogs() {
            for (phoneNumber in PHONE_NUMBERS) {
                Log.d("checkNumber : ", phoneNumber)

                val projection = arrayOf(
                    CallLog.Calls._ID,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE
                )

                val sortOrder = "${CallLog.Calls.DATE} DESC"


                val selection =
                    "${CallLog.Calls.NUMBER} = ? AND (${CallLog.Calls.TYPE} = ? OR ${CallLog.Calls.TYPE} = ?)"
                val selectionArgs = arrayOf(
                    phoneNumber,
                    CallLog.Calls.OUTGOING_TYPE.toString(),
                    CallLog.Calls.INCOMING_TYPE.toString()
                )

                val cursor = requireActivity().contentResolver.query(
                    CallLog.Calls.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null
                )

                binding.call.append("Call Details :");

                cursor?.use { cursor ->
                    if (cursor.moveToFirst()) {

                        val phNumber  = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                        val callType = cursor.getColumnIndex(CallLog.Calls.TYPE)
                        val callDate  = cursor.getColumnIndex(CallLog.Calls.DATE)
                        val callDayTime = Date(callDate.toLong())
                        val callDuration   = cursor.getColumnIndex(CallLog.Calls.DURATION)

                        val dircode = callType
                        val dir = when (dircode) {
                            CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                            CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                            CallLog.Calls.MISSED_TYPE -> "MISSED"
                            else -> "UNKNOWN"
                        }

                        // Call made with the specified number
                        //'  Toast.makeText(this, "User made the call: $phoneNumber", Toast.LENGTH_SHORT).show()
                        Log.d("UserMadeCall", "Yes $phoneNumber")

                        binding.call.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
                        binding.call.append("\n----------------------------------");


                    } else {
                        // No call made with the specified number
                        Log.d("UserMadeCall", "No $phoneNumber")
                        //      Toast.makeText(this, "User did not make the call: $phoneNumber", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CALL_LOG) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCallHistory(requireContext())
            } else {
                // Permission denied
            }
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}