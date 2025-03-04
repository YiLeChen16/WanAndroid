package com.yl.wanandroid.repository

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.UserDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: "我的"界面仓库
 * @author YL Chen
 * @date 2025/3/2 13:41
 * @version 1.0
 */
object MyRepository  : BaseRepository() {

    suspend fun getWxArticleTabs(): MutableList<Children>? {
        return requestResponse {
            WanAndroidApiInterface.api.getWxArticleTabs()
        }
    }

    suspend fun getWxArticlesByWxId(id: Int, page: Int): ArticleDataBean? {
        return requestResponse {
            WanAndroidApiInterface.api.getWxArticlesByWxId(id, page)
        }
    }

    suspend fun getUserInfo():UserDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getUserInfo()
        }
    }
}