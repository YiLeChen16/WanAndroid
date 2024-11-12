package com.yl.wanandroid.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.yl.wanandroid.room.entity.SearchHistoryItem

/**
 * 历史数据增删改查接口
 */
@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history Order BY timestamp")
    fun getAll(): List<SearchHistoryItem>

    @Insert
    fun insert(searchHistoryItem: SearchHistoryItem) // 插入 SearchHistoryItem 对象

    @Query("DELETE FROM search_history WHERE id = :id")
    fun delete(id:Int) // 删除指定的历史项

    @Query("DELETE FROM search_history")
    fun deleteAll()  // 删除所有历史记录
}
