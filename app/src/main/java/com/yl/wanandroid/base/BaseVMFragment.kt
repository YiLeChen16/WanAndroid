package com.yl.wanandroid.base

<<<<<<< HEAD
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yl.wanandroid.R
import com.yl.wanandroid.ui.custom.MyLoadingView
import com.yl.wanandroid.utils.LogUtils
=======
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
import java.lang.reflect.ParameterizedType

/**
 * @description: 携带ViewModel的fragment
 * @author YL Chen
 * @date 2024/9/6 21:52
 * @version 1.0
 */
<<<<<<< HEAD
abstract class BaseVMFragment<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes layoutId: Int) :
    BaseFragment<VB>(layoutId) {


=======
abstract class BaseVMFragment<VB:ViewDataBinding,VM:BaseViewModel>(@LayoutRes layoutId:Int):BaseFragment<VB>(layoutId) {
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    lateinit var mViewModel: VM

    override fun initData() {
        mViewModel = getViewModel()!!
        val variableId = getVariableId()
        if (variableId != -1) {
            mBinding.setVariable(getVariableId(), mViewModel)
            mBinding.executePendingBindings()
        }
<<<<<<< HEAD
        initState()
        initVMData()
        observeLiveData()
=======
        initVMData()
        observeLiveData()
        initState()
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
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
<<<<<<< HEAD
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
        LogUtils.d(this,"onViewCreated-->dataLoading")
        //页面加载完成后展示加载状态视图
        dataLoading()
    }

    /**
     * 数据加载成功
     */
    open fun loadSuccess() {
       mMultiplyStateView.showSuccess()
=======
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

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    }

    /**
     * 网络加载失败
     */
    open fun netError() {
<<<<<<< HEAD
        mMultiplyStateView.showNetError()
    }

=======

    }

    /**
     * 数据加载为空
     */
    open fun dataEmpty() {

    }
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

    /**
     * 数据加载错误
     */
<<<<<<< HEAD
    open fun dataEmpty() {
        mMultiplyStateView.showEmpty()
=======
    open fun dataError() {

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    }

    /**
     * 数据加载中
     */
    open fun dataLoading() {
<<<<<<< HEAD
        mMultiplyStateView.showLoading()
        val loadingView = mMultiplyStateView.getLoadingView()
        val myLoadingView = loadingView.findViewById<MyLoadingView>(R.id.my_loading_view)
        myLoadingView.startRotate()
=======

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
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