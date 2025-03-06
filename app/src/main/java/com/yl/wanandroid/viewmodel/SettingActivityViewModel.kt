package com.yl.wanandroid.viewmodel

import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.network.manager.CookiesManager
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import okhttp3.Cookie

/**
 * @description: 设置界面ViewModel
 * @author YL Chen
 * @date 2025/3/5 21:07
 * @version 1.0
 */
class SettingActivityViewModel : BaseViewModel() {

    //跳转到个人信息界面
    fun onUserInfoClick() {

    }

    //跳转到隐私政策页面
    fun onPrivacyClick() {

    }


    //退出登录
    fun logout() {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
            },
            requestCall = {
                val logout = LoginAndRegisterRepository.logout()
                LoginAndRegisterRepository.clearUser()
                CookiesManager.clearCookies()
                LogUtils.d(this@SettingActivityViewModel,"logout-->$logout")
            }
        )
    }

    override fun onReload() {

    }
}