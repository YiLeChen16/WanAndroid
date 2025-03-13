package com.yl.wanandroid.ui.fragment.system

import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentSystemCourseBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.CourseListAdapter
import com.yl.wanandroid.viewmodel.system.SystemCourseFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 体系下的课程子页面
 * @author YL Chen
 * @date 2024/12/18 21:58
 * @version 1.0
 */
@AndroidEntryPoint
class SystemCourseFragment :
    BaseVMFragment<FragmentSystemCourseBinding, SystemCourseFragmentViewModel>(
        R.layout.fragment_system_course
    ) {

    @Inject
    lateinit var courseListAdapter: CourseListAdapter

    override fun initView() {
        super.initView()
        //禁止加载更多
        mRefreshLayout.setEnableLoadMore(false)
        //刷新监听
        mRefreshLayout.setOnRefreshListener {
            mViewModel.getSystemCourse()
        }
        //为列表设置适配器和布局管理器
        mBinding.courseList.adapter = courseListAdapter
        mBinding.courseList.layoutManager = LinearLayoutManager(context)
    }

    override fun initVMData() {
        //获取数据
        mViewModel.getSystemCourse()
    }

    override fun observeLiveData() {
        //观察数据
        mViewModel.systemCourse.observe(viewLifecycleOwner) {
            mRefreshLayout.finishRefresh()//刷新结束
            if (it == null) {
                //切换状态视图
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                return@observe
            }
            //为列表适配器设置数据
            courseListAdapter.setData(it)
            //切换状态视图
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }
    }
}