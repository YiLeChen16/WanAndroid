package com.yl.wanandroid.ui.fragment

import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseApplication
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.databinding.FragmentProjectBinding
import com.yl.wanandroid.model.ProjectCategoryDataBeanItem
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.ProjectTabViewPagerAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.ProjectFragmentViewModel
import javax.inject.Inject

/**
 * @description: 项目
 * @author YL Chen
 * @date 2024/9/7 16:01
 * @version 1.0
 */
class ProjectFragment :
    BaseVMFragment<FragmentProjectBinding, ProjectFragmentViewModel>(R.layout.fragment_project) {


    private var projectTabViewPagerAdapter: ProjectTabViewPagerAdapter? = null

    @Inject
    lateinit var projectFragmentViewModel: ProjectFragmentViewModel

    override fun initView() {

        super.initView()
        val selectedTypeface =
            Typeface.createFromAsset(BaseApplication.context.assets, "fonts/good_balck.ttf");//下载的字体
        //初始化View
        //禁用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        //为ViewPager设置适配器
        projectTabViewPagerAdapter = ProjectTabViewPagerAdapter(this)
        mBinding.projectViewPager.adapter = projectTabViewPagerAdapter
        // 添加 ViewPager2 的页面变化监听
        mBinding.projectViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.projectTab.getTabAt(position)?.select() // 更新 Tab 选中状态
            }
        })
        //TabLayout选中监听事件
        mBinding.projectTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab == null){
                    return
                }
                // 设置 ViewPager2 当前项
                mBinding.projectViewPager.currentItem = tab.position
                //选中.字体加粗
                val tabSelected = tab.customView as TextView
                tabSelected.textSize = 18f
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_primary))
                //tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                LogUtils.d(this@ProjectFragment,"tabSelected.text-->${tabSelected.text}")
                LogUtils.d(this@ProjectFragment,"tab.text-->${tab.text}")
                tabSelected.setTypeface(selectedTypeface)
                tabSelected.text = (tab.customView as TextView).text
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if(tab == null){
                    return
                }
                //未选中
                val tabSelected = tab.customView as TextView
                tabSelected.textSize = 14f
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_onSurface))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
                //由于此处的tab item是自定义的TextView，而不是TabLayout.Tab类型
                //故需先获取到tab的自定义View再获取其中的text再赋值，（或是直接不写）
                //不可直接将tab.text赋值过去，因为其为null
                //tabSelected.text = (tab.customView as TextView).text
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    //初始化tab条目的样式和值
    private fun initTab(projectCategoryDataBeanItems: MutableList<ProjectCategoryDataBeanItem>) {
        for (i in 0 until projectCategoryDataBeanItems.size) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null) as TextView
            view.text = projectCategoryDataBeanItems[i].name
            view.tag = projectCategoryDataBeanItems[i].id//将分类tab的id存入tag中
            val tab: TabLayout.Tab = mBinding.projectTab.newTab().setCustomView(view)
            mBinding.projectTab.addTab(tab)
        }
        LogUtils.d(this, "initTab-->mBinding.tab.tabCount==${mBinding.projectTab.tabCount}")
        //mBinding.projectTab.isTabIndicatorFullWidth = false
    }


    override fun initVMData() {
        //获取分类数据
        mViewModel.getProjectCategory()
    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.projectCategoriesData.observe(viewLifecycleOwner) {
            LogUtils.d(this, " mViewModel.projectCategoriesData-->${it}")
            //动态添加tab
            if (it != null) {
                //动态创建Tab条目
                initTab(it)
                //为tabViewPager适配器设置数据
                projectTabViewPagerAdapter?.setFragmentSize(it)
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
            } else {
                mViewModel.changeStateView(ViewStateEnum.VIEW_NET_ERROR)
            }
        }
    }
}