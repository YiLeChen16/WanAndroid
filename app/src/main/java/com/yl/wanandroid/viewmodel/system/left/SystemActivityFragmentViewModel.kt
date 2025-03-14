package com.yl.wanandroid.viewmodel.system.left

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.repository.system.SystemRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 体系关键词点击跳转的展示的ViewPager下的Fragment对应的ViewModel
 * @author YL Chen
 * @date 2025/2/7 16:34
 * @version 1.0
 */
class SystemActivityFragmentViewModel : BaseViewModel() {

    var cid = 0

    private val mDefaultPage: Int = 0//默认加载页码

    private var mCurrentPage = mDefaultPage//当前页码

    //页面的数据
    var systemArticleData = MutableLiveData<ArticleDataBean?>()
    //加载更多的页面数据
    var loadMoreSystemArticleData = MutableLiveData<ArticleDataBean?>()



    /**
     *获取对应cid的system下的文章
     * @param cid Int
     */
    fun getSystemArticleDataByCid(cid: Int) {
        this.cid = cid
        //将当前加载页数也重置为0
        mCurrentPage = mDefaultPage
        launchUI(
            errorCallback = { _, errorMSg ->
                TipsToast.showTips(errorMSg)
                systemArticleData.value = null
            },
            requestCall = {
                //将获取到的数据存入map集合中
                systemArticleData.value =
                    SystemRepository.getSystemArticleDataByCid(mDefaultPage, cid)
            }
        )
    }

    //加载更多
    fun loadMOreSystemArticleDataByCid(){
        //页数加一
        mCurrentPage++
        launchUI(
            errorCallback = {
                _,errorMSg->
                TipsToast.showTips(errorMSg)
                loadMoreSystemArticleData.value = null
                //页数回退
                mCurrentPage--
            }, requestCall = {
                loadMoreSystemArticleData.value = SystemRepository.getSystemArticleDataByCid(mCurrentPage,cid)
            }
        )
    }


    override fun onReload() {
        getSystemArticleDataByCid(cid)
    }
}