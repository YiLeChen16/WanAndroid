package com.yl.wanandroid.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户信息实体类
 * @property account String
 * @constructor
 */
@Entity(tableName = "users")
data class UserItem(
    @PrimaryKey
    @ColumnInfo(name = "account")
    val account: String
)
