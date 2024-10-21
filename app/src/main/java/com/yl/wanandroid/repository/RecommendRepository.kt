package com.yl.wanandroid.repository

import com.yl.wanandroid.model.RecommendBlogDataBean
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 首页推荐页仓库
 * @author YL Chen
 * @date 2024/10/20 21:55
 * @version 1.0
 */
class RecommendRepository:BaseRepository() {
    /**
     * 网络请求首页推荐博客数据
     * @param pageSize Int
     * @return RecommendBlogDataBean?
     */
    suspend fun getRecommendBlogData(pageSize:Int): RecommendBlogDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getRecommendBlog(pageSize)
        }
    }


    suspend fun getSearchHotKeyData():MutableList<SearchHotKeyDataBean>?{
        return requestResponse {
            WanAndroidApiInterface.api.getSearchHotkey()
        }
    }
}