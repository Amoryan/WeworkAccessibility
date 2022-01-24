package com.ppdai.wework.plugin.util

import android.content.Context
import com.ppdai.wework.plugin.BaseApplication


/**
 * @author sunshine big boy
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

    fun getBoolean(key: String, defVal: Boolean = false): Boolean {
        return sp.getBoolean(key, defVal)
    }

    fun putLong(key: String, value: Long) {
        sp.edit().putLong(key, value).commit()
    }

    fun getLong(key: String, defVal: Long = 0L): Long {
        return sp.getLong(key, defVal)
    }

    private object Holder {
        val instance = SP()
    }
}