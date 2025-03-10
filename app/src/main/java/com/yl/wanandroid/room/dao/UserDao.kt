package com.yl.wanandroid.room.dao

import android.provider.ContactsContract.CommonDataKinds.Nickname
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
    fun getUser():List<UserItem>

    @Query("UPDATE users SET nickName = :nickname,email = :email")
    fun updateUserNickNameAndEmail(nickname: String,email:String)

    @Query("UPDATE users SET sex = :sex,birthday = :birthday")
    fun updateUserSexAndBirthday(sex:String?,birthday:String?)
}