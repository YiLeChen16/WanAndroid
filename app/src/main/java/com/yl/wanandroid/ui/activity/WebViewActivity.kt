package com.yl.wanandroid.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.activity.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityWebBinding
import com.yl.wanandroid.model.ViewStateEnum
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
    private var mLastUrl: String? = null
    private var mWebView: WebView? = null
    private var previous: MutableList<String> = mutableListOf()

    companion object {

        private lateinit var url: String

        fun start(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            context.startActivity(intent)
            this.url = url
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun initView() {
        super.initView()
        //禁止下拉上拉加载
        mRefreshLayout.setEnableLoadMore(false)
        mRefreshLayout.setEnableRefresh(false)
        mWebView = mBinding.webView
        val webSettings = mWebView?.settings
        webSettings?.javaScriptEnabled = true
        webSettings?.loadsImagesAutomatically = true
        webSettings?.domStorageEnabled = true//不设置会导致微信公众号图片资源无法加载
        //允许该网页中http和https混合使用，Android 5之后默认不允许https安全站点去加载http不安全的资源
        webSettings?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings?.blockNetworkImage = false


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

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        LogUtils.i(this, "onBackPressed")
        val size: Int = previous.size
        if (size > 0) {
            mWebView?.loadUrl(previous[size - 1])
            previous.removeAt(size - 1)
            LogUtils.i(this, "size-->$size")
        } else {
            finish()
            super.onBackPressedDispatcher.onBackPressed()
        }
    }


    override fun initData() {
        super.initData()
        mWebView?.webViewClient = object : WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            override fun shouldOverrideUrlLoading(
                view: WebView?, request: WebResourceRequest?
            ): Boolean {
                try {
                    if (!request?.url.toString().startsWith("http://") && !request?.url.toString()
                            .startsWith("https://")
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request?.url.toString()))
                        startActivity(intent)
                        return true
                    }
                } catch (e: Exception) {//防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    LogUtils.d(this@WebViewActivity, "此APP未安装")
                    return true//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出ERR_UNKNOWN_URL_SCHEME的错误页面
                }
                //在WebView进行二次跳转时保留跳转前的url,存储在返回栈中
                if (mLastUrl != null) {
                    if (previous.isEmpty() || previous[previous.size - 1] != mLastUrl) {
                        previous.add(mLastUrl!!)
                    }
                }
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view?.loadUrl(request?.url.toString())
                return true
            }

            //在页面加载完毕且用户可见后才更换视图状态
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                //更改视图状态
                if (mViewModel.isNetError.value!!) {
                    mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                } else {
                    mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                }
                mLastUrl = url
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                view?.clearHistory()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                //标记网络错误
                mViewModel.isNetError.value = true
            }

        }
        //加载传递过来的网页
        //判断是否为空
        LogUtils.d(this, "initData-->url-->$url")
        //将跳转过来的url数据存储到ViewModel中
        mViewModel.url.value = url
        /*        //加载网页
                mWebView?.loadUrl(url)*/

    }

    override fun initVMData() {
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.url.observe(this) {
            if (it == null) return@observe
            mBinding.webView.loadUrl(url)
        }
    }

    //在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。避免WebView内存泄漏
    override fun onDestroy() {
        if (mWebView != null) {
            mWebView!!.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            mWebView!!.clearCache(true)
            mWebView!!.clearHistory()

            (mWebView!!.parent as ViewGroup).removeView(mWebView)
            mWebView!!.destroy()
            mWebView = null
        }
        super.onDestroy()
    }

    override fun getVariableId(): Int {
        return BR.webActivityViewModel
    }
}