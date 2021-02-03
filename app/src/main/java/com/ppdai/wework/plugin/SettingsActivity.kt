package com.ppdai.wework.plugin

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.ppdai.wework.plugin.constants.SpKeys
import com.ppdai.wework.plugin.util.SP
import kotlinx.android.synthetic.main.activity_settings.*


/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        cbAutoClickRedEnvelopes.isChecked = SP.getInstance().getBoolean(SpKeys.AUTO_CLICK_RED_ENVELOPES_MESSAGE)
        cbAutoOpenRedEnvelopes.isChecked = SP.getInstance().getBoolean(SpKeys.AUTO_OPEN_RED_ENVELOPES)
        cbAutoCloseRedEnvelopesDetail.isChecked = SP.getInstance().getBoolean(SpKeys.AUTO_CLOSE_RED_ENVELOPES_DETAIL)

        clAutoCheckNotification.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        cbAutoClickRedEnvelopes.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(SpKeys.AUTO_CLICK_RED_ENVELOPES_MESSAGE, isChecked)
        }
        cbAutoOpenRedEnvelopes.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(SpKeys.AUTO_OPEN_RED_ENVELOPES, isChecked)
        }
        cbAutoCloseRedEnvelopesDetail.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(SpKeys.AUTO_CLOSE_RED_ENVELOPES_DETAIL, isChecked)
        }

        clDelayOpenRedEnvelopes.setOnClickListener { startActivity(Intent(this, InputDelayMillisActivity::class.java)) }
    }

    private fun isNotificationListenerEnabled(): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(this)
        return packageNames.contains(packageName)
    }
}