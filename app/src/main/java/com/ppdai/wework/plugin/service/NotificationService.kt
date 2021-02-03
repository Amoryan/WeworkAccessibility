package com.ppdai.wework.plugin.service

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.ppdai.wework.plugin.constants.Config
import com.ppdai.wework.plugin.util.Logger

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
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

        val isWework = sbn.packageName == Config.PACKAGE_NAME_WEWORK
        if (isWework) {
            Logger.d("监听到企业微信的通知到达")
            val notification = sbn.notification
            val extras: Bundle = notification.extras
            val text = extras.getCharSequence(Notification.EXTRA_TEXT) ?: ""
            if (text.contains(Config.TEXT_NOTIFICATION_RED_ENVELOPES)) {
                Logger.d("检测到红包消息，正在打开消息")
                try {
                    notification.contentIntent.send()
                } catch (e: Exception) {
                }
            }
        }
    }
}