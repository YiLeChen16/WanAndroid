package com.yl.wanandroid.ui.activity.login

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.yl.wanandroid.Constant.FORGET_PASSWORD_URL
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.activity.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityLoginBinding
import com.yl.wanandroid.ext.initAgreement
import com.yl.wanandroid.ext.setEditTextChange
import com.yl.wanandroid.ext.setTextHide
import com.yl.wanandroid.ext.setTextShow
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.activity.MainActivity
import com.yl.wanandroid.ui.activity.WebViewActivity
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.utils.getStringFromResource
import com.yl.wanandroid.viewmodel.login.LoginActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 登录页面
 * @author YL Chen
 * @date 2025/2/18 15:50
 * @version 1.0
 */
@AndroidEntryPoint
class LoginActivity :
    BaseVMActivity<ActivityLoginBinding, LoginActivityViewModel>(R.layout.activity_login) {
    private var isShowPassword = false

    @Inject
    lateinit var appViewModel: AppViewModel
    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        updateLoginState()
        mBinding.cbAgreement.initAgreement(this)
        initListener()
    }

    private fun initListener() {
        mBinding.passwordToggle.setOnClickListener {
            setPasswordHideOrShow()
        }
        mBinding.edAccount.setEditTextChange(::updateLoginState,lifecycleScope)
        mBinding.edPassword.setEditTextChange(::updateLoginState,lifecycleScope)
        mBinding.btnLogin.setOnClickListener {
            //登录
            toLogin()
        }
        mBinding.cvRegister.setOnClickListener {
            //跳转到注册页面
            startActivity(
                Intent(this@LoginActivity, RegisterActivity::class.java)
            )
        }

        //以游客方式访问,直接跳转到主界面
        mBinding.btnNotLogin.setOnClickListener {
            if(isTaskRoot){
                //为最后一个Activity
                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java)
                )
            }else{
                finish()
            }
        }
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }

        mBinding.tvForgetPassword.setOnClickListener {
            //忘记密码
            WebViewActivity.start(this, FORGET_PASSWORD_URL)
        }

        //协议复选框监听事件,更新登录按钮状态
        mBinding.cbAgreement.setOnCheckedChangeListener { _, _ -> updateLoginState() }
    }

    /**
     * 去登录
     */
    private fun toLogin() {
        val userName = mBinding.edAccount.text?.trim()?.toString()
        val password = mBinding.edPassword.text?.trim()?.toString()

        if (userName.isNullOrEmpty() || userName.length < 11) {
            TipsToast.showTips(getStringFromResource(R.string.error_phone_number))
            return
        }
        if (password.isNullOrEmpty()) {
            TipsToast.showTips(R.string.error_input_password)
            return
        }
        if (!mBinding.cbAgreement.isChecked) {
            TipsToast.showTips(R.string.tips_read_user_agreement)
            return
        }
        mViewModel.login(userName, password)
    }


    /**
     * 更新登录按钮状态
     */
    private fun updateLoginState() {
        val phone = mBinding.edAccount.text.toString()
        val phoneEnable = phone.isNotEmpty() && phone.length == 11
        val password = mBinding.edPassword.text.toString()
        val passwordEnable = password.isNotEmpty()
        val agreementEnable = mBinding.cbAgreement.isChecked

        mBinding.btnLogin.isEnabled = phoneEnable && passwordEnable && agreementEnable
    }

    override fun initVMData() {
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    /**
     * 密码是否可见
     */
    private fun setPasswordHideOrShow() {
        isShowPassword = !isShowPassword
        if (isShowPassword) {
            mBinding.passwordToggle.setImageResource(R.drawable.visibility_off)
            mBinding.edPassword.setTextHide()
        } else {
            mBinding.edPassword.setTextShow()
            mBinding.passwordToggle.setImageResource(R.drawable.visibility)
        }
        mBinding.edPassword.setSelection(mBinding.edPassword.length())
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.user.observe(this){
            if(it == null)
                return@observe
            appViewModel.shouldNavigateToLogin.value = false
            //登录成功
            TipsToast.showTips(R.string.success_login)
            //跳转到主界面
            startActivity(
                Intent(this@LoginActivity, MainActivity::class.java)
            )
            finish()
        }
    }

}