package com.ppdai.wework.plugin.util

import android.util.Log

/**
 * @author sunshine big boy
 */
object Logger {

    private const val TAG = "WeworkPlugin"

    @JvmStatic
    fun d(message: String) {
        Log.d(TAG, message)
    }

    @JvmStatic
    fun e(message: String) {
        Log.d(TAG, message)
    }
}