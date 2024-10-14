package com.yl.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.repository.HomeRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @description: 首页的ViewModel
 * @author YL Chen
 * @date 2024/9/7 15:45
 * @version 1.0
 */
class HomeFragmentViewModel : BaseViewModel() {

    //获取仓库模型
    private var homeRepository: HomeRepository? = getRepository()

    var bannerData = MutableLiveData<MutableList<BannerDataBean>?>()

    /**
     * 获取Banner数据
     */
    fun getBannerData(): LiveData<MutableList<BannerDataBean>?> {
        //切换到主线程运行，因为Toast需要在UI线程中调用
        if (bannerData.value.isNullOrEmpty()) {
            launchUI(
                //请求失败回调
                errorCallback = { errorCode, errorMsg ->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@HomeFragmentViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    bannerData.value = null
                },
                //网络请求
                requestCall = {
                    bannerData.value = homeRepository?.getBannerData()
                }
            )
        }
        return bannerData
    }
}