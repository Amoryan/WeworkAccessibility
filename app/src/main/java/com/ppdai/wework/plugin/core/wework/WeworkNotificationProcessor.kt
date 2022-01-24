package com.ppdai.wework.plugin.core.wework

import android.app.Notification
import android.os.Bundle
import android.service.notification.StatusBarNotification
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.util.Logger
import com.ppdai.wework.plugin.util.SP

/**
 * @author sunshine big boy
 */
object WeworkNotificationProcessor {

    /**
     * 处理企业微信通知
     */
    @JvmStatic
    fun process(sbn: StatusBarNotification) {
        if (!SP.getInstance().getBoolean(WeworkSpKeys.AUTO_CLICK_NOTIFICATION)) {
            Logger.d("【企业微信】自动点击通知功能已关闭")
            return
        }

        Logger.d("【企业微信】通知内容：$sbn")

        val notification = sbn.notification
        val extras: Bundle = notification.extras
        val text = extras.getCharSequence(Notification.EXTRA_TEXT) ?: ""
        if (text.contains("[红包]")) {
            Logger.d("【企业微信】检测到红包通知，正在打开")
            try {
                notification.contentIntent.send()
            } catch (e: Exception) {
            }
        }
    }
}