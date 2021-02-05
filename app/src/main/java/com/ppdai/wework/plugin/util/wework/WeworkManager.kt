package com.ppdai.wework.plugin.util.wework

import com.ppdai.wework.plugin.BaseApplication
import com.ppdai.wework.plugin.constants.wework.WeworkConfig
import com.ppdai.wework.plugin.core.wework.IWeworkProvider
import com.ppdai.wework.plugin.core.wework.impl.Wework14025ProviderImpl
import com.ppdai.wework.plugin.core.wework.impl.Wework14197ProviderImpl

/**
 * @author sunshine big boy
 *
 * <pre>
 *      talking is cheap, show me the code
 * </pre>
 */
class WeworkManager private constructor() {

    companion object {
        @JvmStatic
        fun getInstance(): WeworkManager {
            return Holder.instance
        }
    }

    val versionName: String
    val versionCode: Int
    val provider: IWeworkProvider

    init {
        val packageInfo = BaseApplication.appContext.packageManager
                .getInstalledPackages(0)
                .firstOrNull { it.packageName == WeworkConfig.PACKAGE_NAME_WEWORK }
        versionName = packageInfo?.versionName ?: "unknown"
        versionCode = packageInfo?.versionCode ?: 0

        provider = when (versionCode) {
            WeworkConfig.VERSION_CODE_14025 -> Wework14025ProviderImpl()
            WeworkConfig.VERSION_CODE_14197 -> Wework14197ProviderImpl()
            else -> Wework14025ProviderImpl()
        }
    }

    private object Holder {
        val instance = WeworkManager()
    }
}