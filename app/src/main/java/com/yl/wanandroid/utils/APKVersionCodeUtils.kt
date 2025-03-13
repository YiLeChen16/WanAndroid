package com.yl.wanandroid.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build


/**
 * @description: APK相关工具类
 * @author YL Chen
 * @date 2025/3/12 22:37
 * @version 1.0
 */
object APKVersionCodeUtils {
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    fun getVersionCode(mContext: Context): String {
        var versionCode = 0L
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            val packageInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionCode.toFloat().toString()
    }

}