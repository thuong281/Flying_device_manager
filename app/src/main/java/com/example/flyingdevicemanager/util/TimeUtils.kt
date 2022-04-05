package com.example.flyingdevicemanager.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun convertMillisToDate(millis: Long): String {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            return formatter.format(Date(millis))
        }
        
        @SuppressLint("SimpleDateFormat")
        fun convertMillisToDateDetail(millis: Long): String {
            val formatter = SimpleDateFormat("HH:mm - dd/MM/yyyy")
            formatter.timeZone = TimeZone.getTimeZone("GMT+7");
            return formatter.format(Date(millis))
        }
    }
}