package com.yl.wanandroid.repository

import com.yl.wanandroid.model.HarmonyColumnDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: HarmonyFragment仓库
 * @author YL Chen
 * @date 2024/11/12 21:45
 * @version 1.0
 */
object HarmonyRepository:BaseRepository() {

    /**
     * 获取鸿蒙专栏数据
     * @return HarmonyColumnDataBean?
     */
    suspend fun getHarmonyColumnData():HarmonyColumnDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getHarmonyColumnData()
        }
    }
}