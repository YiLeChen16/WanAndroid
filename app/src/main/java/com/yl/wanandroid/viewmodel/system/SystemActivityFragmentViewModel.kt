package com.yl.wanandroid.viewmodel.system

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.SystemArticleDataBean
import com.yl.wanandroid.repository.SystemRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: TODO
 * @author YL Chen
 * @date 2025/2/7 16:34
 * @version 1.0
 */
class SystemActivityFragmentViewModel : BaseViewModel() {

    var cid = 0
    private val systemRepository: SystemRepository? = getRepository<SystemRepository>()

    private val DEFAULT_PAGE: Int = 0//默认加载页码

    private var currentPage = DEFAULT_PAGE//当前页码

    //页面的数据
    var systemArticleData = MutableLiveData<SystemArticleDataBean?>()
    //加载更多的页面数据
    var loadMoreSystemArticleData = MutableLiveData<SystemArticleDataBean?>()



    /**
     *获取对应cid的system下的文章
     * @param cids Int
     */
    fun getSystemArticleDataByCid(cid: Int) {
        this.cid = cid
        //将当前加载页数也重置为0
        currentPage = DEFAULT_PAGE
        launchUI(
            errorCallback = { _, errorMSg ->
                TipsToast.showTips(errorMSg)
                systemArticleData.value = null
            },
            requestCall = {
                //将获取到的数据存入map集合中
                systemArticleData.value =
                    systemRepository?.getSystemArticleDataByCid(DEFAULT_PAGE, cid)
            }
        )
    }

    //加载更多
    fun loadMOreSystemArticleDataByCid(){
        //页数加一
        currentPage++
        launchUI(
            errorCallback = {
                _,errorMSg->
                TipsToast.showTips(errorMSg)
                loadMoreSystemArticleData.value = null
                //页数回退
                currentPage--
            }, requestCall = {
                loadMoreSystemArticleData.value = systemRepository?.getSystemArticleDataByCid(currentPage,cid)
            }
        )
    }


    override fun onReload() {
        getSystemArticleDataByCid(cid)
    }
}