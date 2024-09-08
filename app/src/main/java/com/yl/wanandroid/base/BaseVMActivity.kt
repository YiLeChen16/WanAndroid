package com.yl.wanandroid.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @description: 携带ViewModel的Activity基类，继承自BaseActivity
 * @author YL Chen
 * @date 2024/9/6 14:16
 * @version 1.0
 */
abstract class BaseVMActivity<VB:ViewDataBinding,VM:BaseViewModel>(@LayoutRes layoutId:Int):BaseActivity<VB>(layoutId) {

    //子类ViewModel实例
    lateinit var mViewModel:VM

    /**
     * 获取对应的ViewModel，并初始化数据
     */
    override fun initData() {
        mViewModel = getViewModel()!!
        //将子类的ViewModel和dataBinding联系起来，实现界面数据的自动更新
        val variableId = getVariableId()
        if(variableId !=-1){
            mBinding.setVariable(variableId,mViewModel)
            //立即执行 Data Binding 中的挂起绑定
            //即Data Binding 会立即将 ViewModel 的属性和方法更新到布局文件中
            mBinding.executePendingBindings()
        }
        //初始化ViewModel数据
        initVMData()
        //监听liveData
        observeLiveData()
        //初始化视图状态
        initViewState()
        //监听View类生命周期
        lifecycle.addObserver(mViewModel)
    }

    /**
     * 监听ViewModel中的LiveData
     */
    open fun observeLiveData() {
    }

    /**
     * 初始化状态
     */
    private fun initViewState() {
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
     * 网络错误
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
     * 初始化ViewModel数据
     */
    abstract fun initVMData()

    /**
     * 获取xml绑定的variable
     * @return Int
     */
    open fun getVariableId(): Int {
        return -1
    }

    /**
     * 通过反射获取子类的ViewModel
     * @return VM?
     */
    private fun getViewModel(): VM? {
        //这里获得到的是类的泛型的类型
        val type = javaClass.genericSuperclass
        if (type != null && type is ParameterizedType) {
            val actualTypeArguments = type.actualTypeArguments
            val tClass = actualTypeArguments[1]
            return ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            )
                .get(tClass as Class<VM>)
        }
        return null
    }

}