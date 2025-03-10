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
    val account: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "headIcon")
    val headIcon: String?,

    @ColumnInfo(name = "nickName")
    val nickName: String?,

    @ColumnInfo(name = "sex")
    val sex: String?,

    @ColumnInfo(name = "birthday")
    val birthday: String?,

    @ColumnInfo(name = "email")
    val email: String?,
)
