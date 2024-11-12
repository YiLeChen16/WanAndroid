package com.yl.wanandroid.repository

import com.yl.wanandroid.room.WanAndroidDataBase
import com.yl.wanandroid.room.dao.SearchHistoryDao
import com.yl.wanandroid.room.entity.SearchHistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * @description: 历史搜索记录数据仓库
 * @author YL Chen
 * @date 2024/11/10 16:27
 * @version 1.0
 */
class SearchHistoryRepository(private val db: WanAndroidDataBase) {
    private val mutex = Mutex()


    suspend fun getSearchHistoryItems(): List<SearchHistoryItem> =
        withContext(Dispatchers.IO) {
            db.searchHistoryDao().getAll()
        }

    suspend fun insertSearchHistoryItem(query: String, time: Long) =
        mutex.withLock {//保证同时只有一个协程可操作此函数:避免数据重复添加
            withContext(Dispatchers.IO) {
                db.runInTransaction {//将多个数据库操作包装为一个事务,要么同时成功,要么同时失败
                    val searchHistoryItems = db.searchHistoryDao().getAll()
                    val sameId = mutableListOf<Int>()
                    searchHistoryItems.forEach { item ->
                        // 记录相同id
                        if (item.query == query) {
                            sameId.add(item.id)
                        }
                    }
                    if (sameId.size > 0) {
                        for (id in sameId) {
                            db.searchHistoryDao().delete(id)
                        }
                    }
                    db.searchHistoryDao().insert(SearchHistoryItem(query = query, timestamp = time))
                }
            }
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
