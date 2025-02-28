package com.yl.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseActivity
import com.yl.wanandroid.databinding.ActivityPrivacyPolicyBinding

/**
 * @description: 协议界面
 * @author YL Chen
 * @date 2025/2/21 17:49
 * @version 1.0
 */
class PrivacyPolicyActivity:BaseActivity<ActivityPrivacyPolicyBinding>(R.layout.activity_privacy_policy) {
    companion object {

        private lateinit var title: String

        fun start(context: Context, title:String) {
            val intent = Intent(context, PrivacyPolicyActivity::class.java)
            context.startActivity(intent)
            this.title = title
        }
    }

    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        mBinding.titleBar.title = PrivacyPolicyActivity.title//设置标题
        mMultiplyStateView.showSuccess()//展示成功视图
    }
    override fun initData() {

    }
}