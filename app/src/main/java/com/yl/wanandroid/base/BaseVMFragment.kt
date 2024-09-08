package com.yl.wanandroid.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @description: 携带ViewModel的fragment
 * @author YL Chen
 * @date 2024/9/6 21:52
 * @version 1.0
 */
abstract class BaseVMFragment<VB:ViewDataBinding,VM:BaseViewModel>(@LayoutRes layoutId:Int):BaseFragment<VB>(layoutId) {
    lateinit var mViewModel: VM

    override fun initData() {
        mViewModel = getViewModel()!!
        val variableId = getVariableId()
        if (variableId != -1) {
            mBinding.setVariable(getVariableId(), mViewModel)
            mBinding.executePendingBindings()
        }
        initVMData()
        observeLiveData()
        initState()
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
    private fun initState(){
        mViewModel.mStateViewLiveData.observe(this) {
            when (it) {
                StateLayoutEnum.DATA_LOADING -> {
                    dataLoading()
                }

                StateLayoutEnum.DATA_ERROR -> {
                    dataError()
                }

                StateLayoutEnum.DATA_NULL -> {
                    dataEmpty()
                }

                StateLayoutEnum.NET_ERROR -> {
                    netError()
                }
            }
        }

    }

    /**
     * 网络加载失败
     */
    open fun netError() {

    }

    /**
     * 数据加载为空
     */
    open fun dataEmpty() {

    }

    /**
     * 数据加载错误
     */
    open fun dataError() {

    }

    /**
     * 数据加载中
     */
    open fun dataLoading() {

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