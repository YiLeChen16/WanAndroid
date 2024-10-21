package com.yl.wanandroid.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.model.RecommendBlogDataBean
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.repository.RecommendRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 首页推荐页ViewModel
 * @author YL Chen
 * @date 2024/10/20 16:04
 * @version 1.0
 */
class RecommendFragmentViewModel:BaseViewModel() {

    private var recommendRepository:RecommendRepository? = getRepository()

    //推荐博客数据
    var recommendBlogData = MutableLiveData<RecommendBlogDataBean?>()
    //搜索热词数据
    var searchHotKeyData = MutableLiveData<MutableList<SearchHotKeyDataBean>?>()

    //默认加载第一页的数据
    val mDefaultPageSize = 0

    //记录当前推荐博客加载页数
    private var mCurrentPageSize = MutableLiveData(mDefaultPageSize)//初始化为0

    /**
     * 获取首页推荐博客数据
     *
     * @return LiveData<RecommendBlogDataBean?>
     */
    fun getRecommendBlogData(): LiveData<RecommendBlogDataBean?> {
        if (recommendBlogData.value?.datas.isNullOrEmpty()){
            launchUI(
                errorCallback = {errorCode,errorMsg->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@RecommendFragmentViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    recommendBlogData.value = null
                },
                requestCall = {
                    recommendBlogData.value = recommendRepository?.getRecommendBlogData(mCurrentPageSize.value!!)
                }
            )
        }
        return recommendBlogData
    }

    fun getSearchHotkeyData():LiveData<MutableList<SearchHotKeyDataBean>?>{
        if(searchHotKeyData.value == null){
            launchUI(
                errorCallback = {errorCode,errorMsg->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@RecommendFragmentViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    searchHotKeyData.value = null
                },
                requestCall = {
                    searchHotKeyData.value = recommendRepository?.getSearchHotKeyData()
                }
            )
        }
        return searchHotKeyData
    }

    //TODO::加载更多推荐博客数据

    //重新加载数据
    override fun onReload() {
        getRecommendBlogData()
        getSearchHotkeyData()
    }
}