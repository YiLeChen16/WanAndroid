package com.yl.wanandroid.viewmodel

import androidx.databinding.ObservableField
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.repository.UserRepository
import com.yl.wanandroid.room.entity.UserItem
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 用户信息界面ViewModel
 * @author YL Chen
 * @date 2025/3/8 14:14
 * @version 1.0
 */
class UserInfoActivityViewModel : BaseViewModel() {
    val user = ObservableField<UserItem>()

    fun getUserInfo() {
        launchUI(
            errorCallback = { _, errMsg ->
                user.set(null)
                TipsToast.showTips(errMsg)
            },
            requestCall = {
                user.set(UserRepository.getUser()[0])
            }
        )
    }

    /**
     * 保存用户可修改的数据
     * @param sex String
     * @param birthday String
     */
    fun saveUserInfo(sex: String?, birthday: String?) {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
            },
            requestCall = {
                UserRepository.updateUserSexAndBirthday(sex, birthday)
            }
        )
    }

    override fun onReload() {
        getUserInfo()
    }


}