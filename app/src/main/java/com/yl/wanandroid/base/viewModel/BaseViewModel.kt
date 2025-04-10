package com.yl.wanandroid.base.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.network.exeception.ERROR
import com.yl.wanandroid.network.exeception.ExceptionHandler
import com.yl.wanandroid.ui.custom.MultiplyStateView
import com.yl.wanandroid.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @description: ViewModel基类
 * @author YL Chen
 * @date 2024/9/6 14:17
 * @version 1.0
 */
abstract class BaseViewModel : ViewModel(),MultiplyStateView.OnReLodListener {

    /**
     * 控制状态视图的LiveData
     */
    val mStateViewLiveData = MutableLiveData(ViewStateEnum.VIEW_LOADING)

    /**
     * 切换到UI线程
     * @param errorCallback SuspendFunction0<Unit> 错误回调
     * @param requestCall SuspendFunction0<Unit> 网络请求函数
     */
    fun launchUI(
        errorCallback: suspend (Int?, String?) -> Unit,
        requestCall: suspend () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            //统一进行异常捕获
            safeApiCall(errorCallback, requestCall)
        }
    }

    /**
     * 对网络请求进行统一异常捕获
     * @param errorCallback SuspendFunction2<Int?, String?, Unit> 错误回调
     * @param requestCall SuspendFunction0<Unit> 网络请求函数
     * @return T? 网络请求成功数据
     */
    private suspend fun<T> safeApiCall(
        errorCallback: suspend (Int?, String?) -> Unit,
        requestCall: suspend () -> T?
    ): T? {
        try {
            //返回网络请求结果
            return requestCall()
        }catch (e: Exception){
            LogUtils.e(this@BaseViewModel,e.message.toString())
            e.printStackTrace()
            //统一异常处理
            //将异常转为ApiException
            val apiException = ExceptionHandler.handleException(e)
            if (apiException.errCode == ERROR.UNKNOW_HOST.code || apiException.errCode == ERROR.NETWORD_ERROR.code){
                changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
            errorCallback(apiException.errCode,apiException.errMsg)
        }
        return null
    }

    /**
     * 更改状态视图的状态
     */
    fun changeStateView(
        state: ViewStateEnum
    ) {
        // 对参数进行校验
        when (state) {
            ViewStateEnum.VIEW_LOADING -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_LOADING)
            }

            ViewStateEnum.VIEW_EMPTY -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_EMPTY)
            }

            ViewStateEnum.VIEW_NET_ERROR -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_NET_ERROR)
            }

            ViewStateEnum.VIEW_LOAD_SUCCESS -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
            ViewStateEnum.VIEW_NONE -> {
                mStateViewLiveData.postValue(ViewStateEnum.VIEW_NONE)
            }
        }
    }

    //错误视图点击回调函数
    override fun onReLoad() {
        //调用方法由子类实现
        onReload()
    }

    //子类可实现此方法实现界面重新加载
    open fun onReload(){}
}