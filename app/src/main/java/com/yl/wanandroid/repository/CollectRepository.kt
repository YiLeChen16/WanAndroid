package com.yl.wanandroid.repository

import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.network.result.BaseResult
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 收藏仓库
 * @author YL Chen
 * @date 2025/2/26 14:55
 * @version 1.0
 */
object CollectRepository : BaseRepository() {
    suspend fun getAllCollectArticle(page: Int): ArticleDataBean? {
        return requestResponse { WanAndroidApiInterface.api.getAllCollectArticle(page) }
    }

    suspend fun collectArticle(collectId: Int): Any? {
        return requestResponse { WanAndroidApiInterface.api.collectArticle(collectId) }
    }

    suspend fun collectOutsideArticle(title:String,author:String,link:String):Any?{
        return requestResponse {
            WanAndroidApiInterface.api.collectOutsideArticle(title,author,link)
        }
    }

    suspend fun cancelCollectArticle(id:Int):Any?{
        return requestResponse {
            WanAndroidApiInterface.api.cancelCollectArticle(id)
        }
    }

    suspend fun cancelMyCollectArticle(id:Int,originId:Int = -1):Any?{
        return requestResponse {
            WanAndroidApiInterface.api.cancelMyCollectArticle(id,originId)
        }
    }

}