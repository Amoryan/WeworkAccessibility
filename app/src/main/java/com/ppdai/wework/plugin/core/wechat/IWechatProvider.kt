package com.ppdai.wework.plugin.core.wechat

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
interface IWechatProvider {

    fun notificationRedEnvelopesText(): String

    fun msgItemContainerId(): String
    fun msgItemRedEnvelopesFlagId(): String
    fun msgItemRedEnvelopesInvalidId(): String

    fun redEnvelopesCoverOpenId(): String
    fun redEnvelopesCoverCloseId(): String

    fun redEnvelopesDetailCloseId(): String
}