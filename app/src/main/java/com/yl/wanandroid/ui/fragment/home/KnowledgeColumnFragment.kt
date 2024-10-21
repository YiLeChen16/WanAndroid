package com.yl.wanandroid.ui.fragment.home

import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentKnowledgeColumnBinding
import com.yl.wanandroid.viewmodel.home.KnowledgeColumnFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @description: 首页知识专栏Fragment TODO
 * @author YL Chen
 * @date 2024/10/20 16:08
 * @version 1.0
 */
class KnowledgeColumnFragment:BaseVMFragment<FragmentKnowledgeColumnBinding, KnowledgeColumnFragmentViewModel>(
    R.layout.fragment_knowledge_column) {
    companion object{
        private var knowledgeColumnFragment: KnowledgeColumnFragment? = null
        fun newInstance() : KnowledgeColumnFragment {
            if(knowledgeColumnFragment == null){
                knowledgeColumnFragment = KnowledgeColumnFragment()
            }
            return knowledgeColumnFragment!!
        }
    }
    override fun initVMData() {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            //测试模拟加载延迟
            delay(2000)
            //模拟加载成功
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }
    }
}