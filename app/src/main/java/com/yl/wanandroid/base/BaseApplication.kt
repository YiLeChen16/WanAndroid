package com.yl.wanandroid.base

import android.app.Application
import android.content.Context
import com.yl.wanandroid.utils.TipsToast
import dagger.hilt.android.HiltAndroidApp

/**
 * @description: App基类
 * @author YL Chen
 * @date 2024/9/8 16:59
 * @version 1.0
 */
//初始化Hilt框架
@HiltAndroidApp
class BaseApplication:Application(){
    companion object{
        lateinit var context: Context
    }
    override fun onCreate() {
        TipsToast.init(this)
        super.onCreate()
        context = applicationContext
    }
}