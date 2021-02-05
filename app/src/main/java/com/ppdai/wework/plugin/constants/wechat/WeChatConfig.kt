package com.ppdai.wework.plugin.constants.wechat

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
object WeChatConfig {

    const val PACKAGE_NAME_WECHAT = "com.tencent.mm"

    // 微信的主界面和聊天界面都是LuancherUI
    const val ACTIVITY_MESSAGE_LIST = "com.tencent.mm.ui.LauncherUI"

    // 聊天界面返回ID，因微信的 主界面和聊天界面都是 LauncherUI，要用个东西做一下区分
    const val ID_MESSAGE_LIST_BACK_IMAGEVIEW = "com.tencent.mm:id/uo"

    // 聊天列表消息Item容器ID
    const val ID_MESSAGE_ITEM_CONTAINER = "com.tencent.mm:id/auf"

    // 微信红包TextView ID
    const val ID_RED_ENVELOPES_TEXTVIEW = "com.tencent.mm:id/u5"

    // 微信红包状态文案 TextView ID（已过期，已领取...）
    const val ID_RED_ENVENLOPES_STATUS_TEXTVIEW = "com.tencent.mm:id/tt"


}