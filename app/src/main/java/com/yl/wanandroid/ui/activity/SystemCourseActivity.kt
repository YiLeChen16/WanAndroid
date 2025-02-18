package com.yl.wanandroid.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.BR
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivitySystemCourseBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import com.yl.wanandroid.viewmodel.system.SystemCourseActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @description: 体系课程界面条目跳转详情页
 * @author YL Chen
 * @date 2025/2/15 16:26
 * @version 1.0
 */
@AndroidEntryPoint
class SystemCourseActivity :
    BaseVMActivity<ActivitySystemCourseBinding, SystemCourseActivityViewModel>(R.layout.activity_system_course) {
    @Inject
    lateinit var courseArticleAdapter: BlogListAdapter

    override fun initView() {
        super.initView()
        //警用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        //返回键监听
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        //为列表设置适配器
        mBinding.articleList.adapter = courseArticleAdapter
        mBinding.articleList.layoutManager = LinearLayoutManager(this)
        //加载更多监听
        mRefreshLayout.setOnLoadMoreListener {
            mViewModel.loadMoreSystemCourseArticleDataByCid(
                mViewModel.courseId,
                mViewModel.judgeSort()
            )
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
        //获取跳转携带的数据并存储到ViewModel中
        val bundle = intent.extras ?: return
        mViewModel.courseId = bundle.getInt(Constant.SYSTEM_COURSE_ID)
        mViewModel.courseName.set(bundle.getString(Constant.SYSTEM_COURSE_NAME))
        LogUtils.d(this, "mViewModel.courseId-->${mViewModel.courseId}")
        LogUtils.d(this, "mViewModel.courseName-->${mViewModel.courseName}")
        mViewModel.getSystemCourseArticleDataByCid(mViewModel.courseId)
    }

    //观察数据
    override fun observeLiveData() {
        mViewModel.sortState.observe(this) {
            mBinding.sort.isSelected = it//切换控件的选中状态
            //重新标志当前加载更多是否有数据
            mRefreshLayout.setNoMoreData(false)
        }
        mViewModel.courseArticleData.observe(this) {
            if (it == null) {
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                return@observe
            }
            //为适配器设置数据
            courseArticleAdapter.setData(it.datas)
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }
        mViewModel.loadMoreCourseArticleData.observe(this) {
            if (it == null) {
                return@observe
            }
            //加载完成
            mRefreshLayout.finishLoadMore()
            if (it.curPage == it.pageCount + 1) {
                //最后一页
                TipsToast.showTips(R.string.tip_toast_last_page)
                mRefreshLayout.finishLoadMoreWithNoMoreData()
            } else {
                //为适配器添加数据
                courseArticleAdapter.addData(it.datas)
                TipsToast.showTips(R.string.tip_toast_load_more_success)
            }
        }
    }


    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.systemCourseActivityViewModel
    }
}