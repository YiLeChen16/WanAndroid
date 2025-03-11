package com.yl.wanandroid.ui.activity

import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.yl.wanandroid.BR
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityUserInfoBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.UserInfoActivityViewModel


/**
 * @description: 用户个人信息界面 //TODO:图片选择
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
        //头像被点击
        mBinding.ivHead.setOnClickListener {
            TipsToast.showWarningTips(R.string.tips_in_development)
        }
        //性别被点击
        mBinding.tvSex.setOnClickListener {
            //弹出底部弹窗
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            //设置布局
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sex, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
            val tvM = view.findViewById<TextView>(R.id.tv_m)
            val tvWm = view.findViewById<TextView>(R.id.tv_wm)
            val cancel = view.findViewById<TextView>(R.id.tv_cancel)
            tvM.setOnClickListener {
                mBinding.tvSex.text = resources.getString(R.string.tips_sex_m)
                bottomSheetDialog.dismiss()
            }
            tvWm.setOnClickListener {
                mBinding.tvSex.text = resources.getString(R.string.tips_sex_wm)
                bottomSheetDialog.dismiss()
            }
            cancel.setOnClickListener {
                //取消
                bottomSheetDialog.dismiss()
            }
        }
        //生日被点击
        mBinding.tvBirthday.setOnClickListener {
            val datePickerDialog = BottomSheetDialog(this)
            val view = LayoutInflater.from(this).inflate(R.layout.dialog_date_picker, null)
            datePickerDialog.setContentView(view)
            datePickerDialog.show()
            val datePicker = view.findViewById<DatePicker>(R.id.dp_date)
            val btnOk = view.findViewById<MaterialButton>(R.id.btn_ok)
            btnOk.setOnClickListener {
                //注意月份是从0开始的,需加一
                mBinding.tvBirthday.text = getString(
                    R.string.birthday,
                    datePicker.year,
                    datePicker.month + 1,
                    datePicker.dayOfMonth
                )
                datePickerDialog.dismiss()
            }
        }
        //保存按钮点击
        mBinding.btnSave.setOnClickListener {
            //获取上述信息并保存到数据库中
            mViewModel.saveUserInfo(mBinding.tvSex.text.toString(),mBinding.tvBirthday.text.toString())
            TipsToast.showSuccessTips("保存成功~")
        }
    }


    override fun initVMData() {
        mViewModel.getUserInfo()
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
    }

    override fun getVariableId(): Int {
        return BR.userInfoActivityViewModel
    }
}