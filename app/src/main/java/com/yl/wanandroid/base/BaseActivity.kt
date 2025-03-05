package com.yl.wanandroid.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.ui.custom.MultiplyStateView

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

    open lateinit var mRefreshLayout: SmartRefreshLayout
    //仅供直接继承本类的子类调用,继承于BaseVMActivity的调用此对象的方法无效,
    // 因为setSuccessView方法先于getLoadView方法执行,导致加载状态无法被移除,继承于BaseVMActivity的想改变状态需使用mViewModel调用changeStateView方法
    open lateinit var mMultiplyStateView: MultiplyStateView
    //子view的布局id
    private var mLayoutId: Int = layoutID

    //子View的dataBinding
    lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置基础布局
        setContentView(R.layout.base_load_more_layout)
        //初始化界面
        initView()
        //初始化数据
        initData()
    }

    //初始化界面
    open fun initView() {
        //获取加载成功DataViewBinding
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            mLayoutId,
            null,
            false
        )
        //刷新框架
        mRefreshLayout = findViewById(R.id.refreshLayout)
        //找到基础布局中的自定义多状态View控件
        mMultiplyStateView = findViewById(R.id.multiply_state_view)
        // 将加载成功View布局添加到自定义多状态View中
        mMultiplyStateView.setSuccessView(mBinding.root)
    }

    //初始化数据
    abstract fun initData()
}