package com.yl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.User
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 登录界面ViewModel
 * @author YL Chen
 * @date 2025/2/18 15:52
 * @version 1.0
 */
class LoginActivityViewModel : BaseViewModel() {

    val user = MutableLiveData<User?>()//登录返回的用户信息


    /**
     * 登录
     * @param userName String
     * @param password String
     */
    fun login(userName: String, password: String) {
        launchUI(errorCallback = { _, errMsg ->
            TipsToast.showTips(errMsg)
            user.value = null
        }, requestCall = {
            user.value = LoginAndRegisterRepository.login(userName, password)
            //将用户数据保存到本地
            LoginAndRegisterRepository.clearUser()//清除本地数据
            LoginAndRegisterRepository.saveUser(userName)//存储新数据
        })
    }



    override fun onReload() {

    }
}