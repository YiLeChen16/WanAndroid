package com.yl.wanandroid.base

import android.os.Bundle
<<<<<<< HEAD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
=======
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yl.wanandroid.R
<<<<<<< HEAD
import com.yl.wanandroid.databinding.BaseLayoutBinding
import com.yl.wanandroid.ui.custom.MultiplyStateView
=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
import com.yl.wanandroid.utils.LogUtils

/**
 * @description: 普通Fragment基类，不带ViewModel
 * @author YL Chen
 * @date 2024/9/6 16:19
 * @version 1.0
 */
abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes layoutId: Int = 0) : Fragment() {

<<<<<<< HEAD
    open lateinit var mMultiplyStateView: MultiplyStateView

=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    //子类的布局id
    val mLayoutId: Int = layoutId

    //子View的dataBinding
    lateinit var mBinding: VB

<<<<<<< HEAD
    lateinit var mRootView: View
=======
    private lateinit var rootView: View
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //设置根布局
<<<<<<< HEAD
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
=======
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
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
        //初始化视图
        initView()
        //初始化数据
        initData()

        LogUtils.d(this, this.toString())
    }

<<<<<<< HEAD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(this, "onCreate--")
    }


=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    //初始化视图
    open fun initView() {
    }

    //初始化数据
    abstract fun initData()
<<<<<<< HEAD


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
=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
}