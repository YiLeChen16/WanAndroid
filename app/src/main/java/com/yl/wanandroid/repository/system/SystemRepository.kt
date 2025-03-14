package com.yl.wanandroid.repository.system

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: System页面仓库
 * @author YL Chen
 * @date 2024/12/18 20:35
 * @version 1.0
 */
object SystemRepository : BaseRepository() {

    suspend fun getSystemData(): MutableList<SystemDataBeanItem>? {
        return requestResponse {
            WanAndroidApiInterface.api.getSystemData()
        }
    }

    suspend fun getSystemArticleDataByCid(page:Int, cid:Int): ArticleDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getSystemArticleDataByCid(page,cid)
        }
    }

}