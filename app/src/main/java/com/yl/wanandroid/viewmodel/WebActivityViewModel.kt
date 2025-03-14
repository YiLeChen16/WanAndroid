package com.yl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel

/**
 * @description: WebActivity的ViewModel
 * @author YL Chen
 * @date 2024/10/12 18:07
 * @version 1.0
 */
class WebActivityViewModel : BaseViewModel() {
    //网页加载url
    var url = MutableLiveData<String>()
    var isNetError = MutableLiveData(false)

    //用户点击错误视图时会回调此方法
    override fun onReload() {
        isNetError.value = false//假设网络已恢复,通知ui进行重新加载
        url.value = url.value
    }

}