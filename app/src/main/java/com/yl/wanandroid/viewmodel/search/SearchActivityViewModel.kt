package com.yl.wanandroid.viewmodel.search

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.ObservableField
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

    var search_fragment_visibility = MutableLiveData(true)//searchFragment的可见性
    //var search_result_fragment_visibility = MutableLiveData("gone")//searchResultFragment的可见性

    val searchActivityRepository: SearchActivityRepository? = getRepository()

    //搜索热词数据
    var searchHotKeyData = MutableLiveData<MutableList<SearchHotKeyDataBean>?>()

    //返回按钮状态
    var isBack = MutableLiveData(false)

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

    //返回按钮点击事件
    fun onBackClick(){
        LogUtils.d(this,"onBackClick-->${isBack.value}")
        this.isBack.value = true
    }

    //搜索按钮点击事件
    fun onSearchClick(){
        LogUtils.d(this,"onSearchClick--")

    }

    override fun onReload() {
        getSearchHotkeyData()
    }
}