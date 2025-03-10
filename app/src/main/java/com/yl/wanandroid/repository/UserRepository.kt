package com.yl.wanandroid.repository

import com.yl.wanandroid.Constant
import com.yl.wanandroid.room.DBInstance
import com.yl.wanandroid.room.entity.UserItem
import com.yl.wanandroid.utils.AESUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description: 用户信息仓库
 * @author YL Chen
 * @date 2025/3/9 21:55
 * @version 1.0
 */
object UserRepository {
    private val db = DBInstance.getDatabase()

    /**
     * 保存用户信息到数据库
     * @param user UserItem
     */
    suspend fun saveUser(user: UserItem) {
        withContext(Dispatchers.IO) {
            //对密码进行加密存储
            val encryptPassword = AESUtil.encrypt(user.password, Constant.KEY_PASSWORD)
            db.userDao().saveUserInfo(
                UserItem(
                    user.account,
                    encryptPassword,
                    user.headIcon,
                    user.nickName,
                    user.sex,
                    user.birthday,
                    user.email
                )
            )
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
    suspend fun getUser(): List<UserItem> =
        withContext(Dispatchers.IO) {
            db.userDao().getUser()
        }

    /**
     * 更新部分用户可能修改的数据
     * @param account String
     * @param email String
     */
    suspend fun updateUserNickNameAndEmail(account: String, email: String) =
        withContext(Dispatchers.IO) {
            db.userDao().updateUserNickNameAndEmail(account, email)
        }

    /**
     * 更新用户可能在客户端修改的数据
     * @param sex String?
     * @param birthday String?
     */
    suspend fun updateUserSexAndBirthday(sex: String?, birthday: String?) {
        withContext(Dispatchers.IO) {
            db.userDao().updateUserSexAndBirthday(sex, birthday)
        }
    }
}