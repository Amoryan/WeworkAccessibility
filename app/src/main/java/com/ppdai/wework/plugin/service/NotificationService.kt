package com.ppdai.wework.plugin.service

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.ppdai.wework.plugin.constants.wechat.WeChatConfig
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.util.Logger
import com.ppdai.wework.plugin.util.SP

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

        when (sbn.packageName) {
            WeworkConfig.PACKAGE_NAME_WEWORK -> processWeworkNotification(sbn)
            WeChatConfig.PACKAGE_NAME_WECHAT -> processWechatNotification(sbn)
        }
    }

    /**
     * 处理企业微信通知
     */
    private fun processWeworkNotification(sbn: StatusBarNotification) {
        Logger.d("监听到企业微信的通知到达")
        if (!SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_NOTIFICATION)) {
            Logger.d("自动点击企业微信通知功能已关闭，请在设置里面打开")
            return
        }

        val notification = sbn.notification
        val extras: Bundle = notification.extras
        val text = extras.getCharSequence(Notification.EXTRA_TEXT) ?: ""
        if (text.contains(WeworkConfig.TEXT_NOTIFICATION_RED_ENVELOPES)) {
            Logger.d("检测到红包消息，正在打开消息")
            try {
                notification.contentIntent.send()
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 处理微信通知
     */
    private fun processWechatNotification(sbn: StatusBarNotification) {
        Logger.d("监听到微信的通知到达")
        val notification = sbn.notification
        val extras: Bundle = notification.extras
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)
        val subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)
    }
}