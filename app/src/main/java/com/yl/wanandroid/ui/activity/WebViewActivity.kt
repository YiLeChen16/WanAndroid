package com.yl.wanandroid.ui.activity

import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.ActivityWebBinding
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.WebActivityViewModel


/**
 * @description: 装载网页的activity
 * @author YL Chen
 * @date 2024年10月10日
 * @version 1.0
 */
class WebViewActivity :
    BaseVMActivity<ActivityWebBinding, WebActivityViewModel>(R.layout.activity_web) {
    private var mWebView: WebView? = null

    override fun initView() {
        super.initView()
        mWebView = mBinding.webView
        val webSettings = mWebView?.settings
        webSettings?.javaScriptEnabled = true
        webSettings?.loadsImagesAutomatically = true

        //设置自适应屏幕，两者合用
        webSettings?.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings?.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        webSettings?.setSupportZoom(true)//支持缩放，默认为true。是下面那个的前提。
        webSettings?.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings?.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        webSettings?.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings?.allowFileAccess = true //设置可以访问文件
        webSettings?.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings?.loadsImagesAutomatically = true //支持自动加载图片
        webSettings?.defaultTextEncodingName = "utf-8"//设置编码格式

    }

    override fun initData() {
        super.initData()
        val url = intent.extras?.getString(Constant.toWebUrlKey)
        //加载传递过来的网页
        //判断是否为空
        LogUtils.d(this, "initData-->url-->$url")
        if (url == null) {
            //加载空界面
            mViewModel.changeStateView(ViewStateEnum.VIEW_EMPTY)
        } else {
            //加载网页
            LogUtils.d(this, "mWebView-->$mWebView")
            mWebView?.loadUrl(url)
        }
        mWebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                //更改视图状态
                LogUtils.d(this@WebViewActivity, "onPageFinished---")
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            }
        }
    }

    override fun initVMData() {

    }

    //在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。避免WebView内存泄漏
    override fun onDestroy() {
        if (mWebView != null) {
            mWebView!!.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            mWebView!!.clearHistory()

            (mWebView!!.parent as ViewGroup).removeView(mWebView)
            mWebView!!.destroy()
            mWebView = null
        }
        super.onDestroy()
    }
}