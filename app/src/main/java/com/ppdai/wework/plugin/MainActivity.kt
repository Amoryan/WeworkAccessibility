package com.ppdai.wework.plugin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.util.SP
import com.ppdai.wework.plugin.util.wework.WeworkManager
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author sunshine big boy
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermissionGroup()
        initWeworkGroup()
    }

    /**
     * 系统权限
     */
    private fun initPermissionGroup() {
        tvOpenAccessibilityServices.setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        tvRetrieveNotification.setOnClickListener { startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
    }

    /**
     * 企业微信
     */
    @SuppressLint("SetTextI18n")
    private fun initWeworkGroup() {
        tvWework.text = "企业微信(版本${WeworkManager.getInstance().versionName}，${WeworkManager.getInstance().versionCode})"

        cbWeworkOpenNotification.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.AUTO_CLICK_NOTIFICATION)
        cbWeworkOpenRedEnvelopesMsg.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.AUTO_CLICK_RED_ENVELOPES_MSG, true)
        cbWeworkOpenRedEnvelopes.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.AUTO_OPEN_RED_ENVELOPES, true)
        cbWeworkClose.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.AUTO_CLOSE_RED_ENVELOPES_DETAIL, true)

        cbWeworkOpenNotification.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.AUTO_CLICK_NOTIFICATION, isChecked)
        }
        cbWeworkOpenRedEnvelopesMsg.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.AUTO_CLICK_RED_ENVELOPES_MSG, isChecked)
        }
        cbWeworkOpenRedEnvelopes.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.AUTO_OPEN_RED_ENVELOPES, isChecked)
        }
        cbWeworkClose.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.AUTO_CLOSE_RED_ENVELOPES_DETAIL, isChecked)
        }
    }
}