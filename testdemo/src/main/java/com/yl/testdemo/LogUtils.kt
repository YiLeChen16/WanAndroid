package com.yl.testdemo

import android.util.Log

/**
 * @description: 日志工具类，用于规范日志输出
 * @author YL Chen
 * @date 2024/9/8 15:34
 * @version 1.0
 */
object LogUtils {

    const val NONE = 0
    const val INFO = 1
    const val DEBUG = 2
    const val ERROR = 3

    //当CURRENT==NONE时，日志均不输出
    //当CURRENT==ERROR时，日志均输出
    const val CURRENT = ERROR


    fun i(any: Any, msg: String) {
        if (CURRENT >= 1) {
            Log.i(any.javaClass.simpleName, msg)
        }
    }

    fun d(any: Any, msg: String) {
        if (CURRENT >= 2) {
            Log.d(any.javaClass.simpleName, msg)
        }
    }

    fun e(any: Any, msg: String) {
        if (CURRENT >= 3) {
            Log.e(any.javaClass.simpleName, msg)
        }
    }

}