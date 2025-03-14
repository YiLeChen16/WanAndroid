package com.yl.wanandroid.viewmodel.login

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.User
import com.yl.wanandroid.repository.login.LoginAndRegisterRepository
import com.yl.wanandroid.repository.my.UserRepository
import com.yl.wanandroid.room.entity.UserItem
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
            if (user.value == null)return@launchUI
            UserRepository.clearUser()//清除本地数据
            UserRepository.saveUser(UserItem(user.value?.username!!,password,"",user.value?.nickname,"","",user.value?.email))
        })
    }



    override fun onReload() {

    }
}