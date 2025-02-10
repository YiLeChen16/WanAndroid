package com.yl.wanandroid.viewmodel.home

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.Constant
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.ArticleItemData
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.repository.WendaRepository
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: HotOrMoreWendaFragment的ViewModel
 * @author YL Chen
 * @date 2024/10/20 16:25
 * @version 1.0
 */
class HotOrMoreWendaFragmentViewModel : BaseViewModel() {
    private val DEFAULT_PAGE: Int = 0//默认加载页码
    private val wendaRepository = getRepository<WendaRepository>()

    //热门问答数据
    val hotWendaData = MutableLiveData<MutableList<ArticleItemData>?>()

    //普通问答数据
    val normalWendaData = MutableLiveData<ArticleDataBean?>()

    //当前加载页码
    private var currentPage = DEFAULT_PAGE//初始为0

    //更多普通问答数据
    val moreNormalWendaData = MutableLiveData<ArticleDataBean?>()

    //fragment的类型
    var type: String = ""

    fun getHotWendaData() {
        launchUI(
            errorCallback = { errorCode, errorMsg ->
                TipsToast.showTips(errorMsg)
                hotWendaData.value = null
                changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            },
            requestCall = { hotWendaData.value = wendaRepository?.getHotWendaData() }
        )
    }

    fun getNormalWendaData() {
        launchUI(
            errorCallback = { errorCode, errorMsg ->
                TipsToast.showTips(errorMsg)
                normalWendaData.value = null
                changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            },
            requestCall = {
                //重置当前页码数
                currentPage = DEFAULT_PAGE
                normalWendaData.value = wendaRepository?.getNormalWendaData(DEFAULT_PAGE)
            }
        )
    }

    fun getMoreNormalWendaData() {
        //页码+1
        currentPage++
        launchUI(
            errorCallback = { errorCode, errorMsg ->
                TipsToast.showTips(errorMsg)
                moreNormalWendaData.value = null
                currentPage--//页码数-1
            },
            requestCall = {
                moreNormalWendaData.value = wendaRepository?.getNormalWendaData(currentPage)
            }
        )
    }


    override fun onReload() {
        if (type == Constant.HOT_WENDA_FRAGMENT_TYPE) {
            getHotWendaData()
        } else {
            getNormalWendaData()
        }
    }
}