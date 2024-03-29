package com.ppdai.wework.plugin.core.wework.impl

import com.ppdai.wework.plugin.core.wework.IWeworkProvider

/**
 * @author sunshine big boy
 */
class Wework14025ProviderImpl : IWeworkProvider {

    override fun msgItemContainerId(): String = "com.tencent.wework:id/b90"
    override fun msgItemReadStatusId(): String = "com.tencent.wework:id/eu2"
    override fun msgItemRedEnvelopesFlagId(): String = "com.tencent.wework:id/ge9"
    override fun msgItemRedEnvelopesHasOpenId(): String = "com.tencent.wework:id/fz0"

    override fun redEnvelopesCoverOpenId(): String = "com.tencent.wework:id/fyy"
    override fun redEnvelopesCoverCloseId(): String = "com.tencent.wework:id/geq"

    override fun redEnvelopesDetailCloseId(): String = "com.tencent.wework:id/idp"
}