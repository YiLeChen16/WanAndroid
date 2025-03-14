package com.yl.wanandroid.viewmodel.my.setting

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.repository.login.LoginAndRegisterRepository
import com.yl.wanandroid.repository.my.SettingRepository
import com.yl.wanandroid.repository.my.UserRepository
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
    val gotoAbout = MutableLiveData<Boolean>()

    val cache = ObservableField<String>()

    //跳转到个人信息界面
    fun onUserInfoClick() {
        gotoUserInfo.value = true
    }

    //跳转到隐私政策页面
    fun onPrivacyClick() {
        gotoPrivacy.value = true
    }

    //跳转到关于页面
    fun onAboutClick() {
        gotoAbout.value = true
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
                LogUtils.d(this@SettingActivityViewModel, "logout-->$logout")
            }
        )
    }


    //获取缓存
    fun getTotalCache() {
        launchUI(errorCallback = { _, errMsg ->
            LogUtils.d(this, "errMsg-->$errMsg")
        }, requestCall = {
                cache.set(SettingRepository.getTotalCache())
            })
    }

    //清理缓存
    fun onClearCacheClick() {
        launchUI(
            errorCallback =
            { _, errMsg ->
                TipsToast.showTips(errMsg)
            }, requestCall = {
                SettingRepository.clearCache()
                cache.set(SettingRepository.getTotalCache())
            }
        )
    }

    override fun onReload() {

    }
}