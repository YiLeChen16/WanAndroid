package com.yl.wanandroid.ui.activity

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityUserInfoBinding
import com.yl.wanandroid.viewmodel.UserInfoActivityViewModel
import com.yl.wanandroid.BR
import com.yl.wanandroid.model.ViewStateEnum

/**
 * @description: 用户个人信息界面
 * @author YL Chen
 * @date 2025/3/8 14:08
 * @version 1.0
 */
class UserInfoActivity : BaseVMActivity<ActivityUserInfoBinding, UserInfoActivityViewModel>(
    R.layout.activity_user_info
) {
    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)

    }
    override fun initVMData() {
        mViewModel.getUserInfo()
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun getVariableId(): Int {
        return BR.userInfoActivityViewModel
    }
}