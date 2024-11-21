package com.yl.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel

/**
 * @description: WebActivity的ViewModel
 * @author YL Chen
 * @date 2024/10/12 18:07
 * @version 1.0
 */
class WebActivityViewModel:BaseViewModel() {
    //网页加载url
    var url = MutableLiveData<String>()

    override fun onReload() {
        url.value = url.value
    }

}