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
}