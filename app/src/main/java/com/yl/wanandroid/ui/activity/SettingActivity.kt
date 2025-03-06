package com.yl.wanandroid.ui.activity

import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivitySettingBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.viewmodel.SettingActivityViewModel

/**
 * @description: TODO
 * @author YL Chen
 * @date 2025/3/5 21:06
 * @version 1.0
 */
class SettingActivity:BaseVMActivity<ActivitySettingBinding,SettingActivityViewModel>(R.layout.activity_setting) {
    override fun initVMData() {
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun getVariableId(): Int {
        return BR.settingActivityViewModel
    }
}