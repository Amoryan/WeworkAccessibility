package com.ppdai.wework.plugin.core.wechat.impl

import com.ppdai.wework.plugin.core.wechat.IWechatProvider

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class Wechat1840ProviderImpl : IWechatProvider {

    override fun notificationRedEnvelopesText(): String = "[微信红包]"

    override fun msgItemContainerId(): String = "com.tencent.mm:id/auf"
    override fun msgItemRedEnvelopesFlagId(): String = "com.tencent.mm:id/tv"
    override fun msgItemRedEnvelopesInvalidId(): String = "com.tencent.mm:id/tt"

    override fun redEnvelopesCoverOpenId(): String = "com.tencent.mm:id/f4f"
    override fun redEnvelopesCoverCloseId(): String = "com.tencent.mm:id/f4e"

    override fun redEnvelopesDetailCloseId(): String = "com.tencent.mm:id/eh"
}