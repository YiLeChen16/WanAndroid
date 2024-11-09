package com.yl.wanandroid.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.yl.wanandroid.R
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.utils.LogUtils


/**
 * @description: 自定义多状态View
 * @author YL Chen
 * @date 2024/9/29 10:47
 * @version 1.0
 */

open class MultiplyStateView : FrameLayout {

    private var mOnReLodListener: OnReLodListener? = null
    private lateinit var params: LayoutParams
    private lateinit var mInflater: LayoutInflater
    private var mSuccessViewId: Int = 0
    private var mEmptyViewId: Int = 0
    private var mNetErrorViewId: Int = 0
    private var mLoadingViewId: Int = 0

    //四种展示的view
    private var mLoadingView: View? = null
    private var mSuccessView: View? = null
    private var mNetErrorView: View? = null
    private var mEmptyView: View? = null

    //当前视图状态
    var currentState: ViewStateEnum = ViewStateEnum.VIEW_NONE

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defy: Int) : super(context, attrs, defy) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        //获取自定义属性
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.MultiplyStateView)
        mLoadingViewId =
            a.getResourceId(R.styleable.MultiplyStateView_msv_loadingView, R.layout.view_loading)
        mNetErrorViewId =
            a.getResourceId(R.styleable.MultiplyStateView_msv_netErrorView, R.layout.view_net_error)
        mEmptyViewId =
            a.getResourceId(R.styleable.MultiplyStateView_msv_emptyView, R.layout.view_empty)
        mSuccessViewId = a.getResourceId(R.styleable.MultiplyStateView_msv_successView, 0)

        a.recycle()
        mInflater = LayoutInflater.from(context)
        params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setLoadingView(mLoadingViewId)
        setEmptyView(mEmptyViewId)
        setNetErrorView(mNetErrorViewId)
    }

    //在 XML 布局文件中的视图被加载并且所有的子视图都被添加到父视图中之后执行
/*    override fun onFinishInflate() {
        super.onFinishInflate()
        //展示加载页面
        LogUtils.d(this,"onFinishInflate-->showLoading")
        showLoading()
        val loadingView = getLoadingView()
        val myLoadingView = loadingView.findViewById<MyLoadingView>(R.id.my_loading_view)
        myLoadingView.startRotate()
    }*/

    //++++++++++++++++++++++++++++++++加载页面++++++++++++++++++++

    //动态加载并展示加载页面
    fun showLoading() {
        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(mLoadingViewId, null);
        }
        if (mLoadingView != null ) {
            removeAllViews()
            addView(mLoadingView, 0, params)
        } else {
            throw NullPointerException("you have to set loading view before that");
        }
    }

    /**
     * 提供方法给外界设置自定义加载页面
     * @param layoutId Int 布局Id
     */
    open fun setLoadingView(@LayoutRes layoutId: Int) {
        setLoadingView(mInflater.inflate(layoutId, null))
    }

    open fun setLoadingView(view: View) {
        mLoadingView = view
    }

    /**
     * 获取加载页面
     */
    fun getLoadingView(): View {
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewId, null)
        }
        return mLoadingView!!
    }

    //++++++++++++++++++++++++++++++++成功页面++++++++++++++++++++
    /**
     * 显示成功状态
     */
    fun showSuccess() {
        if (null == mSuccessView) {
            mSuccessView = mInflater.inflate(mSuccessViewId, null)
        }
        if (mSuccessView != null) {
            removeAllViews()
            addView(mSuccessView, 0, params)
            currentState = ViewStateEnum.VIEW_LOAD_SUCCESS
        } else {
            throw NullPointerException("you have to set success view before that")
        }
    }

    /**
     * 设置自定义的成功页面
     *
     * @param layoutResID
     */
    fun setSuccessView(@LayoutRes layoutResID: Int) {
        setSuccessView(mInflater.inflate(layoutResID, null))
    }

    /**
     * 设置自定义的成功页面
     *
     * @param view
     */
    fun setSuccessView(view: View) {
        mSuccessView = view
    }


    /**
     * 获取成功页面
     */
    fun getSuccessView(): View {
        if (null == mSuccessView) {
            mSuccessView = mInflater.inflate(mSuccessViewId, null)
        }
        return mSuccessView!!
    }


    //++++++++++++++++++++++++++++++++网络错误页面++++++++++++++++++++
    /**
     * 显示加载失败(网络错误)状态 带监听器的
     */
    fun showNetError() {
        if (null == mNetErrorView) {
            mNetErrorView = mInflater.inflate(mNetErrorViewId, null)
        }

        if (mNetErrorView != null) {
            removeAllViews()
            addView(mNetErrorView, 0, params)
            currentState = ViewStateEnum.VIEW_NET_ERROR
            mNetErrorView!!.setOnClickListener { showReLoading() }
        } else {
            throw java.lang.NullPointerException("you have to set unknown view before that")
        }
    }


    /**
     * 设置自定义的网络异常
     *
     * @param layoutResID
     */
    fun setNetErrorView(@LayoutRes layoutResID: Int) {
        setNetErrorView(mInflater.inflate(layoutResID, null))
    }

    /**
     * 设置自定义的网络异常
     *
     * @param view
     */
    fun setNetErrorView(view: View) {
        mNetErrorView = view
    }


    /**
     * 设置获取网络错误页面
     */
    fun getNetErrorView(): View {
        if (null == mNetErrorView) {
            mNetErrorView = mInflater.inflate(mNetErrorViewId, null)
        }
        return mNetErrorView!!
    }


    //++++++++++++++++++++++++++++++++空页面页面++++++++++++++++++++
    /**
     * 显示无数据状态
     */
    fun showEmpty() {
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewId, null)
        }

        if (mEmptyView != null) {
            removeAllViews()
            addView(mEmptyView, 0, params)
            currentState = ViewStateEnum.VIEW_EMPTY
        } else {
            throw java.lang.NullPointerException("you have to set empty view before that")
        }
    }


    /**
     * 设置自定义的空页面
     *
     * @param layoutResID
     */
    fun setEmptyView(@LayoutRes layoutResID: Int) {
        setEmptyView(mInflater.inflate(layoutResID, null))
    }

    /**
     * 设置自定义的空页面
     *
     * @param view
     */
    fun setEmptyView(view: View) {
        mEmptyView = view
    }


    /**
     * 设置获取空页面
     */
    fun getEmptyView(): View {
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewId, null)
        }
        return mEmptyView!!
    }


    /**
     * 再次加载数据
     */
    private fun showReLoading() {
        //第一步重新loading
        if (mOnReLodListener != null) {
            showLoading()
            mOnReLodListener!!.onReLoad()
        } else {
            //未设置重新加载回调
            LogUtils.e(this, "请设置重新加载监听")
        }
    }

    /**
     * 外部回调
     *
     * @param onReLodListener
     */
    fun setOnReLodListener(onReLodListener: OnReLodListener) {
        this.mOnReLodListener = onReLodListener
    }

    /**
     * 重新加载页面的回调接口
     */
    interface OnReLodListener {
        fun onReLoad()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateVisibility()
    }

    private fun updateVisibility() {
        // 获取父布局的可见性
        val parentVisibility = (parent as? View)?.visibility ?: View.VISIBLE
        visibility = parentVisibility
    }


    // 如果需要监听父布局的可见性变化，可以重写这个方法
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateVisibility()
    }

}