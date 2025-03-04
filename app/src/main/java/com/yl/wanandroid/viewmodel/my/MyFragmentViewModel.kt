package com.yl.wanandroid.viewmodel.my

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.UserDataBean
import com.yl.wanandroid.repository.MyRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: "我的"界面对应ViewModel//TODO
 * @author YL Chen
 * @date 2024/9/7 16:05
 * @version 1.0
 */
class MyFragmentViewModel : BaseViewModel() {


    val wxArticleTabs = MutableLiveData<MutableList<Children>?>()
    private val userData = MutableLiveData<UserDataBean?>()

    val userName = ObservableField("未登录")
    val level = ObservableField("0")
    val integral = ObservableField("0")
    val rank = ObservableField("0")

    val gotoCollection = MutableLiveData<Boolean>()
    val gotoMyMessage = MutableLiveData<Boolean>()
    val gotoMyShare = MutableLiveData<Boolean>()
    val gotoMyIntegral = MutableLiveData<Boolean>()
    val gotoSearch = MutableLiveData<Boolean>()
    val themeDark = MutableLiveData<Boolean>()
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
    fun getUserInfo() {
        launchUI(
            errorCallback =
            { _, errMsg ->
                LogUtils.d(this,"errMsg-->$errMsg")
                TipsToast.showTips(errMsg)
                userData.value = null
            },
            requestCall = {
                userData.value = MyRepository.getUserInfo()
                userName.set(userData.value?.userInfo?.username)
                level.set(userData.value?.coinInfo?.level.toString())
                integral.set(userData.value?.coinInfo?.coinCount.toString())
                rank.set(userData.value?.coinInfo?.rank)
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

    fun onChangeThemeClick() {
        //变化主题
        themeDark.value = true
    }

    fun onSettingClick() {
        //跳转到设置界面
        gotoSetting.value = true
    }

    fun onMyCollectionClick() {
        //通知UI跳转到收藏界面
        gotoCollection.value = true
    }

    fun onMyMessageClick() {
        //通知UI跳转到消息界面
        gotoMyMessage.value = true
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
        getWxArticleTabs()
    }
}