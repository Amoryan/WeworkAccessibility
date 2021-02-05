package com.ppdai.wework.plugin.util.wechat

import com.ppdai.wework.plugin.BaseApplication
import com.ppdai.wework.plugin.constants.wechat.WeChatConfig
import com.ppdai.wework.plugin.core.wechat.IWechatProvider
import com.ppdai.wework.plugin.core.wechat.impl.Wechat1840ProviderImpl

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class WechatManager private constructor() {

    companion object {
        @JvmStatic
        fun getInstance(): WechatManager {
            return Holder.instance
        }
    }

    val versionName: String
    val versionCode: Int
    val provider: IWechatProvider

    init {
        val packageInfo = BaseApplication.appContext.packageManager
                .getInstalledPackages(0)
                .firstOrNull { it.packageName == WeChatConfig.PACKAGE_NAME_WECHAT }
        versionName = packageInfo?.versionName ?: "unknown"
        versionCode = packageInfo?.versionCode ?: 0

        provider = when (versionCode) {
            WeChatConfig.VERSION_CODE_1840 -> Wechat1840ProviderImpl()
            else -> Wechat1840ProviderImpl()
        }
    }

    private object Holder {
        val instance = WechatManager()
    }
}