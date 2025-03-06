package com.yl.wanandroid.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.repository.HomeRepository
import com.yl.wanandroid.repository.LoginAndRegisterRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 首页的ViewModel
 * @author YL Chen
 * @date 2024/9/7 15:45
 * @version 1.0
 */
class HomeFragmentViewModel : BaseViewModel() {

    //Banner数据
    var bannerDatas = MutableLiveData<MutableList<BannerDataBean>?>()

    //用户登录标志
    val gotoCollection = MutableLiveData<Boolean>()


    /**
     * 获取Banner数据
     */
    fun getBannerData(): LiveData<MutableList<BannerDataBean>?> {
        //切换到主线程运行，因为Toast需要在UI线程中调用
        if (bannerDatas.value.isNullOrEmpty()) {
            launchUI(
                //请求失败回调
                errorCallback = { _, errorMsg ->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@HomeFragmentViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    bannerDatas.value = null
                },
                //网络请求
                requestCall = {
                    bannerDatas.value = HomeRepository.getBannerData()
                }
            )
        }
        return bannerDatas
    }


    //首页Tab收藏按钮被点击
    fun onCollectClick() {
        gotoCollection.value = true
    }


    //错误状态视图点击回调函数
    override fun onReload() {
        //重新加载数据
        getBannerData()
    }

}