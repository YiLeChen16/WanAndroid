package com.yl.wanandroid.ui.activity.system

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.activity.BaseActivity
import com.yl.wanandroid.databinding.ActivitySystemBinding
import com.yl.wanandroid.ui.adapter.system.left.SystemActivityViewPagerAdapter
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * @description: 展示体系子页面文章的Activity
 * @author YL Chen
 * @date 2025/2/6 16:52
 * @version 1.0
 */
@AndroidEntryPoint
class SystemActivity : BaseActivity<ActivitySystemBinding>(R.layout.activity_system) {

    private var keywords: ArrayList<String>? = null
    private var chooseCid: Int? = null
    private var cids: ArrayList<Int>? = null

    private lateinit var systemActivityViewPagerAdapter: SystemActivityViewPagerAdapter

    override fun initView() {
        super.initView()
        //禁用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        mBinding.systemTab.isTabIndicatorFullWidth = false
        //给ViewPager设置适配器
        systemActivityViewPagerAdapter = SystemActivityViewPagerAdapter(this)
        mBinding.systemViewPager.adapter = systemActivityViewPagerAdapter
        //联动TabLayout和ViewPager2
        //TabLayout选中监听事件
        mBinding.systemTab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                if (tab == null) {
                    return
                }
                // 设置 ViewPager2 当前项
                mBinding.systemViewPager.currentItem = tab.position
                //选中.字体加粗
                val tabSelected = tab.customView as TextView
                tabSelected.setTextColor(resources.getColor(R.color.md_theme_primary,theme))//此处若使用context.getColor()方法会无法适配暗黑主题下的字体颜色
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                tabSelected.text = (tab.customView as TextView).text
            }

            override fun onTabUnselected(tab: Tab?) {
                if (tab == null) {
                    return
                }
                //未选中
                val tabSelected = tab.customView as TextView
                tabSelected.setTextColor(resources.getColor(R.color.md_theme_onSurface,theme))//此处若使用context.getColor()方法会无法适配暗黑主题下的字体颜色
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
                tabSelected.text = (tab.customView as TextView).text
            }

            override fun onTabReselected(tab: Tab?) {
            }
        })
        // ViewPager2 的页面变化监听
        mBinding.systemViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //选中对应Tab
                //必须在post中调用,否则会选中对应tab但条目过多超过屏幕宽度时不会滚动到选中的tab
                mBinding.systemTab.post {
                    mBinding.systemTab.getTabAt(position)?.select()
                }

            }
        })
        mMultiplyStateView.showSuccess()//展示成功视图
    }

    @SuppressLint("InflateParams")
    override fun initData() {
        //接受跳转的传递过来的数据,并存入ViewModel中
        val extras = intent.extras
        val bundle = extras?.getBundle(Constant.TO_SYSTEM)
        this.cids = bundle?.getIntegerArrayList(Constant.SYSTEM_ALL_KEYWORD_CID)
        this.keywords = bundle?.getStringArrayList(Constant.SYSTEM_ALL_KEYWORD)
        this.chooseCid = bundle?.getInt(Constant.SYSTEM_CHOOSE_KEYWORD_CID)
        LogUtils.d(this, "cids-->$cids")
        LogUtils.d(this, "keywords-->$keywords")
        LogUtils.d(this, "chooseCid-->$chooseCid")

        //为ViewPager设置数据
        systemActivityViewPagerAdapter.setData(cids)
        //为tabLayout设置数据
        mBinding.systemTab.post {
            keywords?.forEach { keyword ->
                val newTab = mBinding.systemTab.newTab()
                val view = LayoutInflater.from(this).inflate(R.layout.item_tab, null) as TextView
                view.text = keyword
                newTab.setCustomView(view)
                mBinding.systemTab.addTab(newTab)
            }
        }

        //ViewPager选中chooseCid对应的页面
        val indexOf = cids?.indexOf(chooseCid) ?: return
        LogUtils.d(this, "indexOf-->$indexOf")
        mBinding.systemViewPager.post {
            mBinding.systemViewPager.setCurrentItem(indexOf, false)
        }
    }

}