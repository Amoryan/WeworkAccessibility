package com.ppdai.wework.plugin.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.core.wework.IWeworkProvider
import com.ppdai.wework.plugin.util.Logger
import com.ppdai.wework.plugin.util.SP
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

    private var weworkProvider: IWeworkProvider = WeworkManager.getInstance().weworkProvider

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
                onWindowContentChanged(event)
            }
        }
    }

    private fun onWindowStateChanged(event: AccessibilityEvent) {
        Logger.d("$rootInActiveWindow")

        currentWindow = event.className.toString()

        val windowName = currentWindow ?: return

        when (windowName) {
            WeworkConfig.ACTIVITY_NAME_MESSAGE_LIST -> {
                Logger.d("当前 Window 是 $currentWindow")
                // 如果是新的消息列表，清空数据
                if (rootInActiveWindow != messageListActivityNodeInfo) {
                    messageListActivityNodeInfo = rootInActiveWindow
                    messageListActivityRedEnvelopesFilterList.clear()
                }
            }
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_COVER -> {
                Logger.d("当前 Window 是 $currentWindow")
                // 红包封面
                openRedEnvelopes()
            }
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_DETAIL -> {
                Logger.d("当前 Window 是 $currentWindow")
            }
        }
    }

    private fun onWindowContentChanged(event: AccessibilityEvent) {
        val windowName = currentWindow ?: return
        when (windowName) {
            WeworkConfig.ACTIVITY_NAME_MESSAGE_LIST -> {
                // 消息列表界面，查询是否有红包
                queryRedEnvelopes()
            }
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_COVER -> {
                // 红包封面
            }
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_DETAIL -> {
                // 红包详情
                closeRedEnvelopesDetail()
            }
        }
    }

    /**
     * 查询是否有红包节点
     */
    private fun queryRedEnvelopes() {
        if (!SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_RED_ENVELOPES_MSG)) {
            Logger.d("自动点击红包功能已关闭，请到设置里面开启")
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
                }
            }
        }
    }


    /**
     * 打开红包
     */
    private fun openRedEnvelopes() {
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
            Logger.d("自动打开红包功能已关闭，请到设置里面开启")
            return
        }

        // 点击开启红包
        Logger.d("从打开红包节点向上查找第一个可点击的节点")

        val delay: Long = SP.getInstance().getLong(WeworkSpKeys.WEWORK_DELAY_OPEN_RED_ENVELOPES)
        if (delay > 0L) {
            Logger.d("已开启延迟打开红包，将延迟$delay ms后开启红包")
            handler.postDelayed({ findAndClickFirstClickableParentNode(openRedEnvelopesNodeList.last()) }, delay)
        } else {
            findAndClickFirstClickableParentNode(openRedEnvelopesNodeList.last())
        }
    }

    /**
     * 关闭红包详情页
     */
    private fun closeRedEnvelopesDetail() {
        if (!SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLOSE_RED_ENVELOPES_DETAIL)) {
            Logger.d("抢完红包自动关闭界面功能已关闭，请到设置里面开启")
            return
        }
        val rootNode = rootInActiveWindow
        val closeNodeList = rootNode.findAccessibilityNodeInfosByViewId(weworkProvider.redEnvelopesDetailCloseId())
        findAndClickFirstClickableParentNode(closeNodeList.firstOrNull())
    }

    /**
     * 找第一个可点击的节点并进行点击
     */
    private fun findAndClickFirstClickableParentNode(node: AccessibilityNodeInfo?) {
        var clickNode = node
        while (clickNode != null) {
            if (clickNode.isClickable) {
                clickNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                break
            }
            clickNode = clickNode.parent
        }
    }
}