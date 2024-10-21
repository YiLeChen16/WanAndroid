package com.yl.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.repository.SearchActivityRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 搜索界面ViewModel
 * @author YL Chen
 * @date 2024/10/21 17:47
 * @version 1.0
 */
class SearchActivityViewModel : BaseViewModel() {

    val searchActivityRepository: SearchActivityRepository? = getRepository()

    //搜索热词数据
    var searchHotKeyData = MutableLiveData<MutableList<SearchHotKeyDataBean>?>()

    fun getSearchHotkeyData(): LiveData<MutableList<SearchHotKeyDataBean>?> {
        if (searchHotKeyData.value == null) {
            launchUI(
                errorCallback = { errorCode, errorMsg ->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@SearchActivityViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    searchHotKeyData.value = null
                },
                requestCall = {
                    searchHotKeyData.value = searchActivityRepository?.getSearchHotKeyData()
                }
            )
        }
        return searchHotKeyData
    }

    override fun onReload() {
        getSearchHotkeyData()
    }
}