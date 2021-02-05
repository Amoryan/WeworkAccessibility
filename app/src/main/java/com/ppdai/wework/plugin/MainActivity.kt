package com.ppdai.wework.plugin

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.ppdai.wework.plugin.constants.wework.WeworkSpKeys
import com.ppdai.wework.plugin.util.SP
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermissionGroup()
        initWeworkGroup()
        initCommonGroup()
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
    private fun initWeworkGroup() {
        cbWeworkOpenNotification.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_NOTIFICATION)
        cbWeworkOpenRedEnvelopesMsg.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_RED_ENVELOPES_MSG)
        cbWeworkOpenRedEnvelopes.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_OPEN_RED_ENVELOPES)
        cbWeworkClose.isChecked = SP.getInstance().getBoolean(WeworkSpKeys.WEWORK_AUTO_CLOSE_RED_ENVELOPES_DETAIL)

        cbWeworkOpenNotification.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_NOTIFICATION, isChecked)
        }
        cbWeworkOpenRedEnvelopesMsg.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.WEWORK_AUTO_CLICK_RED_ENVELOPES_MSG, isChecked)
        }
        cbWeworkOpenRedEnvelopes.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.WEWORK_AUTO_OPEN_RED_ENVELOPES, isChecked)
        }
        cbWeworkClose.setOnCheckedChangeListener { _, isChecked ->
            SP.getInstance().putBoolean(WeworkSpKeys.WEWORK_AUTO_CLOSE_RED_ENVELOPES_DETAIL, isChecked)
        }
    }

    /**
     * 通用设置
     */
    private fun initCommonGroup() {
        tvDelayOpenRedEnvelopes.setOnClickListener { startActivity(Intent(this, InputDelayMillisActivity::class.java)) }
    }
}