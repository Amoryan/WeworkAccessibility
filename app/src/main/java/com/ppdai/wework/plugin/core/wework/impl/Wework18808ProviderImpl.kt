package com.ppdai.wework.plugin.core.wework.impl

import com.ppdai.wework.plugin.core.wework.IWeworkProvider

/**
 * @author sunshine big boy
 *
 * 企业微信v3.1.2 (14197)
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class Wework18808ProviderImpl : IWeworkProvider {

    override fun notificationRedEnvelopesText(): String = "[红包]"

    override fun msgItemContainerId(): String = "com.tencent.wework:id/gb8"//gb9
    override fun msgItemReadStatusId(): String = "com.tencent.wework:id/gd9"
    override fun msgItemRedEnvelopesFlagId(): String = "com.tencent.wework:id/i6a"
    override fun msgItemRedEnvelopesHasOpenId(): String = "com.tencent.wework:id/hly"

    override fun redEnvelopesCoverOpenId(): String = "com.tencent.wework:id/hlw"
    override fun redEnvelopesCoverCloseId(): String = "com.tencent.wework:id/i6s"

    override fun redEnvelopesDetailCloseId(): String = "com.tencent.wework:id/kbo"
}