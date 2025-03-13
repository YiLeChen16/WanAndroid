package com.yl.wanandroid.ui.activity

import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivitySettingBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.utils.APKVersionCodeUtils
import com.yl.wanandroid.utils.CacheDataUtils
import com.yl.wanandroid.viewmodel.SettingActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 设置界面
 * @author YL Chen
 * @date 2025/3/5 21:06
 * @version 1.0
 */
@AndroidEntryPoint
class SettingActivity :
    BaseVMActivity<ActivitySettingBinding, SettingActivityViewModel>(R.layout.activity_setting) {

    @Inject
    lateinit var appViewModel: AppViewModel

    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        //初始化退出登录按钮状态
        if (appViewModel.isUserLoginNotJump()) {
            //用户已登录
            mBinding.btnLogout.isEnabled = true
        } else {
            //用户未登录
            mBinding.btnLogout.isEnabled = false
        }
        //获取并设置当前版本号
        mBinding.tvVersionCode.text =
            getString(R.string.current_version, APKVersionCodeUtils.getVersionCode(this))
        //退出登录按钮点击
        mBinding.btnLogout.setOnClickListener {
            //弹窗提示用户是否退出登录
            val customDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)
            val dialog = MaterialAlertDialogBuilder(this)
                .setView(customDialogView)
                .setCancelable(true)
                .setOnCancelListener {
                    it.cancel()
                }
                .show()
            val cancelButton = customDialogView.findViewById<TextView>(R.id.tv_cancel)
            val positiveButton = customDialogView.findViewById<TextView>(R.id.tv_positive)
            cancelButton.setOnClickListener {
                //关闭对话框
                dialog.dismiss()
            }
            positiveButton.setOnClickListener {
                //关闭对话框
                dialog.dismiss()
                //退出登录
                mViewModel.logout()
                //跳转到首页
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)//跳转到一个新的主界面,并清除之前所有的Activity栈
                startActivity(intent)
                finish()//关闭当前界面
            }
        }
    }

    override fun initVMData() {
        mViewModel.getTotalCache()
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.gotoPrivacy.observe(this) {
            if (it) {
                //跳转到隐私政策界面
                PrivacyPolicyActivity.start(
                    this,
                    getString(R.string.login_privacy_agreement),
                    getString(R.string.user_privacy_policy)
                )
                mViewModel.gotoPrivacy.value = false
            }
        }

        mViewModel.gotoUserInfo.observe(this) {
            if (it) {
                //跳转到个人信息界面
                startActivity(Intent(this, UserInfoActivity::class.java))
                mViewModel.gotoUserInfo.value = false
            }
        }

        mViewModel.gotoAbout.observe(this) {
            if (it) {
                startActivity(Intent(this, AboutActivity::class.java))
                mViewModel.gotoAbout.value = false
            }
        }
    }

    override fun getVariableId(): Int {
        return BR.settingActivityViewModel
    }
}