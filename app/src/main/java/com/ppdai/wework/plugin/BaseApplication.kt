package com.ppdai.wework.plugin

import android.app.Application
import android.content.Context

/**
 * @author sunshine big boy
 */
class BaseApplication : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}