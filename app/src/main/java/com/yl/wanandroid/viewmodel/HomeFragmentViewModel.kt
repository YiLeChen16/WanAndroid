package com.yl.wanandroid.viewmodel

<<<<<<< HEAD
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.repository.HomeRepository
import com.yl.wanandroid.utils.TipsToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
=======
import com.yl.wanandroid.base.BaseViewModel
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

/**
 * @description: 首页的ViewModel
 * @author YL Chen
 * @date 2024/9/7 15:45
 * @version 1.0
 */
<<<<<<< HEAD
class HomeFragmentViewModel @Inject constructor() :BaseViewModel(){

    @Inject lateinit var homeRepository:HomeRepository

    var bannerData = MutableLiveData<MutableList<BannerDataBean>?>()

    /**
     * 获取Banner数据
     */
    fun getBannerData() :LiveData<MutableList<BannerDataBean>?>{
        //切换到主线程运行，因为Toast需要在UI线程中调用
        if(bannerData.value.isNullOrEmpty()){
            launchUI(
                //请求失败回调
                errorCallback = {
                        errorCode,errorMsg->
                    TipsToast.showTips(errorMsg)
                    bannerData.value = null
                },
                //网络请求
                requestCall = {
                    bannerData.value = homeRepository.getBannerData()
                }
            )
        }
        return bannerData
    }
=======
class HomeFragmentViewModel :BaseViewModel(){
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
}