package com.yl.wanandroid.repository.login

import com.yl.wanandroid.Constant
import com.yl.wanandroid.model.User
import com.yl.wanandroid.network.WanAndroidApiInterface
import com.yl.wanandroid.network.manager.CookiesManager
import com.yl.wanandroid.repository.my.UserRepository.getUser
import com.yl.wanandroid.repository.base.BaseRepository
import com.yl.wanandroid.utils.AESUtil
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @description: 登录注册模块仓库
 * @author YL Chen
 * @date 2025/2/22 17:32
 * @version 1.0
 */
object LoginAndRegisterRepository : BaseRepository() {


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
     * 自动登录.实时获取用户的个人信息数据
     * @return User?
     */
    suspend fun autoLogin(): User? {
        if (!isUserLogin()) return null
        val user = getUser()
        //对取出的用户密码进行解密
        val decryptPassword = AESUtil.decrypt(user[0].password, Constant.KEY_PASSWORD)
        return requestResponse {
            WanAndroidApiInterface.api.login(
                user[0].account,
                decryptPassword
            )
        }
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
     * 退出登录
     * @return Boolean
     */
    suspend fun logout(): Boolean {
        withContext(Dispatchers.IO) {
            val result = WanAndroidApiInterface.api.logout()
            if (result?.isFailed() == true) {
                TipsToast.showTips(result.errorMsg)
                return@withContext false
            }
            true
        }.let {
            return it
        }
    }



    /**
     * 用户是否登录
     * @return Boolean
     */
    fun isUserLogin(): Boolean {
        LogUtils.d(this, "isUserLogin-->${CookiesManager.getCookies().isNullOrEmpty()}")
        return !CookiesManager.getCookies().isNullOrEmpty()
    }
}