package com.ppdai.wework.plugin.util

import android.view.accessibility.AccessibilityNodeInfo

/**
 * @author sunshine big boy
 */
object AccessibilityHelper {

    fun findAndClickFirstClickableNode(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
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
}