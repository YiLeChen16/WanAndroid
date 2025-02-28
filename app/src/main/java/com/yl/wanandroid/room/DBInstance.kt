package com.yl.wanandroid.room

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        private const val DB_NAME = "wan_android_database"

        //数据库更新迁移
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE users(account TEXT PRIMARY KEY NOT NULL)")
            }

        }

        @Volatile
        private var INSTANCE: WanAndroidDataBase? = null

        fun getDatabase(): WanAndroidDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    BaseApplication.context, WanAndroidDataBase::class.java, DB_NAME
                ).addMigrations(MIGRATION_1_2)//添加迁移
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}