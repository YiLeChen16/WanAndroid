package com.yl.wanandroid.repository

import com.yl.wanandroid.room.WanAndroidDataBase
import com.yl.wanandroid.room.dao.SearchHistoryDao
import com.yl.wanandroid.room.entity.SearchHistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description: 历史搜索记录数据仓库
 * @author YL Chen
 * @date 2024/11/10 16:27
 * @version 1.0
 */
class SearchHistoryRepository(private val db: WanAndroidDataBase) {

    suspend fun getSearchHistoryItems(): List<SearchHistoryItem> =
        withContext(Dispatchers.IO) {
            db.searchHistoryDao().getAll()
        }

    suspend fun insertSearchHistoryItem(query: String,time:Long) =
        withContext(Dispatchers.IO) {
            val searchHistoryItems = db.searchHistoryDao().getAll()
            var sameId = -1
            searchHistoryItems.forEach { item ->
                // 记录相同id
                if(item.query == query){
                    sameId = item.id
                }
            }
            if (sameId != -1){
                db.searchHistoryDao().delete(sameId)
            }
            db.searchHistoryDao().insert(SearchHistoryItem(query = query, timestamp = time))
        }

    suspend fun deleteSearchHistoryItem(id: Int) =
        withContext(Dispatchers.IO) {
            db.searchHistoryDao().delete(id)
        }

    suspend fun deleteAllSearchHistory() =
        withContext(Dispatchers.IO) {
            db.searchHistoryDao().deleteAll()
        }
}
