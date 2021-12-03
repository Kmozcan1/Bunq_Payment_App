package com.kmozcan1.bunqpaymentapp.presentation

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kadir Mert Özcan on 28-Nov-21.
 */

/** Fragment extension to hide the keyboard */
fun Fragment.hideKeyboard() {
    if (activity?.currentFocus != null) {
        val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}

/** Returns formatted date-time */
fun getFormattedDateTime(dateString: String) : String {
    return try {
        val dtf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDateTime = dtf.parse(dateString)
        formattedDateTime?.toString() ?: dateString
    } catch (e: Exception) {
        dateString
    }
}