package com.ppdai.wework.plugin.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.ppdai.wework.plugin.constants.SpKeys
import com.ppdai.wework.plugin.constants.wechat.WeChatConfig
import com.ppdai.wework.plugin.constants.wechat.WechatSpKeys
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.core.wechat.IWechatProvider
import com.ppdai.wework.plugin.core.wework.IWeworkProvider
import com.ppdai.wework.plugin.util.Logger
import com.ppdai.wework.plugin.util.SP
import com.ppdai.wework.plugin.util.wechat.WechatManager
import com.ppdai.wework.plugin.util.wework.WeworkManager

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class RedEnvelopesService : AccessibilityService() {

    companion object {
        val handler = Handler(Looper.getMainLooper())
    }

    private var weworkProvider: IWeworkProvider = WeworkManager.getInstance().provider
    private var wechatProvider: IWechatProvider = WechatManager.getInstance().provider

    private var currentWindow: String? = null

    private var messageListActivityNodeInfo: AccessibilityNodeInfo? = null
    private var messageListActivityRedEnvelopesFilterList = ArrayList<AccessibilityNodeInfo>()
    private var clickRedEnvelopesNode: AccessibilityNodeInfo? = null

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

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                Logger.d("接收到 WINDOW_STATE_CHANGED 事件")
                onWindowStateChanged(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                Logger.d("接收到 WINDOW_CONTENT_CHANGED 事件")
                onWindowContentChanged()
            }
        }
    }

    private fun onWindowStateChanged(event: AccessibilityEvent) {
        when (event.packageName) {
            WeChatConfig.PACKAGE_NAME_WECHAT -> {
                currentWindow = event.className.toString()
                // 微信主界面和聊天都是LauncherUI
                if ("android.widget.LinearLayout" == currentWindow) {
                    currentWindow = WeChatConfig.ACTIVITY_NAME_MESSAGE_LIST
                }
                Logger.d("当前window: $currentWindow")
                val windowName = currentWindow ?: return

                when (windowName) {
                    WeChatConfig.ACTIVITY_NAME_RED_ENVELOPES_COVER -> openWechatRedEnvelopes(event)
                }
            }
            WeworkConfig.PACKAGE_NAME_WEWORK -> {
                currentWindow = event.className.toString()
                Logger.d("当前window: $currentWindow")
                val windowName = currentWindow ?: return

                when (windowName) {
                    WeworkConfig.ACTIVITY_NAME_MESSAGE_LIST -> {
                        // 如果是新的消息列表，清空数据
                        if (rootInActiveWindow != messageListActivityNodeInfo) {
                            messageListActivityNodeInfo = rootInActiveWindow
                            messageListActivityRedEnvelopesFilterList.clear()
                        }
                    }
                    WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_COVER -> openWeworkRedEnvelopes()
                }
            }
        }
    }

    private fun onWindowContentChanged() {
        val windowName = currentWindow ?: return
        when (windowName) {
            WeworkConfig.ACTIVITY_NAME_MESSAGE_LIST -> queryWeworkRedEnvelopes()
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_DETAIL -> closeWeworkRedEnvelopesDetail()
            WeChatConfig.ACTIVITY_NAME_MESSAGE_LIST -> queryWechatRedEnvelopes()
            WeChatConfig.ACTIVITY_NAME_RED_ENVELOPES_DETAIL -> closeWechatRedEnvelopesDetail()
        }
    }

    /**
     * 查询企业微信聊天界面是否有红包节点
     */
    private fun queryWeworkRedEnvelopes() {
        if (!SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_RED_ENVELOPES_MSG)) {
            Logger.d("企业微信自动点击红包功能已关闭，请到设置里面开启")
            return
        }

        val rootNode = rootInActiveWindow
        // 查找消息Container Node
        val messageItemContainerNodeList = rootNode.findAccessibilityNodeInfosByViewId(weworkProvider.msgItemContainerId())
        Logger.d("查找到消息数量: ${messageItemContainerNodeList.size}")

        if (messageItemContainerNodeList.isEmpty()) {
            return
        }

        /*
            反向遍历消息 Container ，在每个Node 上做如下事情
            1. 是否包含已读未读的View Id，如果包含，说明是本人发送消息；
            2. 查找是否有红包 ImageView Id，如果有，则说明这是红包；
            3. 如果是红包消息，则看红包是否已被领取，如果未被领取，则点击
         */
        messageItemContainerNodeList.reverse()
        for (messageItemContainerNode in messageItemContainerNodeList) {
            val readNodeList = messageItemContainerNode.findAccessibilityNodeInfosByViewId(weworkProvider.msgItemReadStatusId())
            if (readNodeList.isNotEmpty()) {
                Logger.d("此条消息为本人发送，忽略")
                continue
            }

            // 消息上找红包ImageView
            val redEnvelopesImageViewNodeList = messageItemContainerNode.findAccessibilityNodeInfosByViewId(weworkProvider.msgItemRedEnvelopesFlagId())
            if (redEnvelopesImageViewNodeList.isEmpty()) {
                Logger.d("此条消息不是红包消息，忽略")
            } else {
                // 查找是否有红包已领取 TextView的显示，如果有说明
                val hasOpenNodeList = messageItemContainerNode.findAccessibilityNodeInfosByViewId(weworkProvider.msgItemRedEnvelopesHasOpenId())
                if (hasOpenNodeList.isNotEmpty()) {
                    Logger.d("此条消息是红包消息，但是已经被领取了，忽略")
                    continue
                } else {
                    Logger.d("此条消息是红包消息，且未被领取，执行点击")
                    var clickNode = redEnvelopesImageViewNodeList.first()
                    while (clickNode != null) {
                        if (clickNode.isClickable) {
                            if (messageListActivityRedEnvelopesFilterList.contains(clickNode)) {
                                Logger.d("这个红包点击过了，发现是过期红包，忽略")
                            } else {
                                clickRedEnvelopesNode = clickNode
                                clickNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            }
                            break
                        }
                        clickNode = clickNode.parent
                    }
                    if (clickNode.isClickable) {
                        break
                    }
                }
            }
        }
    }

    /**
     * 打开企业微信红包
     */
    private fun openWeworkRedEnvelopes() {
        val rootNode = rootInActiveWindow
        val openRedEnvelopesNodeList = rootNode.findAccessibilityNodeInfosByViewId(weworkProvider.redEnvelopesCoverOpenId())

        if (openRedEnvelopesNodeList.isEmpty()) {
            Logger.d("此红包已过期")
            clickRedEnvelopesNode?.let { messageListActivityRedEnvelopesFilterList.add(it) }
            // 关闭红包封面
            val closeRedEnvelopesCoverNodeList = rootNode.findAccessibilityNodeInfosByViewId(weworkProvider.redEnvelopesCoverCloseId())
            findAndClickFirstClickableParentNode(closeRedEnvelopesCoverNodeList.firstOrNull())
            return
        }

        if (!SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_OPEN_RED_ENVELOPES)) {
            Logger.d("企业微信自动打开红包功能已关闭，请到设置里面开启")
            return
        }

        openRedEnvelopes(openRedEnvelopesNodeList.last())
    }

    /**
     * 企业微信关闭红包详情页
     */
    private fun closeWeworkRedEnvelopesDetail() {
        if (!SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLOSE_RED_ENVELOPES_DETAIL)) {
            Logger.d("企业微信抢完红包自动关闭界面功能已关闭，请到设置里面开启")
            return
        }
        val rootNode = rootInActiveWindow
        val closeNodeList = rootNode.findAccessibilityNodeInfosByViewId(weworkProvider.redEnvelopesDetailCloseId())
        findAndClickFirstClickableParentNode(closeNodeList.firstOrNull())
    }

    /**
     * 查询微信聊天界面是否有红包节点
     */
    private fun queryWechatRedEnvelopes() {
        if (!SP.getInstance().getBoolean(WechatSpKeys.WECHAT_AUTO_CLICK_RED_ENVELOPES_MSG)) {
            Logger.d("微信自动点击红包功能已关闭，请到设置里面开启")
            return
        }

        val rootNode = rootInActiveWindow
        // 查找消息Container Node
        val messageItemContainerNodeList = rootNode.findAccessibilityNodeInfosByViewId(wechatProvider.msgItemContainerId())
        Logger.d("查找到红包消息数量: ${messageItemContainerNodeList.size}")

        if (messageItemContainerNodeList.isEmpty()) {
            return
        }

        messageItemContainerNodeList.reverse()
        for (messageItemContainerNode in messageItemContainerNodeList) {
            val parentNode = messageItemContainerNode.parent
            // 查找是否有红包已领取 TextView的显示，如果有说明
            val invalidNodeList = parentNode.findAccessibilityNodeInfosByViewId(wechatProvider.msgItemRedEnvelopesInvalidId())
            if (invalidNodeList.isNotEmpty()) {
                Logger.d("此条消息是红包消息，但是已经被领取了，忽略")
                continue
            } else {
                Logger.d("此条消息是红包消息，且未被领取，执行点击")
                val clickNode = findAndClickFirstClickableParentNode(parentNode.findAccessibilityNodeInfosByViewId(wechatProvider.msgItemRedEnvelopesFlagId())?.firstOrNull())
                if (clickNode?.isClickable == true) {
                    break
                }
            }
        }
    }

    /**
     * 打开微信红包
     */
    private fun openWechatRedEnvelopes(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow
        val openRedEnvelopesNodeList = rootNode.findAccessibilityNodeInfosByViewId(wechatProvider.redEnvelopesCoverOpenId())

        if (openRedEnvelopesNodeList.isEmpty()) {
            Logger.d("此红包已过期")
            // 关闭红包封面
            val closeRedEnvelopesCoverNodeList = rootNode.findAccessibilityNodeInfosByViewId(wechatProvider.redEnvelopesCoverCloseId())
            findAndClickFirstClickableParentNode(closeRedEnvelopesCoverNodeList.firstOrNull())
            return
        }

        if (!SP.getInstance().getBoolean(WechatSpKeys.WECHAT_AUTO_OPEN_RED_ENVELOPES)) {
            Logger.d("微信自动打开红包功能已关闭，请到设置里面开启")
            return
        }

        openRedEnvelopes(openRedEnvelopesNodeList.last())
    }

    /**
     * 微信关闭红包详情页
     */
    private fun closeWechatRedEnvelopesDetail() {
        if (!SP.getInstance().getBoolean(WechatSpKeys.WECHAT_AUTO_CLOSE_RED_ENVELOPES_DETAIL)) {
            Logger.d("微信抢完红包自动关闭界面功能已关闭，请到设置里面开启")
            return
        }
        val rootNode = rootInActiveWindow
        val closeNodeList = rootNode.findAccessibilityNodeInfosByViewId(wechatProvider.redEnvelopesDetailCloseId())
        findAndClickFirstClickableParentNode(closeNodeList.firstOrNull())
    }

    /**
     * 找第一个可点击的节点并进行点击
     */
    private fun findAndClickFirstClickableParentNode(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        var clickNode = node
        while (clickNode != null) {
            if (clickNode.isClickable) {
                clickNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                break
            }
            clickNode = clickNode.parent
        }
        return clickNode
    }

    /**
     * 打开红包
     */
    private fun openRedEnvelopes(nodeInfo: AccessibilityNodeInfo) {
        val delay: Long = SP.getInstance().getLong(SpKeys.DELAY_OPEN_RED_ENVELOPES)
        if (delay > 0L) {
            Logger.d("已开启延迟打开红包，将延迟$delay ms后开启红包")
            handler.postDelayed({ findAndClickFirstClickableParentNode(nodeInfo) }, delay)
        } else {
            findAndClickFirstClickableParentNode(nodeInfo)
        }
    }
}