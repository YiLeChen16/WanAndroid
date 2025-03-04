package com.yl.wanandroid.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.model.ArticleDataBean
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
class RecommendFragmentViewModel : BaseViewModel() {

    //推荐博客数据
    var recommendBlogData = MutableLiveData<ArticleDataBean?>()

    //加载更多推荐博客数据
    var loadMoreRecommendBlogData = MutableLiveData<ArticleDataBean?>()

    //搜索热词数据
    var searchHotKeyData = MutableLiveData<MutableList<SearchHotKeyDataBean>?>()

    //默认加载第一页
    private val mDefaultPage = 0

    //记录当前推荐博客加载页数
    private var mCurrentPage = mDefaultPage//初始化为0

    /**
     * 获取首页推荐博客数据
     *
     * @return LiveData<RecommendBlogDataBean?>
     */
    fun getRecommendBlogData(): LiveData<ArticleDataBean?> {
        //将当前加载页数也重置为0
        mCurrentPage = mDefaultPage
        launchUI(
            errorCallback = { _, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@RecommendFragmentViewModel, "errorCallback-->$errorMsg")
                changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                recommendBlogData.value = null
            },
            requestCall = {
                recommendBlogData.value =
                    RecommendRepository.getRecommendBlogData(mDefaultPage)
            }
        )
        return recommendBlogData
    }

    fun getSearchHotkeyData(): LiveData<MutableList<SearchHotKeyDataBean>?> {
        launchUI(
            errorCallback = { _, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@RecommendFragmentViewModel, "errorCallback-->$errorMsg")
                changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                searchHotKeyData.value = null
            },
            requestCall = {
                searchHotKeyData.value = RecommendRepository.getSearchHotKeyData()
            }
        )
        return searchHotKeyData
    }

    //加载更多推荐博客数据
    fun loadMoreRecommendBlogData() {
        //当前页码数+1
        mCurrentPage++

        launchUI(
            errorCallback = { _, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@RecommendFragmentViewModel, "errorCallback-->$errorMsg")
                loadMoreRecommendBlogData.value = null
                //请求失败将页码数-1
                mCurrentPage--
            },
            requestCall = {
                //网络请求数据
                loadMoreRecommendBlogData.value =
                    RecommendRepository.getRecommendBlogData(mCurrentPage)
            }
        )
    }


    //重新加载数据
    override fun onReload() {
        getRecommendBlogData()
        getSearchHotkeyData()
    }
}