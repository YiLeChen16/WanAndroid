package com.yl.wanandroid.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yl.wanandroid.room.dao.SearchHistoryDao
import com.yl.wanandroid.room.entity.SearchHistoryItem

//数据库
@Database(entities = [SearchHistoryItem::class], version = 1)
abstract class WanAndroidDataBase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}