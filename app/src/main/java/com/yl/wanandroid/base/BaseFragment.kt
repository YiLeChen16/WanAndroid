package com.yl.wanandroid.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yl.wanandroid.R
import com.yl.wanandroid.utils.LogUtils

/**
 * @description: 普通Fragment基类，不带ViewModel
 * @author YL Chen
 * @date 2024/9/6 16:19
 * @version 1.0
 */
abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes layoutId: Int = 0) : Fragment() {

    //子类的布局id
    val mLayoutId: Int = layoutId

    //子View的dataBinding
    lateinit var mBinding: VB

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置根布局
        rootView = layoutInflater.inflate(R.layout.base_layout, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //获取子类ViewDataBinding
        //mBinding = DataBindingUtil.bind(view)!!
        mBinding = DataBindingUtil.inflate(layoutInflater, mLayoutId, null, false)
        //找到根布局的baseFrameLayout
        val baseFrameLayout = rootView.findViewById<FrameLayout>(R.id.base_frame_layout)
        //将子类布局添加进去
        baseFrameLayout.addView(mBinding.root)
        //初始化视图
        initView()
        //初始化数据
        initData()

        LogUtils.d(this, this.toString())
    }

    //初始化视图
    open fun initView() {
    }

    //初始化数据
    abstract fun initData()
}