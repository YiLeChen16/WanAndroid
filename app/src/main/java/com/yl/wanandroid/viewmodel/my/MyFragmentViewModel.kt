package com.yl.wanandroid.viewmodel.my

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.User
import com.yl.wanandroid.model.UserDataBean
import com.yl.wanandroid.repository.login.LoginAndRegisterRepository
import com.yl.wanandroid.repository.my.MyRepository
import com.yl.wanandroid.repository.my.UserRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: "我的"界面对应ViewModel
 * @author YL Chen
 * @date 2024/9/7 16:05
 * @version 1.0
 */
class MyFragmentViewModel : BaseViewModel() {


    val wxArticleTabs = MutableLiveData<MutableList<Children>?>()
    val userData = ObservableField<UserDataBean?>()
    val user = ObservableField<User?>()


    val gotoCollection = MutableLiveData<Boolean>()
    val gotoMyShare = MutableLiveData<Boolean>()
    val gotoMyIntegral = MutableLiveData<Boolean>()
    val gotoSearch = MutableLiveData<Boolean>()
    val gotoMyInfo = MutableLiveData<Boolean>()
    val gotoSetting = MutableLiveData<Boolean>()


    //获取tab数据
    fun getWxArticleTabs() {
        launchUI(
            errorCallback =
            { _, errMsg ->
                TipsToast.showTips(errMsg)
                wxArticleTabs.value = null
            },
            requestCall = {
                wxArticleTabs.value = MyRepository.getWxArticleTabs()
            })
    }

    //获取用户信息
    fun getCoinInfo() {
        launchUI(
            errorCallback =
            { _, errMsg ->
                LogUtils.d(this, "errMsg-->$errMsg")
                TipsToast.showTips(errMsg)
                userData.set(null)
            },
            requestCall = {
                userData.set(MyRepository.getUserInfo())
            })
    }

    //由于我的信息界面的用户信息不是实时更新的,故我们此处从登录接口获取用户信息
    fun getUserInfo(){
        launchUI(
            errorCallback =
            { _, errMsg ->
                LogUtils.d(this, "errMsg-->$errMsg")
                TipsToast.showTips(errMsg)
                user.set(null)
            },
            requestCall = {
                user.set(LoginAndRegisterRepository.autoLogin())
                LogUtils.d(this,"user-->${user.get()}")
                //更新数据库中的用户信息数据
                if (user.get() == null)return@launchUI
                UserRepository.updateUserNickNameAndEmail(user.get()!!.nickname, user.get()!!.email)
            })
    }

    fun onSearchClick() {
        //跳转到搜索界面
        gotoSearch.value = true
    }

    fun onMyInfoClick() {
        //跳转到我的信息页面
        gotoMyInfo.value = true
    }


    fun onSettingClick() {
        //跳转到设置界面
        gotoSetting.value = true
    }

    fun onMyCollectionClick() {
        //通知UI跳转到收藏界面
        gotoCollection.value = true
    }


    fun onMyShareClick() {
        //通知UI跳转到分享界面
        gotoMyShare.value = true
    }

    //积分排名点击
    fun onRankLayoutClick() {
        //通知UI跳转到积分排名界面
        gotoMyIntegral.value = true
    }

    override fun onReload() {
        getUserInfo()
        getCoinInfo()
        getWxArticleTabs()
    }
}