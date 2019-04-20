package com.example.petmatcher.util

import android.util.Log
import javax.inject.Singleton

@Singleton
class Logger {
    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}