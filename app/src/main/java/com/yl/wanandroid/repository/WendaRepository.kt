package com.yl.wanandroid.repository

import com.yl.wanandroid.model.ItemData
import com.yl.wanandroid.model.NormalWendaDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 问答Fragment仓库
 * @author YL Chen
 * @date 2024/11/19 22:08
 * @version 1.0
 */
class WendaRepository : BaseRepository() {
    suspend fun getHotWendaData(): MutableList<ItemData>? {
        return requestResponse {
            WanAndroidApiInterface.api.getHotWendaData()
        }
    }

    suspend fun getNormalWendaData(page: Int): NormalWendaDataBean? {
        return requestResponse {
            WanAndroidApiInterface.api.getNormalWendaData(page)
        }
    }
}