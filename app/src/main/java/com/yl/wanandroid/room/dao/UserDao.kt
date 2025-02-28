package com.yl.wanandroid.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yl.wanandroid.room.entity.UserItem

/**
 * @description: 用户信息增删改查接口
 * @author YL Chen
 * @date 2025/2/26 10:37
 * @version 1.0
 */
@Dao
interface UserDao {
    @Insert
    fun saveUserInfo(user: UserItem)

    @Query("DELETE FROM users")
    fun clearUser()

    @Query("SELECT * FROM users")
    fun getUser():List<UserItem>?
}