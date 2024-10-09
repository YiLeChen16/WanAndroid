package com.yl.wanandroid.base

<<<<<<< HEAD
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.yl.wanandroid.R
import com.yl.wanandroid.ui.custom.MultiplyStateView
import com.yl.wanandroid.ui.custom.MyLoadingView
import com.yl.wanandroid.utils.LogUtils
=======
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
import java.lang.reflect.ParameterizedType

/**
 * @description: 携带ViewModel的Activity基类，继承自BaseActivity
 * @author YL Chen
 * @date 2024/9/6 14:16
 * @version 1.0
 */
<<<<<<< HEAD
abstract class BaseVMActivity<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes layoutId: Int) :
    BaseActivity<VB>(layoutId) {
    //子类ViewModel实例
    lateinit var mViewModel: VM
=======
abstract class BaseVMActivity<VB:ViewDataBinding,VM:BaseViewModel>(@LayoutRes layoutId:Int):BaseActivity<VB>(layoutId) {

    //子类ViewModel实例
    lateinit var mViewModel:VM
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

    /**
     * 获取对应的ViewModel，并初始化数据
     */
    override fun initData() {
<<<<<<< HEAD
        dataLoading()
        mViewModel = getViewModel()!!
        //将子类的ViewModel和dataBinding联系起来，实现界面数据的自动更新
        val variableId = getVariableId()
        if (variableId != -1) {
            mBinding.setVariable(variableId, mViewModel)
=======
        mViewModel = getViewModel()!!
        //将子类的ViewModel和dataBinding联系起来，实现界面数据的自动更新
        val variableId = getVariableId()
        if(variableId !=-1){
            mBinding.setVariable(variableId,mViewModel)
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
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
<<<<<<< HEAD
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
=======
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
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
            }
        }
    }

    /**
<<<<<<< HEAD
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


=======
     * 网络错误
     */
    open fun netError() {
    }

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    /**
     * 数据加载为空
     */
    open fun dataEmpty() {
<<<<<<< HEAD
        mMultiplyStateView.showEmpty()
=======
    }

    /**
     * 数据加载错误
     */
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