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
class Wework14197ProviderImpl : IWeworkProvider {

    override fun notificationRedEnvelopesText(): String = "[红包]"

    override fun msgItemContainerId(): String = "com.tencent.wework:id/b9f"
    override fun msgItemReadStatusId(): String = "com.tencent.wework:id/eyt"
    override fun msgItemRedEnvelopesFlagId(): String = "com.tencent.wework:id/gft"
    override fun msgItemRedEnvelopesHasOpenId(): String = "com.tencent.wework:id/g0j"

    override fun redEnvelopesCoverOpenId(): String = "com.tencent.wework:id/g0h"
    override fun redEnvelopesCoverCloseId(): String = "com.tencent.wework:id/gga"

    override fun redEnvelopesDetailCloseId(): String = "com.tencent.wework:id/ig0"
}