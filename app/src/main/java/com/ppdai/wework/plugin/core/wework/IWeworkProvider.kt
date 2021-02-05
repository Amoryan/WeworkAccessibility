package com.ppdai.wework.plugin.core.wework

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
interface IWeworkProvider {

    fun notificationRedEnvelopesText(): String

    fun msgItemContainerId(): String
    fun msgItemReadStatusId(): String
    fun msgItemRedEnvelopesFlagId(): String
    fun msgItemRedEnvelopesHasOpenId(): String

    fun redEnvelopesCoverOpenId(): String
    fun redEnvelopesCoverCloseId(): String

    fun redEnvelopesDetailCloseId(): String
}