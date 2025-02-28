package com.yl.wanandroid.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.yl.wanandroid.R
import com.yl.wanandroid.app.AppViewModel
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivityCollectBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.BlogListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.home.CollectViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @description: 收藏界面
 * @author YL Chen
 * @date 2025/2/26 13:57
 * @version 1.0
 */
@AndroidEntryPoint
class CollectActivity :
    BaseVMActivity<ActivityCollectBinding, CollectViewModel>(R.layout.activity_collect) {
    @Inject
    lateinit var listAdapter: BlogListAdapter

    @Inject
    lateinit var appViewModel: AppViewModel

    override fun initView() {
        super.initView()
        //设置适配器和布局管理器
        mBinding.collectList.adapter = listAdapter
        mBinding.collectList.layoutManager = LinearLayoutManager(this)
    }

    override fun initVMData() {
        mViewModel.getAllCollectArticle()
        //设置收藏监听事件
        listAdapter.setOnCollectionEventListener(appViewModel)
        LogUtils.d(this, "appViewModel-->$appViewModel")

    }

    override fun observeLiveData() {
        super.observeLiveData()
        mViewModel.collectArticles.observe(this) {
            if (it == null)
                return@observe
            LogUtils.d(this@CollectActivity, "data-->${it.datas}")
            listAdapter.setData(it.datas)
            mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
        }

        appViewModel.isUserLogin.observe(this) {
            if (!it) {
                //跳转到登录页面
                startActivity(Intent(this@CollectActivity, LoginActivity::class.java))
            }
        }

        appViewModel.event.observe(this) {
            //更新我的收藏页面的列表
            listAdapter.removeItemByOriginId(it.originId)
        }
    }

}