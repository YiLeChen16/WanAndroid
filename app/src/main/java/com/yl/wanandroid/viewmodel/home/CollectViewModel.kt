package com.yl.wanandroid.viewmodel.home

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.repository.CollectRepository
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.utils.getStringFromResource

/**
 * @description: 收藏ViewModel//TODO
 * @author YL Chen
 * @date 2025/2/26 14:06
 * @version 1.0
 */
class CollectViewModel : BaseViewModel() {

    var collectArticles = MutableLiveData<ArticleDataBean?>()
    val repository = getRepository<CollectRepository>()
    private val DEFAULT_PAGE: Int = 0//默认加载页码

    private var currentPage = DEFAULT_PAGE//当前页码

    //获取所有收藏文章
    fun getAllCollectArticle() {
        launchUI(
            errorCallback = { _, errMsg ->
                TipsToast.showTips(errMsg)
                collectArticles.value = null
            },
            requestCall = {
                val allCollectArticle = repository?.getAllCollectArticle(DEFAULT_PAGE)
                //由于收藏接口返回的json不包含collect字段,故会将collect字段置为false,为了方便初始化收藏按钮状态,需手动将collect字段置为true
                for (data in allCollectArticle?.datas!!) {
                    data.collect = true
                }
                collectArticles.value = allCollectArticle
            })
    }



    //TODO::加载更多

    override fun onReload() {
        getAllCollectArticle()
    }
}