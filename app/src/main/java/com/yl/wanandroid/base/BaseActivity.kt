package com.yl.wanandroid.base

import android.os.Bundle
<<<<<<< HEAD
=======
import android.widget.FrameLayout
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yl.wanandroid.R
<<<<<<< HEAD
import com.yl.wanandroid.ui.custom.MultiplyStateView
=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451

/**
 * @description:
 * 普通Activity基类，不带ViewModel,显示基本加载状态
 * 需要获取到子类的布局id用于databinding的绑定
 * @author YL Chen
 * @date 2024/9/4 21:34
 * @version 1.0
 */
abstract class BaseActivity<VB : ViewDataBinding>(@LayoutRes layoutID: Int) :
    AppCompatActivity() { //此处不能将layoutId传递进去，否则会导致fragment加载但不显示

<<<<<<< HEAD
    open lateinit var mMultiplyStateView: MultiplyStateView

=======
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    //子view的布局id
    private var mLayoutId: Int = layoutID

    //子View的dataBinding
    lateinit var mBinding: VB

<<<<<<< HEAD
=======

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置基础布局
        setContentView(R.layout.base_layout)
<<<<<<< HEAD
        //获取加载成功DataViewBinding
=======
        //找到基础布局中的控件
        val baseFrameLayout = findViewById<FrameLayout>(R.id.base_frame_layout)
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            mLayoutId,
            null,
            false
        )
<<<<<<< HEAD
        //找到基础布局中的自定义多状态View控件
        mMultiplyStateView = findViewById(R.id.multiply_state_view)
        // 将加载成功View布局添加到自定义多状态View中
        mMultiplyStateView.setSuccessView(mBinding.root)
=======
        // 将子布局添加到基础布局中
        baseFrameLayout.addView(mBinding.root)

>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
        //初始化界面
        initView()
        //初始化数据
        initData()
    }

<<<<<<< HEAD
    //初始化界面
    open fun initView() {
        mMultiplyStateView.showSuccess()
=======

    //初始化界面
    open fun initView() {
>>>>>>> 891810884e0260482ab4f05672b0615f60aba451
    }

    //初始化数据
    abstract fun initData()
}