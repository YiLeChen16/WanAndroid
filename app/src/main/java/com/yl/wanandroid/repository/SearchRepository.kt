package com.yl.wanandroid.repository

import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.model.SearchResultDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: 搜索界面的仓库
 * @author YL Chen
 * @date 2024/10/21 19:32
 * @version 1.0
 */
class SearchRepository:BaseRepository() {
    //获取搜索热词数据
    suspend fun getSearchHotKeyData():MutableList<SearchHotKeyDataBean>?{
        return requestResponse {
            WanAndroidApiInterface.api.getSearchHotkey()
        }
    }

    //获取搜索结果列表数据
    suspend fun getSearchResultData(page:Int,k:String):SearchResultDataBean?{
        return requestResponse {
            WanAndroidApiInterface.api.getSearchResult(page,k)
        }
    }
}