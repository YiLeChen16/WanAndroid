package com.yl.wanandroid.ui.activity

import android.content.Intent
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.yl.wanandroid.Constant
import com.yl.wanandroid.Constant.FORGET_PASSWORD_URL
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityLoginBinding
import com.yl.wanandroid.ext.textChangeFlow
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.utils.getColorFromResource
import com.yl.wanandroid.utils.getStringFromResource
import com.yl.wanandroid.viewmodel.LoginActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * @description: 登录页面//TODO::
 * @author YL Chen
 * @date 2025/2/18 15:50
 * @version 1.0
 */
class LoginActivity :
    BaseVMActivity<ActivityLoginBinding, LoginActivityViewModel>(R.layout.activity_login) {
    private var isShowPassword = true

    override fun initView() {
        super.initView()
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        updateLoginState()
        initAgreement()
        initListener()
    }

    private fun initListener() {
        mBinding.passwordToggle.setOnClickListener {
            setPasswordHide()
        }
        setEditTextChange(mBinding.edAccount)
        setEditTextChange(mBinding.edPassword)
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
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(Constant.TO_WEB_URL, FORGET_PASSWORD_URL)//携带数据跳转
            startActivity(intent)
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
     * 监听EditText文本变化
     */
    private fun setEditTextChange(editText: EditText) {
        editText.textChangeFlow()
            .debounce(300)
            .flowOn(Dispatchers.IO)
            .onEach {
                withContext(Dispatchers.Main) { // 切换回主线程
                    updateLoginState()
                }
            }
            .launchIn(lifecycleScope)
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
    private fun setPasswordHide() {
        isShowPassword = !isShowPassword
        if (isShowPassword) {
            mBinding.passwordToggle.setImageResource(R.drawable.visibility_off)
            mBinding.edPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            mBinding.edPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            mBinding.passwordToggle.setImageResource(R.drawable.visibility)
        }
        mBinding.edPassword.setSelection(mBinding.edPassword.length())
    }

    /**
     * 初始化协议点击
     */
    private fun initAgreement() {
        val agreement = getStringFromResource(R.string.login_agreement)
        try {
            mBinding.cbAgreement.movementMethod = LinkMovementMethod.getInstance()
            val spaBuilder = SpannableStringBuilder(agreement)
            val privacySpan = getStringFromResource(R.string.login_privacy_agreement)
            val serviceSpan = getStringFromResource(R.string.login_user_agreement)
            spaBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        (widget as TextView).highlightColor =
                            getColorFromResource(R.color.transparent)
                        LogUtils.d(
                            this@LoginActivity,
                            "privacySpan-->${getStringFromResource(R.string.login_privacy_agreement)}"
                        )
                        PrivacyPolicyActivity.start(
                            this@LoginActivity,
                            getStringFromResource(R.string.login_privacy_agreement)
                        )
                    }

                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = getColorFromResource(R.color.md_theme_primary)
                        ds.isUnderlineText = false
                        ds.clearShadowLayer()
                    }
                },
                spaBuilder.indexOf(privacySpan),
                spaBuilder.indexOf(privacySpan) + privacySpan.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spaBuilder.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        (widget as TextView).highlightColor =
                            getColorFromResource(R.color.transparent)
                        LogUtils.d(
                            this@LoginActivity,
                            "privacySpan-->${getStringFromResource(R.string.login_user_agreement)}"
                        )
                        PrivacyPolicyActivity.start(
                            this@LoginActivity,
                            getStringFromResource(R.string.login_user_agreement)
                        )
                    }

                    @RequiresApi(Build.VERSION_CODES.M)
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = getColorFromResource(R.color.md_theme_primary)
                        ds.isUnderlineText = false
                        ds.clearShadowLayer()
                    }
                },
                spaBuilder.indexOf(serviceSpan),
                spaBuilder.indexOf(serviceSpan) + serviceSpan.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            mBinding.cbAgreement.setText(spaBuilder, TextView.BufferType.SPANNABLE)
        } catch (e: Exception) {
            LogUtils.e(e, e.message.toString())
            mBinding.cbAgreement.text = agreement
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.user.observe(this){
            if(it == null)
                return@observe
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