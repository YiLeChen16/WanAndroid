package com.yl.wanandroid.base.activity


import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yl.wanandroid.R
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.custom.MyLoadingView
import com.yl.wanandroid.utils.LogUtils
import java.lang.reflect.ParameterizedType

/**
 * @description: 携带ViewModel的Activity基类，继承自BaseActivity
 * @author YL Chen
 * @date 2024/9/6 14:16
 * @version 1.0
 */
abstract class BaseVMActivity<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes layoutId: Int) :
    BaseActivity<VB>(layoutId) {
    //子类ViewModel实例
    lateinit var mViewModel: VM

    /**
     * 获取对应的ViewModel，并初始化数据
     */
    override fun initData() {
        //dataLoading()
        mViewModel = getViewModel()!!
        //将子类的ViewModel和dataBinding联系起来，实现界面数据的自动更新
        //将xml布局对应的viewModel对象赋值到xml布局中声明的viewModel变量 即实现如:mBinding.viewModel = ViewModel() 的效果
        val variableId = getVariableId()
        if (variableId != -1) {
            mBinding.setVariable(variableId, mViewModel)
            //立即执行 Data Binding 中的挂起绑定
            //即Data Binding 会立即将 ViewModel 的属性和方法更新到布局文件中
            mBinding.executePendingBindings()
        }
        //初始化视图状态
        initViewState()
        //初始化ViewModel数据
        initVMData()
        //监听liveData
        observeLiveData()
        //设置状态页点击重新加载监听
        mMultiplyStateView.setOnReLodListener(mViewModel)
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
     * 数据加载为空
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
     * 初始化ViewModel数据
     */
    abstract fun initVMData()

    /**
     * 获取xml绑定的variable
     * @return Int
     */
    //子类通过重写此方法返回子类对应xml文件中绑定的viewModel变量的id
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
            )[tClass as Class<VM>]
        }
        return null
    }

}