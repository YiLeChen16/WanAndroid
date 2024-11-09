package com.yl.wanandroid.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.BaseViewModel
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.model.SearchResultDataBean
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.repository.SearchRepository
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast
import kotlin.properties.Delegates


/**
 * @description: 搜索界面的共享ViewModel 历史数据持久化
 * @author YL Chen
 * @date 2024/10/21 17:47
 * @version 1.0
 */
object SearchShareViewModel : BaseViewModel() {

    //是否取消搜索
    var cancelSearch = MutableLiveData(false)

    //搜索框中的提示词
    var searchHintKeyWord: String = ""

    //搜索框中的数据
    var editData = MutableLiveData("")

    //历史搜索过的关键词集合
    var searchHistoriesList =
        MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }

    private val searchActivityRepository: SearchRepository? = getRepository()

    //此变量用于控制searchFragment与searchResultFragment的可见性
    //true:表示searchResultFragment界面可见
    //false:表示searchFragment界面可见
    var search_fragment_visibility =
        MutableLiveData<Boolean>()


    //返回按钮状态
    var isBack = MutableLiveData(false)

    //当前搜索关键词
    var mCurrentSearchKeyWord = MutableLiveData<String>()

    //搜索结果列表数据
    var searchResultListData = MutableLiveData<SearchResultDataBean?>()

    //加载更多搜索结果列表数据
    var loadMoreSearchResultListData = MutableLiveData<SearchResultDataBean?>()

    //默认搜索页数
    var mDefaultPage = 0

    //当前搜索结果页数
    var mCurrentPage = MutableLiveData(mDefaultPage)

    //返回按钮点击事件
    fun onBackClick() {
        LogUtils.d(this, "onBackClick-->${isBack.value}")
        this.isBack.value = true
    }

    //搜索按钮点击事件
    fun onSearchClick() {
        LogUtils.d(this, "onSearchClick--")
        if (editData.value?.isNotEmpty()!!) {
            //顺序不能改变,因为搜索的动作是在搜索界面可见之后才会进行,若先进行了可见性修改,会导致搜索的词还是原来的词
            mCurrentSearchKeyWord.value = editData.value
            search_fragment_visibility.value = true
        } else {
            //搜索提示词hint
            //顺序不能改变,因为搜索的动作是在搜索界面可见之后才会进行,若先进行了可见性修改,会导致搜索的词还是原来的词
            mCurrentSearchKeyWord.value = searchHintKeyWord
            search_fragment_visibility.value = true
        }
    }

    //取消搜索按钮点击事件
    fun onCancelSearch() {
        LogUtils.d(this, "onCancelSearch--")
        cancelSearch.value = true
    }

    /**
     * 获取搜索结果列表数据
     * @param k String 搜索关键词
     * @return LiveData<SearchResultDataBean?> 结果数据
     */
    fun getSearchResultData(k: String): LiveData<SearchResultDataBean?> {
        LogUtils.d(this, "getSearchResultData-->k-->$k")
        //不能对要装载的值进行空判断,否则会导致只搜索第一次设置进来的值
        if (search_fragment_visibility.value == true) {//当搜索结果界面可见才进行搜索
            //将搜索的关键词存入历史搜索记录集合中
            //去除重复词
            val iterator = searchHistoriesList.value?.iterator()
            while (iterator!!.hasNext()) {
                val oldKey = iterator.next()
                if (oldKey == k) {
                    // 将历史记录中的 key 移除
                    iterator.remove()
                    //跳出循环
                    break
                }
            }
            /*searchHistoriesList.value?.forEachIndexed { index, key ->
                if (key == k) {
                    // 将历史记录中的 key 移除
                    searchHistoriesList.value?.removeAt(index)
                    return@forEachIndexed//跳出循环
                }
            }*///不可直接这样写,会报java.util.ConcurrentModificationException 不允许在集合遍历时修改集合
            //将当前 k 添加到末尾
            searchHistoriesList.value?.add(k)
            //将当前搜索页面置为第一页
            mCurrentPage.value = mDefaultPage
            LogUtils.d(this, "searchHistoriesList.value-->${searchHistoriesList.value}")
            launchUI(
                errorCallback = { errorCode, errorMsg ->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@SearchShareViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    searchResultListData.value = null
                },
                requestCall = {
                    LogUtils.d(this@SearchShareViewModel, "requestCall")
                    searchResultListData.value =
                        searchActivityRepository?.getSearchResultData(mDefaultPage, k)//默认搜索第一页数据
                }
            )
        }
        return searchResultListData
    }


    //搜索热词数据
    var searchHotKeyData = MutableLiveData<MutableList<SearchHotKeyDataBean>?>()

    //获取搜索热词
    fun getSearchHotkeyData(): LiveData<MutableList<SearchHotKeyDataBean>?> {
        launchUI(
            errorCallback = { _, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@SearchShareViewModel, "errorCallback-->$errorMsg")
                changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                searchHotKeyData.value = null
            },
            requestCall = {
                searchHotKeyData.value = searchActivityRepository?.getSearchHotKeyData()
            }
        )
        return searchHotKeyData
    }


    //TODO::
    override fun onReload() {

    }

    /**
     * 加载更多搜索结果数据
     * @param k String
     */
    fun loadMoreSearchResultData(): LiveData<SearchResultDataBean?> {
        //当前页码数+1
        mCurrentPage.value = mCurrentPage.value?.plus(1)
        launchUI(
            errorCallback = { errorCode, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@SearchShareViewModel, "errorCallback-->$errorMsg")
                loadMoreSearchResultListData.value = null
                //还原页码数
                mCurrentPage.value = mCurrentPage.value?.minus(1)
            },
            requestCall = {
                LogUtils.d(this@SearchShareViewModel, "requestCall")
                loadMoreSearchResultListData.value =
                    searchActivityRepository?.getSearchResultData(
                        mCurrentPage.value!!,
                        mCurrentSearchKeyWord.value!!
                    )
            }
        )
        return loadMoreSearchResultListData
    }
}