package com.yl.wanandroid.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yl.wanandroid.R
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.custom.MyLoadingView
import com.yl.wanandroid.utils.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * @description: 携带ViewModel的fragment
 * @author YL Chen
 * @date 2024/9/6 21:52
 * @version 1.0
 */
abstract class BaseVMFragment<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes layoutId: Int) :
    BaseFragment<VB>(layoutId) {


    lateinit var mViewModel: VM

    override fun initData() {
        mViewModel = getViewModel()!!
        val variableId = getVariableId()
        if (variableId != -1) {
            mBinding.setVariable(getVariableId(), mViewModel)
            mBinding.executePendingBindings()
        }
        initState()
        initVMData()
        observeLiveData()
        //设置状态页点击重新加载监听
        mMultiplyStateView.setOnReLodListener(mViewModel)
    }

    /**
     * 获取子类xml 的Variable
     * @return Int
     */
    open fun getVariableId(): Int {
        return -1
    }

    /**
     * 初始化状态
     */
    private fun initState() {
        mViewModel.mStateViewLiveData.observe(this) {
            when (it) {
                ViewStateEnum.VIEW_LOADING -> {
                    LogUtils.d(this, "StateLayoutEnum.DATA_LOADING")
                    dataLoading()
                }

                ViewStateEnum.VIEW_EMPTY -> {
                    LogUtils.d(this, "StateLayoutEnum.DATA_ERROR")
                    dataEmpty()
                }


                ViewStateEnum.VIEW_NET_ERROR -> {
                    LogUtils.d(this, "StateLayoutEnum.NET_ERROR")
                    netError()
                }

                ViewStateEnum.VIEW_LOAD_SUCCESS -> {
                    LogUtils.d(this, "StateLayoutEnum.LOAD_SUCCESS")
                    loadSuccess()
                }

                ViewStateEnum.VIEW_NONE -> {
                    LogUtils.d(this, "StateLayoutEnum.NONE")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtils.d(this, "onViewCreated-->dataLoading")
    }

    /**
     * 数据加载成功
     */
    open fun loadSuccess() {
        mMultiplyStateView.showSuccess()
    }

    /**
     * 网络加载失败
     */
    open fun netError() {
        mMultiplyStateView.showNetError()
    }


    /**
     * 数据加载错误
     */
    open fun dataEmpty() {
        mMultiplyStateView.showEmpty()
    }

    /**
     * 数据加载中
     */
    open fun dataLoading() {
        mMultiplyStateView.showLoading()
        val loadingView = mMultiplyStateView.getLoadingView()
        val myLoadingView = loadingView.findViewById<MyLoadingView>(R.id.my_loading_view)
        myLoadingView.startRotate()
    }

    /**
     * 通过反射获取子类的ViewModel
     * @return VM?
     */
    private fun getViewModel(): VM? {//这里获得到的是类的泛型的类型
        val type = javaClass.genericSuperclass
        if (type != null && type is ParameterizedType) {
            val actualTypeArguments = type.actualTypeArguments
            val tClass = actualTypeArguments[1]
            return ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            )
                .get(tClass as Class<VM>)
        }
        return null
    }

    /**
     * 初始化ViewModel数据
     */
    abstract fun initVMData()

    /**
     * 监听ViewModel中的LiveData
     */
    open fun observeLiveData() {
    }

}