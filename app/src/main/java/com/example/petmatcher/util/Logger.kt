package com.example.petmatcher.util

import android.util.Log
import javax.inject.Inject

/**
 * @author Lisa Watkins
 *
 * Wrapper around [Log] so we can easily mock this class in unit tests.
 */
class Logger @Inject constructor() {
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