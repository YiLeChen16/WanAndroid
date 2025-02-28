package com.yl.wanandroid.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yl.wanandroid.room.dao.SearchHistoryDao
import com.yl.wanandroid.room.dao.UserDao
import com.yl.wanandroid.room.entity.SearchHistoryItem
import com.yl.wanandroid.room.entity.UserItem

//数据库
@Database(entities = [SearchHistoryItem::class,UserItem::class], version = 2)
abstract class WanAndroidDataBase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun userDao(): UserDao
}

