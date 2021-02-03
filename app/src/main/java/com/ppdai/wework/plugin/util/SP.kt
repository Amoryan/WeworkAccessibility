package com.ppdai.wework.plugin.util

import android.content.Context
import android.content.SharedPreferences
import com.ppdai.wework.plugin.BaseApplication


/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class SP private constructor() {

    companion object {
        @JvmStatic
        fun getInstance(): SP {
            return Holder.instance
        }
    }

    private val sp = BaseApplication.appContext.getSharedPreferences("weworkConfig", Context.MODE_PRIVATE)

    fun putBoolean(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).commit()
    }

    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    fun putLong(key: String, value: Long) {
        sp.edit().putLong(key, value).commit()
    }

    fun getLong(key: String): Long {
        return sp.getLong(key, 0L)
    }

    private object Holder {
        val instance = SP()
    }
}