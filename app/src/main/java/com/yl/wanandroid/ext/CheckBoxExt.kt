package com.yl.wanandroid.ext

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.yl.wanandroid.R
import com.yl.wanandroid.ui.activity.login.PrivacyPolicyActivity
import com.yl.wanandroid.utils.getColorFromResource
import com.yl.wanandroid.utils.getStringFromResource

/**
 * 初始化协议高亮和点击事件
 * @receiver CheckBox
 * @param context Context
 */
fun CheckBox.initAgreement(
    context: Context
) {
    val agreement = getStringFromResource(R.string.login_agreement)
    val privacySpan = getStringFromResource(R.string.login_privacy_agreement)
    val serviceSpan = getStringFromResource(R.string.login_user_agreement)
    movementMethod = LinkMovementMethod.getInstance()
    val spaBuilder = SpannableStringBuilder(agreement)
    spaBuilder.setSpan(
        object : ClickableSpan() {
            override fun onClick(widget: View) {
                (widget as TextView).highlightColor =
                    getColorFromResource(R.color.transparent)
                PrivacyPolicyActivity.start(
                    context,
                    getStringFromResource(R.string.login_privacy_agreement),
                    getStringFromResource(R.string.user_privacy_policy)
                )
            }

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
                PrivacyPolicyActivity.start(
                    context,
                    getStringFromResource(R.string.login_user_agreement),
                    getStringFromResource(R.string.user_service_policy)
                )
            }

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
    setText(spaBuilder, TextView.BufferType.SPANNABLE)
}