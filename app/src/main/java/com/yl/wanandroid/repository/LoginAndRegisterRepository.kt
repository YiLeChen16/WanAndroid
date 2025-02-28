package com.yl.wanandroid.repository

import com.yl.wanandroid.model.User
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.repository.base.BaseRepository
import com.yl.wanandroid.room.WanAndroidDataBase
import com.yl.wanandroid.room.entity.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description: 登录注册模块仓库
 * @author YL Chen
 * @date 2025/2/22 17:32
 * @version 1.0
 */
class LoginAndRegisterRepository(val db: WanAndroidDataBase) : BaseRepository() {
    /**
     * 登录
     * @param userName String
     * @param password String
     * @return User?
     */
    suspend fun login(userName: String, password: String): User? {
        return requestResponse { WanAndroidApiInterface.api.login(userName, password) }
    }

    /**
     * 注册
     * @param userName String
     * @param password String
     * @param confirmPassword String
     * @return User?
     */
    suspend fun register(userName: String, password: String, confirmPassword: String): User? {
        return requestResponse {
            WanAndroidApiInterface.api.register(
                userName,
                password,
                confirmPassword
            )
        }
    }

    /**
     * 保存用户信息到数据库
     * @param account String
     * @param password String
     */
    suspend fun saveUser(account: String) {
        withContext(Dispatchers.IO) {
            db.userDao().saveUserInfo(UserItem(account))
        }
    }

    /**
     * 清除用户数据
     */
    suspend fun clearUser() {
        withContext(Dispatchers.IO) {
            db.userDao().clearUser()
        }
    }

    /**
     * 获取用户数据
     * @return List<UserItem>
     */
    suspend fun getUser(): List<UserItem>? =
        withContext(Dispatchers.IO) {
            db.userDao().getUser()
        }

    /**
     * 用户是否登录
     * @return Boolean
     */
    suspend fun isUserLogin():Boolean{
        return getUser() != null
    }
}