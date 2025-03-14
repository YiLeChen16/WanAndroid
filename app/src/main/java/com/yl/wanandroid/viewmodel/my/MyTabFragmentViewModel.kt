package com.yl.wanandroid.viewmodel.my

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.repository.my.MyRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description:
 * @author YL Chen
 * @date 2025/3/2 15:03
 * @version 1.0
 */
class MyTabFragmentViewModel : BaseViewModel() {
    private val DEFAULT_PAGE: Int = 0//默认加载页码


    private var currentPage = DEFAULT_PAGE//当前页码

    var id = 0//微信公众号id


    override fun onReload() {
        getWxArticlesByWxId()
    }

    val wxArticles = MutableLiveData<ArticleDataBean?>()
    val moreWxArticles = MutableLiveData<ArticleDataBean?>()

    //获取某个公众号的文章数据
    fun getWxArticlesByWxId() {
        currentPage = DEFAULT_PAGE
        launchUI(
            errorCallback =
            { _, errMsg ->
                TipsToast.showTips(errMsg)
                wxArticles.value = null
            },
            requestCall = {
                wxArticles.value = MyRepository.getWxArticlesByWxId(id, DEFAULT_PAGE)
            })
    }

    //加载更多
    fun getMoreWxArticlesByWxId() {
        currentPage++
        launchUI(
            errorCallback =
            { _, errMsg ->
                TipsToast.showTips(errMsg)
                currentPage--
                moreWxArticles.value = null
            },
            requestCall = {
                moreWxArticles.value = MyRepository.getWxArticlesByWxId(id, currentPage)
            })
    }
}