package com.yl.wanandroid.ui.activity.login

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.activity.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityRegisterBinding
import com.yl.wanandroid.ext.initAgreement
import com.yl.wanandroid.ext.setEditTextChange
import com.yl.wanandroid.ext.setTextHide
import com.yl.wanandroid.ext.setTextShow
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.activity.MainActivity
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.utils.getStringFromResource
import com.yl.wanandroid.viewmodel.login.RegisterActivityViewModel
import javax.inject.Inject

/**
 * @description: 注册页面
 * @author YL Chen
 * @date 2025/2/22 17:23
 * @version 1.0
 */
class RegisterActivity :
    BaseVMActivity<ActivityRegisterBinding, RegisterActivityViewModel>(R.layout.activity_register) {
    private var isShowPassword = false
    private var isShowPasswordAgain = false

    @Inject
    lateinit var appViewModel: AppViewModel

    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        mBinding.cbAgreement.initAgreement(this)
        updateRegisterState()
        initListener()
    }

    private fun initListener() {
        mBinding.passwordToggle.setOnClickListener {
            setPasswordHideOrShow()
        }
        mBinding.passwordToggleAgain.setOnClickListener {
            setPasswordAgainHideOrShow()
        }
        mBinding.edAccount.setEditTextChange(::updateRegisterState, lifecycleScope)
        mBinding.edPassword.setEditTextChange(::updateRegisterState, lifecycleScope)
        mBinding.edPasswordAgain.setEditTextChange(::updateRegisterState, lifecycleScope)
        mBinding.btnRegistser.setOnClickListener {
            toRegister()
        }
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }

        //协议复选框监听事件,更新登录按钮状态
        mBinding.cbAgreement.setOnCheckedChangeListener { _, _ -> updateRegisterState() }
    }

    /**
     * 更新注册按钮状态
     */
    private fun updateRegisterState() {
        val phone = mBinding.edAccount.text.toString()
        val phoneEnable = phone.isNotEmpty() && phone.length == 11
        val password = mBinding.edPassword.text.toString()
        val passwordEnable = password.isNotEmpty()
        val passwordAgain = mBinding.edPasswordAgain.text.toString()
        val passwordAgainEnable = passwordAgain.isNotEmpty()
        val agreementEnable = mBinding.cbAgreement.isChecked

        mBinding.btnRegistser.isEnabled =
            phoneEnable && passwordEnable && passwordAgainEnable && agreementEnable
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

    /**
     * 密码是否可见
     */
    private fun setPasswordAgainHideOrShow() {
        isShowPasswordAgain = !isShowPasswordAgain
        if (isShowPasswordAgain) {
            mBinding.passwordToggleAgain.setImageResource(R.drawable.visibility_off)
            mBinding.edPasswordAgain.setTextHide()
        } else {
            mBinding.passwordToggleAgain.setImageResource(R.drawable.visibility)
            mBinding.edPasswordAgain.setTextShow()
        }
        mBinding.edPasswordAgain.setSelection(mBinding.edPassword.length())
    }

    /**
     * 去注册
     */
    private fun toRegister() {
        val userName = mBinding.edAccount.text?.trim()?.toString()
        val password = mBinding.edPassword.text?.trim()?.toString()
        val passwordAgain = mBinding.edPasswordAgain.text?.trim()?.toString()

        if (userName.isNullOrEmpty() || userName.length < 11) {
            TipsToast.showTips(getStringFromResource(R.string.error_phone_number))
            return
        }
        if (password.isNullOrEmpty()) {
            TipsToast.showTips(R.string.error_input_password)
            return
        }
        if (passwordAgain.isNullOrEmpty()) {
            TipsToast.showTips(R.string.error_input_password_again)
            return
        }
        if (password != passwordAgain) {
            TipsToast.showWarningTips(R.string.error_input_password_not_equal)
            return
        }
        if (!mBinding.cbAgreement.isChecked) {
            TipsToast.showTips(R.string.tips_read_user_agreement)
            return
        }
        mViewModel.register(userName, password, passwordAgain)
    }

    override fun initVMData() {
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.user.observe(this) {
            if (it == null) return@observe
            appViewModel.shouldNavigateToLogin.value = false
            //注册
            TipsToast.showSuccessTips("注册成功！")
            //跳转到登录界面
            //跳转到主界面
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)//跳转到一个新的主界面,并清除之前所有的Activity栈
            startActivity(
                intent
            )
            finish()
        }
    }
}