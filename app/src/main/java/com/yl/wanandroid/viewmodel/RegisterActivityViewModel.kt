package com.yl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.User
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 注册页面ViewModel
 * @author YL Chen
 * @date 2025/2/22 17:24
 * @version 1.0
 */
class RegisterActivityViewModel : BaseViewModel() {

    val user = MutableLiveData<User?>()

    fun register(account: String, password: String, passwordAgain: String) {
        launchUI(
            errorCallback = { _, errMSg ->
                TipsToast.showWarningTips(errMSg)
                user.value = null
            },
            requestCall = {
                user.value = LoginAndRegisterRepository.register(account, password, passwordAgain)
            }
        )
    }

    override fun onReload() {

    }
}