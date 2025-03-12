package com.yl.wanandroid.ui.activity

import android.text.method.LinkMovementMethod
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseActivity
import com.yl.wanandroid.databinding.ActivityAboutBinding
import com.yydcdut.markdown.MarkdownConfiguration
import com.yydcdut.markdown.MarkdownProcessor
import com.yydcdut.markdown.syntax.text.TextFactory

/**
 * @description: 关于界面
 * @author YL Chen
 * @date 2025/3/11 22:05
 * @version 1.0
 */
class AboutActivity : BaseActivity<ActivityAboutBinding>(R.layout.activity_about) {
    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableLoadMore(false)
        mRefreshLayout.setEnableRefresh(false)
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        //设置MarkDown解释器
        val markdownProcessor = MarkdownProcessor(this)
        markdownProcessor.factory(TextFactory.create())
        val configuration =
            MarkdownConfiguration.Builder(this)
                .setLinkFontColor(getColor(R.color.md_theme_primary))
                .build()
        markdownProcessor.config(configuration)
        mBinding.tvIntroduce.text = markdownProcessor.parse(getString(R.string.about_introduce))
        mBinding.tvIntroduce.movementMethod =
            LinkMovementMethod.getInstance()//由于MarkDown解释器的setOnLinkClickCallback无效,故需从TextView自身监听超链接的点击跳转事件
        mMultiplyStateView.showSuccess()//展示成功视图
    }

    override fun initData() {

    }
}