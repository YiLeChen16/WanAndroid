package com.yl.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yl.wanandroid.R
import com.yl.wanandroid.databinding.BaseLayoutBinding
import com.yl.wanandroid.ui.custom.MultiplyStateView
import com.yl.wanandroid.utils.LogUtils

/**
 * @description: 普通Fragment基类，不带ViewModel
 * @author YL Chen
 * @date 2024/9/6 16:19
 * @version 1.0
 */
abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes layoutId: Int = 0) : Fragment() {

    open lateinit var mMultiplyStateView: MultiplyStateView

    //子类的布局id
    val mLayoutId: Int = layoutId

    //子View的dataBinding
    lateinit var mBinding: VB

    lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置根布局
        mRootView = layoutInflater.inflate(R.layout.base_layout, container, false)
        LogUtils.d(this, "onCreateView--")
        return mRootView
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d(this, "onStart--")

    }

    override fun onResume() {
        super.onResume()
        LogUtils.d(this, "onResume--")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        LogUtils.d(this, "onViewCreated--")
        super.onViewCreated(view, savedInstanceState)
        //获取加载成功View的DataViewBinding
        mBinding = DataBindingUtil.inflate(layoutInflater, mLayoutId, null, false)
        //找到根布局的baseFrameLayout
        mMultiplyStateView = mRootView.findViewById(R.id.multiply_state_view)
        //将子类加载成功View布局添加进去
        mMultiplyStateView.setSuccessView(mBinding.root)
        //初始化视图
        initView()
        //初始化数据
        initData()
        LogUtils.d(this, this.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(this, "onCreate--")
    }


    //初始化视图
    open fun initView() {
    }

    //初始化数据
    abstract fun initData()


    override fun onDestroy() {
        LogUtils.d(this, "onDestroy--")
        super.onDestroy()
    }

    override fun onPause() {
        LogUtils.d(this, "onPause--")
        super.onPause()
    }

    override fun onStop() {
        LogUtils.d(this, "onStop--")
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtils.d(this, "onDestroyView--")

    }
}