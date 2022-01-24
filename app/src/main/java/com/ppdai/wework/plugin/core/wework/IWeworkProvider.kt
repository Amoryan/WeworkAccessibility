package com.ppdai.wework.plugin.core.wework

/**
 * @author sunshine big boy
 */
interface IWeworkProvider {

    fun msgItemContainerId(): String
    fun msgItemReadStatusId(): String
    fun msgItemRedEnvelopesFlagId(): String
    fun msgItemRedEnvelopesHasOpenId(): String

    fun redEnvelopesCoverOpenId(): String
    fun redEnvelopesCoverCloseId(): String

    fun redEnvelopesDetailCloseId(): String
}