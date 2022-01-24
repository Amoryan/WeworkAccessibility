package com.ppdai.wework.plugin.util

import android.util.Log
import com.ppdai.wework.plugin.BuildConfig

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
object Logger {

    private const val TAG = "WeworkPlugin"

    @JvmStatic
    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }

    @JvmStatic
    fun e(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message)
        }
    }
}