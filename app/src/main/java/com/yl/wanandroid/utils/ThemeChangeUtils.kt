package com.yl.wanandroid.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.yl.wanandroid.R


/**
 * 主题切换工具类
 */
object ThemeChangeUtils {

    const val KEY_MODE: String = "white_night_mode_sp"

    /**
     * 在 Application 的 onCreate() 方法中调用
     */
    fun init(application: Application) {
        val nightMode = getNightModel(application)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    /**
     * 应用夜间模式
     */
    fun applyNightMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setNightModel(context, AppCompatDelegate.MODE_NIGHT_YES)
    }

    /**
     * 应用日间模式
     */
    fun applyDayMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setNightModel(context, AppCompatDelegate.MODE_NIGHT_NO)
    }

    /**
     * 跟随系统主题时需要动态切换
     */
    fun applySystemMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setNightModel(context, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    /**
     * 取消跟随系统主题
     * @param context Context
     */
    fun notApplySystemMode(context: Context) {
        // 获取当前应用的夜间模式设置
        if (isDarkMode(context)) {
            applyNightMode(context)
        } else {
            applyDayMode(context)
        }
    }

    /**
     * 判断App当前是否处于暗黑模式状态
     */
    fun isDarkMode(context: Context): Boolean {
        // 获取当前应用的夜间模式设置
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        // 如果应用设置为跟随系统
        if (nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            // 获取系统的UI模式配置
            val applicationUiMode = context.resources.configuration.uiMode
            // 使用位掩码判断系统是否为暗黑模式
            val systemMode = applicationUiMode and Configuration.UI_MODE_NIGHT_MASK
            return systemMode == Configuration.UI_MODE_NIGHT_YES
        } else {
            // 如果应用设置为固定的暗黑或亮色模式
            return nightMode == AppCompatDelegate.MODE_NIGHT_YES
        }
    }


    private fun getNightModel(context: Context): Int {
        val sp = context.getSharedPreferences(KEY_MODE, Context.MODE_PRIVATE)
        return sp.getInt(KEY_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    private fun setNightModel(context: Context, nightMode: Int) {
        val sp = context.getSharedPreferences(KEY_MODE, Context.MODE_PRIVATE)
        sp.edit().putInt(KEY_MODE, nightMode).apply()
    }

    /**
     * 判断App主题是否跟随系统主题
     * @param context Context
     */
    fun isFollowSystemMode(context: Context): Boolean {
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == getNightModel(context)
    }

}