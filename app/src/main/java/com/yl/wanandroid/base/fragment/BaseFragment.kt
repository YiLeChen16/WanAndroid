package com.yl.wanandroid.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.ui.custom.MultiplyStateView

/**
 * @description: 普通Fragment基类，不带ViewModel
 * @author YL Chen
 * @date 2024/9/6 16:19
 * @version 1.0
 */
abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes layoutId: Int = 0) : Fragment() {

    open lateinit var mRefreshLayout: SmartRefreshLayout
    open lateinit var mMultiplyStateView: MultiplyStateView

    //子类的布局id
    private val mLayoutId: Int = layoutId

    //子View的dataBinding
    lateinit var mBinding: VB

    lateinit var mRootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置根布局
        mRootView = layoutInflater.inflate(R.layout.base_load_more_layout, container, false)
        return mRootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化视图
        initView()
        //初始化数据
        initData()
    }

    //初始化视图
    open fun initView() {
        //获取加载成功View的DataViewBinding
        mBinding = DataBindingUtil.inflate(layoutInflater, mLayoutId, null, false)
        //找到根布局的baseFrameLayout
        mMultiplyStateView = mRootView.findViewById(R.id.multiply_state_view)
        //将子类加载成功View布局添加进去
        mMultiplyStateView.setSuccessView(mBinding.root)
        //获取到刷新框架
        mRefreshLayout = mRootView.findViewById(R.id.refreshLayout)
    }

    //初始化数据
    abstract fun initData()
}