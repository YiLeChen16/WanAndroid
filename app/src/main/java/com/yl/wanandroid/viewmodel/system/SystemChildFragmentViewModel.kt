package com.yl.wanandroid.viewmodel.system

import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.model.SystemDataBeanItem
import com.yl.wanandroid.repository.SystemRepository
import com.yl.wanandroid.ui.adapter.SystemChildContentListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast

/**
 * @description: 体系ViewModel
 * @author YL Chen
 * @date 2024/12/18 22:03
 * @version 1.0
 */
class SystemChildFragmentViewModel : BaseViewModel(),
    SystemChildContentListAdapter.OnKeyWordClickListener {

    //体系数据
    val systemData = MutableLiveData<MutableList<SystemDataBeanItem>?>()

    //所有关键词对应的cid
    var keyWordIdList = ArrayList<Int>()
    //所有关键词
    var keyWordList = ArrayList<String>()

    //选中的关键词cid
    var chooseCid = 0


    //是否跳转
    val isGo = MutableLiveData(false)

    /**
     * 获取体系数据
     */
    fun getSystemData() {
        launchUI(
            errorCallback = { _, errorMSg ->
                TipsToast.showTips(errorMSg)
                systemData.value = null
            },
            requestCall = {
                systemData.value = SystemRepository.getSystemData()
            }
        )
    }


    override fun onReload() {
        getSystemData()
    }

    /**
     * 体系子页面右侧列表的CommonFlowLayout关键词被点击时回调
     * @param id Int
     * @param children List<Children>
     */
    override fun onKeyWordClick(id: Int, children: List<Children>) {
        //清空数据
        keyWordIdList.clear()
        keyWordList.clear()
        //选中页面id
        chooseCid = id
        //获取所有关键词及其id
        for (child in children) {
            keyWordIdList.add(child.id)
            keyWordList.add(child.name)
            LogUtils.d(this,"keyWordList-->$keyWordList")
            LogUtils.d(this,"keyWordIdList-->$keyWordIdList")
        }
        //更新标志变量,通知跳转到SystemActivity
        isGo.value = true
    }


}