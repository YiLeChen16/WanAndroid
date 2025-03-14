package com.yl.wanandroid.viewmodel.collect

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.repository.collect.CollectRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 收藏ViewModel
 * @author YL Chen
 * @date 2025/2/26 14:06
 * @version 1.0
 */
class CollectViewModel : BaseViewModel() {

    var collectArticles = MutableLiveData<ArticleDataBean?>()
    var moreCollectArticles = MutableLiveData<ArticleDataBean?>()//更多数据
    private val mDefaultPage: Int = 0//默认加载页码

    private var currentPage = mDefaultPage//当前页码

    //获取所有收藏文章
    fun getAllCollectArticle() {
        currentPage = mDefaultPage//重置页面
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
                collectArticles.value = null
            },
            requestCall = {
                val allCollectArticle = CollectRepository.getAllCollectArticle(mDefaultPage)
                //由于收藏接口返回的json不包含collect字段,故会将collect字段置为false,为了方便初始化收藏按钮状态,需手动将collect字段置为true
                for (data in allCollectArticle?.datas!!) {
                    data.collect = true
                }
                collectArticles.value = allCollectArticle
            })
    }


    //加载更多
    fun getMoreAllCollectArticle() {
        currentPage++
        launchUI(errorCallback = { _, errMsg ->
            TipsToast.showTips(errMsg)
            currentPage--
            moreCollectArticles.value = null
        }, requestCall = {
            moreCollectArticles.value = CollectRepository.getAllCollectArticle(currentPage)
        })
    }

    override fun onReload() {
        getAllCollectArticle()
    }
}