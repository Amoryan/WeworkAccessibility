package com.ppdai.wework.plugin.constants

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
object Config {

    const val PACKAGE_NAME_WEWORK = "com.tencent.wework"

    const val ACTIVITY_NAME_MESSAGE_LIST = "com.tencent.wework.msg.controller.MessageListActivity"
    const val ACTIVITY_NAME_RED_ENVELOPES_COVER = "com.tencent.wework.enterprise.redenvelopes.controller.RedEnvelopeCollectorWithCoverActivity"
    const val ACTIVITY_NAME_RED_ENVELOPES_DETAIL = "com.tencent.wework.enterprise.redenvelopes.controller.RedEnvelopeDetailWithCoverActivity"

    // 消息列表消息Container ID
    const val ID_MESSAGE_LIST_ITEM = "com.tencent.wework:id/b90"

    // 已读未读，标记是否是本人发送的消息
    const val ID_MESSAGE_LIST_READ_IMAGE = "com.tencent.wework:id/eu2"

    // 消息列表红包消息红包已领取 TextView ID
    const val ID_MESSAGE_LIST_RED_ENVELOPES_HAS_OPEN = "com.tencent.wework:id/fz0"

    // 消息列表红包icon ImageView ID
    const val ID_MESSAGE_LIST_RED_ENVELOPES_IMAGE = "com.tencent.wework:id/ge9"

    // 消息列表红包消息红包 TextView ID
    const val ID_MESSAGE_LIST_RED_ENVELOPES_TEXT = "com.tencent.wework:id/ge8"

    // 红包封面页面打开红包 ImageView ID
    const val ID_RED_ENVELOPES_COVER_IMAGE_OPEN = "com.tencent.wework:id/fyy"

    // 红包封面红包已过期 TextView ID
    const val ID_RED_ENVELOPES_COVER_HAS_EXPIRED = ID_MESSAGE_LIST_RED_ENVELOPES_HAS_OPEN

    // 红包封面关闭 ImageView ID
    const val ID_RED_ENVELOPES_COVER_CLOSE = "com.tencent.wework:id/geq"

    // 红包详情关闭 ImageView ID
    const val ID_RED_ENVELOPES_DETAIL_CLOSE = "com.tencent.wework:id/idp"

    // 通知栏红包消息文本
    const val TEXT_NOTIFICATION_RED_ENVELOPES = "[红包]"
}