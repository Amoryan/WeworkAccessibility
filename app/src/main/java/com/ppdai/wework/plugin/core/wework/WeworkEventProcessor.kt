package com.ppdai.wework.plugin.core.wework

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.util.AccessibilityHelper
import com.ppdai.wework.plugin.util.Logger
import com.ppdai.wework.plugin.util.SP
import com.ppdai.wework.plugin.util.wework.WeworkManager

/**
 * @author sunshine big boy
 */
object WeworkEventProcessor {

    // 当前window
    private var currentWindow: String? = null

    // 最近的聊天Node
    private var latestMsgListNode: AccessibilityNodeInfo? = null

    // 过期红包（过期红包有的不显示已过期，会一直循环点）
    private val blackList = arrayListOf<AccessibilityNodeInfo>()

    private var tmpClickNode: AccessibilityNodeInfo? = null

    fun process(service: AccessibilityService, event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> windowStateChanged(service, event)
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> windowContentChanged(service, event)
            else -> {
            }
        }
    }

    /**
     * TYPE_WINDOW_STATE_CHANGED
     */
    private fun windowStateChanged(service: AccessibilityService, event: AccessibilityEvent) {
        Logger.d("【企业微信】TYPE_WINDOW_STATE_CHANGED, ${event.className?.toString()}")

        currentWindow = event.className?.toString()

        when (event.className) {
            WeworkConfig.ACTIVITY_NAME_MESSAGE_LIST -> {
                if (latestMsgListNode != service.rootInActiveWindow) {
                    latestMsgListNode = service.rootInActiveWindow
                    blackList.clear()
                }
            }
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_COVER -> {
                openRedEnvelopes(service)
            }
        }
    }

    /**
     * TYPE_WINDOW_CONTENT_CHANGED
     */
    private fun windowContentChanged(service: AccessibilityService, event: AccessibilityEvent) {
        Logger.d("【企业微信】TYPE_WINDOW_CONTENT_CHANGED")

        when (currentWindow) {
            WeworkConfig.ACTIVITY_NAME_MESSAGE_LIST -> {
                queryAndClickRedEnvelopesMsg(service)
            }
            WeworkConfig.ACTIVITY_NAME_RED_ENVELOPES_DETAIL -> {
                // 关闭红包界面
                if (!SP.getInstance().getBoolean(WeworkSpKeys.AUTO_CLOSE_RED_ENVELOPES_DETAIL, true)) {
                    Logger.d("【企业微信】抢完红包自动关闭界面已关闭")
                    return
                }
                val rootNode = service.rootInActiveWindow
                val findNodeList = rootNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.redEnvelopesDetailCloseId())
                AccessibilityHelper.findAndClickFirstClickableNode(findNodeList.firstOrNull())
            }
        }
    }

    /**
     * 查询并点击红包消息
     */
    private fun queryAndClickRedEnvelopesMsg(service: AccessibilityService) {
        if (!SP.getInstance().getBoolean(WeworkSpKeys.AUTO_CLICK_RED_ENVELOPES_MSG, true)) {
            Logger.d("【企业微信】自动点击红包已关闭")
            return
        }

        val rootNode = service.rootInActiveWindow
        val findMsgNodeList = rootNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.msgItemContainerId())

        // 反向检索
        findMsgNodeList.reverse()

        for (msgNode in findMsgNodeList) {
            // 过滤本人发送消息
            var findNodeList = msgNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.msgItemReadStatusId())
            if (findNodeList.isNotEmpty()) {
                Logger.d("【企业微信】此消息为本人发送")
                continue
            }

            // 过滤非红包消息
            findNodeList = msgNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.msgItemRedEnvelopesFlagId())
            if (findNodeList.isEmpty()) {
                Logger.d("【企业微信】此消息非红包消息")
                continue
            }

            var clickNode = findNodeList.first()

            // 过滤不可领取红包消息
            findNodeList = msgNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.msgItemRedEnvelopesHasOpenId())
            if (findNodeList.isNotEmpty()){
                Logger.d("【企业微信】此红包不可领")
                continue
            }

            // 点击红包消息
            while (clickNode != null) {
                if (clickNode.isClickable) {
                    if (blackList.contains(clickNode)){
                        Logger.d("【企业微信】此红包不可领")
                    } else {
                        tmpClickNode = clickNode
                        clickNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                    break
                }
                clickNode = clickNode.parent
            }

            if (clickNode.isClickable){
                break
            }
        }
    }

    /**
     * 打开红包
     */
    private fun openRedEnvelopes(service: AccessibilityService) {
        val rootNode = service.rootInActiveWindow
        var findNodeList = rootNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.redEnvelopesCoverOpenId())

        if (findNodeList.isEmpty()) {
            Logger.d("【企业微信】红包不可领")
            tmpClickNode?.let { blackList.add(it) }
            // 关闭红包封面
            findNodeList = rootNode.findAccessibilityNodeInfosByViewId(WeworkManager.getInstance().provider.redEnvelopesCoverCloseId())
            AccessibilityHelper.findAndClickFirstClickableNode(findNodeList.firstOrNull())
            return
        }

        if (!SP.getInstance().getBoolean(WeworkSpKeys.AUTO_OPEN_RED_ENVELOPES, true)) {
            Logger.d("【企业微信】自动打开红包功能已关闭")
            return
        }

        AccessibilityHelper.findAndClickFirstClickableNode(findNodeList.firstOrNull())
    }

}