package com.yl.wanandroid.repository

import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository

/**
 * @description: TODO
 * @author YL Chen
 * @date 2024/10/21 19:32
 * @version 1.0
 */
class SearchActivityRepository:BaseRepository() {
    suspend fun getSearchHotKeyData():MutableList<SearchHotKeyDataBean>?{
        return requestResponse {
            WanAndroidApiInterface.api.getSearchHotkey()
        }
    }
}