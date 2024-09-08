package com.yl.wanandroid.base

import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yl.wanandroid.R

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

    //子view的布局id
    private var mLayoutId: Int = layoutID

    //子View的dataBinding
    lateinit var mBinding: VB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置基础布局
        setContentView(R.layout.base_layout)
        //找到基础布局中的控件
        val baseFrameLayout = findViewById<FrameLayout>(R.id.base_frame_layout)
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            mLayoutId,
            null,
            false
        )
        // 将子布局添加到基础布局中
        baseFrameLayout.addView(mBinding.root)

        //初始化界面
        initView()
        //初始化数据
        initData()
    }


    //初始化界面
    open fun initView() {
    }

    //初始化数据
    abstract fun initData()
}