package com.yl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.network.manager.CookiesManager
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.repository.UserRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 设置界面ViewModel
 * @author YL Chen
 * @date 2025/3/5 21:07
 * @version 1.0
 */
class SettingActivityViewModel : BaseViewModel() {

    val gotoPrivacy = MutableLiveData<Boolean>()
    val gotoUserInfo = MutableLiveData<Boolean>()

    //跳转到个人信息界面
    fun onUserInfoClick() {
        gotoUserInfo.value = true
    }

    //跳转到隐私政策页面
    fun onPrivacyClick() {
        gotoPrivacy.value = true
    }


    //退出登录
    fun logout() {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
            },
            requestCall = {
                val logout = LoginAndRegisterRepository.logout()
                //清除Room中存储的用户信息
                UserRepository.clearUser()
                LogUtils.d(this@SettingActivityViewModel,"logout-->$logout")
            }
        )
    }

    override fun onReload() {

    }
}