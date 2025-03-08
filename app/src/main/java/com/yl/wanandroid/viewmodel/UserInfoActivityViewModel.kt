package com.yl.wanandroid.viewmodel

import androidx.databinding.ObservableField
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.UserInfo
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.repository.MyRepository
import com.yl.wanandroid.room.entity.UserItem
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 用户信息界面ViewModel
 * @author YL Chen
 * @date 2025/3/8 14:14
 * @version 1.0
 */
class UserInfoActivityViewModel :BaseViewModel(){
    val user = ObservableField<UserItem>()

    fun getUserInfo(){
        launchUI(
            errorCallback = {
                _,errMsg->
                user.set(null)
                TipsToast.showTips(errMsg)
            },
            requestCall = {
                user.set(LoginAndRegisterRepository.getUser()[0])
            }
        )
    }
    override fun onReload() {
        getUserInfo()
    }
}