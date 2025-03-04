package com.yl.wanandroid.repository

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 问答Fragment仓库
 * @author YL Chen
 * @date 2024/11/19 22:08
 * @version 1.0
 */
object WendaRepository : BaseRepository() {
    /**
     * 获取热门问答数据
     * @return MutableList<ArticleItemData>?
     */
    suspend fun getHotWendaData(): MutableList<ArticleItemData>? {
        return requestResponse {
            WanAndroidApiInterface.api.getHotWendaData()
        }
    }

    /**
     * 获取普通问答数据
     * @param page Int
     * @return NormalWendaDataBean?
     */
    suspend fun getNormalWendaData(page: Int): ArticleDataBean? {
        return requestResponse {
            WanAndroidApiInterface.api.getNormalWendaData(page)
        }
    }
}