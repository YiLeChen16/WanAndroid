package com.yl.wanandroid.room

import android.content.Context
import androidx.room.Room
import com.yl.wanandroid.base.BaseApplication

/**
 * @description: 数据库的创建者 & 负责数据库版本更新的具体实现者
 * @author YL Chen
 * @date 2024/11/10 17:03
 * @version 1.0
 */
class DBInstance {
    // 单例数据库实例
    companion object {
        val DB_NAME = "wan_android_database"

        @Volatile
        private var INSTANCE: WanAndroidDataBase? = null

        fun getDatabase(): WanAndroidDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    BaseApplication.context,
                    WanAndroidDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}