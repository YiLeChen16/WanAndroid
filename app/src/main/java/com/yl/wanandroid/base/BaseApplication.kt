package com.yl.wanandroid.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @description: App基类
 * @author YL Chen
 * @date 2024/9/8 16:59
 * @version 1.0
 */
//初始化Hilt框架
@HiltAndroidApp
class BaseApplication:Application()