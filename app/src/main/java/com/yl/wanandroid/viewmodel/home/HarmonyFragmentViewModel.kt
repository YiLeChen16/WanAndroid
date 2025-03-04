package com.yl.wanandroid.viewmodel.home

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.HarmonyColumnDataBean
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.repository.HarmonyRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: HarmonyFragment对应的ViewModel
 * @author YL Chen
 * @date 2024/10/20 16:10
 * @version 1.0
 */
class HarmonyFragmentViewModel:BaseViewModel() {
    //获取仓库对象
/*    val harmonyRepository = getRepository<HarmonyRepository>()*/

    //鸿蒙专栏全部数据
    val harmonyColumnDataBean = MutableLiveData<HarmonyColumnDataBean?>()

    //常用链接数据
    val linkData = MutableLiveData<MutableList<ArticleItemData>>()

    //开源项目数据
    val projectData = MutableLiveData<MutableList<ArticleItemData>>()

    //常用工具数据
    val toolData = MutableLiveData<MutableList<ArticleItemData>>()

    //默认展示数据
    //0:常用链接
    //1:开源项目
    //2:常用工具
    var show = MutableLiveData(0)

    //请求鸿蒙专栏全部数据
    fun getHarmonyColumnData(){
        launchUI(
            errorCallback = {
                _,errorMsg->
                TipsToast.showTips(errorMsg)
                harmonyColumnDataBean.value = null
            },
            requestCall = {
                harmonyColumnDataBean.value = HarmonyRepository.getHarmonyColumnData()
            }
        )
    }

    /**
     * 三个分栏点击事件
     */

    //常用链接分栏点击事件
    fun onTvLinkClick(){
        linkData.value = harmonyColumnDataBean.value?.links?.articleList?.toMutableList()
        show.value = 0
    }

    //开源项目分栏点击事件
    fun onTvProjectClick(){
        projectData.value = harmonyColumnDataBean.value?.open_sources?.articleList?.toMutableList()
        show.value = 1
    }

    //常用工具分栏点击事件
    fun onTvToolClick(){
        toolData.value = harmonyColumnDataBean.value?.tools?.articleList?.toMutableList()
        show.value = 2
    }

    override fun onReload() {
        getHarmonyColumnData()
    }
}