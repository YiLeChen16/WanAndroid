package com.yl.wanandroid.viewmodel.system

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.SystemArticleDataBean
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.repository.SystemRepository
import com.yl.wanandroid.ui.adapter.SystemChildLeftListAdapter
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel.launchUI

/**
 * @description: 体系ViewModel
 * @author YL Chen
 * @date 2024/12/18 22:03
 * @version 1.0
 */
class SystemChildFragmentViewModel : BaseViewModel() {
    val systemRepository: SystemRepository? = getRepository<SystemRepository>()

    val systemData = MutableLiveData<MutableList<SystemDataBeanItem>?>()

    val systemArticleData = MutableLiveData<SystemArticleDataBean?>()


    private val DEFAULT_PAGE: Int = 0//默认加载页码

    //当前加载页码
    private var currentPage = DEFAULT_PAGE//初始为0

    /**
     * 获取体系数据
     */
    fun getSystemData() {
        launchUI(
            errorCallback = { _, errorMSg ->
                TipsToast.showTips(errorMSg)
                systemData.value = null
            },
            requestCall = {
                systemData.value = systemRepository?.getSystemData()
            }
        )
    }

    /**
     *获取对应cid的system下的文章
     * @param cid Int
     */
    fun getSystemArticleDataByCid(cid: Int) {
        launchUI(
            errorCallback = { _, errorMSg ->
                TipsToast.showTips(errorMSg)
                systemArticleData.value = null
            },
            requestCall = {
                systemArticleData.value =
                    systemRepository?.getSystemArticleDataByCid(DEFAULT_PAGE, cid)
            }
        )
    }

    override fun onReload() {
        getSystemData()
    }


}