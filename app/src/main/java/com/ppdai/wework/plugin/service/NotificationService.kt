package com.ppdai.wework.plugin.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.util.Logger
import com.ppdai.wework.plugin.core.wework.WeworkNotificationProcessor

/**
 * @author sunshine big boy
 */
class NotificationService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Logger.d("通知服务已绑定")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Logger.d("通知服务已解绑")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        sbn ?: return

        when (sbn.packageName) {
            WeworkConfig.PACKAGE_NAME -> WeworkNotificationProcessor.process(sbn)
        }
    }
}