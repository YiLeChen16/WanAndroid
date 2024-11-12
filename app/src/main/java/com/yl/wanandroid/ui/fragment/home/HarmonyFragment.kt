package com.yl.wanandroid.ui.fragment.home

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentHarmonyBinding
import com.yl.wanandroid.viewmodel.home.HarmonyFragmentViewModel
import com.yl.wanandroid.BR


/**
 * @description: 首页鸿蒙Fragment TODO
 * @author YL Chen
 * @date 2024/10/20 16:08
 * @version 1.0
 */
class HarmonyFragment:BaseVMFragment<FragmentHarmonyBinding, HarmonyFragmentViewModel>(
    R.layout.fragment_harmony) {
    companion object{
        private var knowledgeColumnFragment: HarmonyFragment? = null
        fun newInstance() : HarmonyFragment {
            if(knowledgeColumnFragment == null){
                knowledgeColumnFragment = HarmonyFragment()
            }
            return knowledgeColumnFragment!!
        }
    }

    override fun initVMData() {

    }

    override fun getVariableId(): Int {
        return BR.harmonyFragmentViewModel
    }
}