package com.ppdai.wework.plugin.service

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class Handler(service: RedEnvelopesService) : Handler(Looper.getMainLooper()) {

    companion object {
        const val OPEN_WECHAT_RED_ENVELOPES = 0x0001
    }

    private val service: RedEnvelopesService? = WeakReference(service).get()

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            OPEN_WECHAT_RED_ENVELOPES -> {
                service?.findCloseRedEnvelopesCover()
            }
        }
    }
}