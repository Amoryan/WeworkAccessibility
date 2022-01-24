package com.ppdai.wework.plugin.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.core.wework.WeworkEventProcessor
import com.ppdai.wework.plugin.util.Logger

/**
 * @author sunshine big boy
 */
class RedEnvelopesService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        Logger.d("皮皮呆助手已绑定")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Logger.d("皮皮呆助手已解绑")
        return super.onUnbind(intent)
    }

    override fun onInterrupt() {
        Logger.d("皮皮呆助手被中断")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return

        when (event.packageName) {
            WeworkConfig.PACKAGE_NAME -> WeworkEventProcessor.process(this, event)
        }
    }
}