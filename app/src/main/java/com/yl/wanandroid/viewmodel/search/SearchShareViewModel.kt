package com.yl.wanandroid.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yl.wanandroid.base.viewModel.BaseViewModel
import com.yl.wanandroid.model.ArticleDataBean
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.repository.search.SearchHistoryRepository
import com.yl.wanandroid.repository.search.SearchRepository
import com.yl.wanandroid.room.entity.SearchHistoryItem
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.TipsToast


/**
 * @description: 搜索界面的共享ViewModel 历史数据持久化
 * @author YL Chen
 * @date 2024/10/21 17:47
 * @version 1.0
 */
object SearchShareViewModel : BaseViewModel() {

    //标记当前是否有网络
    var isNoNetWork = false

    //是否取消搜索
    var cancelSearch = MutableLiveData(false)

    //搜索框中的提示词
    var searchHintKeyWord: String = ""

    //搜索框中的数据
    var editData = MutableLiveData("")

    //历史搜索过的关键词集合
    //MutableLiveData，可以修改其值。
    val searchHistoriesList: MutableLiveData<MutableList<SearchHistoryItem>> =
        MutableLiveData<MutableList<SearchHistoryItem>>().apply {
            // 可以在这里添加一些初始化逻辑，如设置默认值等
            getAllSearchHistory()
        }


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
    var searchResultListData = MutableLiveData<ArticleDataBean?>()

    //加载更多搜索结果列表数据
    var loadMoreSearchResultListData = MutableLiveData<ArticleDataBean?>()

    //默认搜索页数
    private var mDefaultPage = 0

    //当前搜索结果页数
    private var mCurrentPage = mDefaultPage

    //返回按钮点击事件
    fun onBackClick() {
        LogUtils.d(this, "onBackClick-->${isBack.value}")
        this.isBack.value = true
    }

    //搜索按钮点击事件
    fun onSearchClick() {
        LogUtils.d(this, "onSearchClick--")
        //判断是否有网络
        if(isNoNetWork){
            TipsToast.showTips("莫得网络哦~")
            return
        }
        LogUtils.d(this,"editData.value?.isNotEmpty()!!-->${editData.value?.isNotEmpty()}")
        if (editData.value?.isNotEmpty()!!) {
            LogUtils.d(this,"if")
            //顺序不能改变,因为搜索的动作是在搜索界面可见之后才会进行,若先进行了可见性修改,会导致搜索的词还是原来的词
            mCurrentSearchKeyWord.value = editData.value
            search_fragment_visibility.value = true
        } else if (searchHintKeyWord.isNotEmpty()){
            LogUtils.d(this,"else")
            LogUtils.d(this,"searchHintKeyWord-->${searchHintKeyWord}")
            //搜索提示词hint
            //顺序不能改变,因为搜索的动作是在搜索界面可见之后才会进行,若先进行了可见性修改,会导致搜索的词还是原来的词
            mCurrentSearchKeyWord.value = searchHintKeyWord
            search_fragment_visibility.value = true
        }else{
            //搜索关键词为空，提示用户，不进行搜索
            TipsToast.showTips("搜索内容不能为空哦~")
            search_fragment_visibility.value = false
        }
    }

    //取消搜索按钮点击事件
    fun onCancelSearch() {
        LogUtils.d(this, "onCancelSearch--")
        cancelSearch.value = true
    }

    // 从数据库中获取所有搜索历史记录
    private fun getAllSearchHistory() {
        launchUI(errorCallback = { _, _ ->
            searchHistoriesList.value = mutableListOf()
        }, requestCall = {
            searchHistoriesList.value = SearchHistoryRepository.getSearchHistoryItems().toMutableList()
        })
    }

    //根据id删除数据库中的历史记录
    fun deleteSearchHistory(id: Int) {
        launchUI(errorCallback = { _, _ ->

        }, requestCall = {
            SearchHistoryRepository.deleteSearchHistoryItem(id)
            searchHistoriesList.value = SearchHistoryRepository.getSearchHistoryItems().toMutableList()//触发更新
        })
    }

    //添加历史记录
    private fun insertSearchHistory(searchHistoryItem: SearchHistoryItem) {
        launchUI(errorCallback = { _, _ ->
            searchHistoriesList.value = mutableListOf()
        }, requestCall = {
            SearchHistoryRepository.insertSearchHistoryItem(searchHistoryItem.query,searchHistoryItem.timestamp)
            searchHistoriesList.value = SearchHistoryRepository.getSearchHistoryItems().toMutableList()//触发更新
        })
    }

    // 清空所有历史记录
    fun deleteAllSearchHistory() {
        launchUI(errorCallback = { _, _ ->
            searchHistoriesList.value = mutableListOf()
        }, requestCall = {
            SearchHistoryRepository.deleteAllSearchHistory()
            searchHistoriesList.value = SearchHistoryRepository.getSearchHistoryItems().toMutableList()//触发更新
        })
    }


    /**
     * 获取搜索结果列表数据
     * @param k String 搜索关键词
     * @return LiveData<SearchResultDataBean?> 结果数据
     */
    fun getSearchResultData(k: String): LiveData<ArticleDataBean?> {
        LogUtils.d(this, "getSearchResultData-->k-->$k")
        //不能对要装载的值进行空判断,否则会导致只搜索第一次设置进来的值
        if (search_fragment_visibility.value == true) {//当搜索结果界面可见才进行搜索
            //将搜索的关键词存入历史搜索记录集合中
            //去除重复词
            //将历史记录插入到末尾
            insertSearchHistory(
                SearchHistoryItem(
                    query = k,
                    timestamp = System.currentTimeMillis()
                )
            )
            //将当前搜索页面置为第一页
            mCurrentPage = mDefaultPage
            LogUtils.d(this, "searchHistoriesList.value-->${searchHistoriesList.value}")
            launchUI(
                errorCallback = { _, errorMsg ->
                    TipsToast.showTips(errorMsg)
                    LogUtils.d(this@SearchShareViewModel, "errorCallback-->$errorMsg")
                    changeStateView(ViewStateEnum.VIEW_NET_ERROR)
                    //将搜索结果去除

                    searchResultListData.value = null
                },
                requestCall = {
                    LogUtils.d(this@SearchShareViewModel, "requestCall")
                    searchResultListData.value =
                        SearchRepository.getSearchResultData(mDefaultPage, k)//默认搜索第一页数据
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
            errorCallback = { errorCode, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@SearchShareViewModel, "errorCallback-->$errorMsg")
                //标记当前无网
                if(errorCode == 1002 || errorCode == 1007){
                    isNoNetWork = true
                }
                searchHotKeyData.value = null
            },
            requestCall = {
                searchHotKeyData.value = SearchRepository.getSearchHotKeyData()
            }
        )
        return searchHotKeyData
    }


    override fun onReload() {
        if(search_fragment_visibility.value == true){
            //搜索结果界面可见
            getSearchResultData(mCurrentSearchKeyWord.value!!)
        }else{
            //搜索界面可见
            getSearchHotkeyData()
        }
    }

    /**
     * 加载更多搜索结果数据
     */
    fun loadMoreSearchResultData(): LiveData<ArticleDataBean?> {
        //当前页码数+1
        mCurrentPage++
        launchUI(
            errorCallback = { _, errorMsg ->
                TipsToast.showTips(errorMsg)
                LogUtils.d(this@SearchShareViewModel, "errorCallback-->$errorMsg")
                loadMoreSearchResultListData.value = null
                //还原页码数
                mCurrentPage--
            },
            requestCall = {
                LogUtils.d(this@SearchShareViewModel, "requestCall")
                loadMoreSearchResultListData.value =
                    SearchRepository.getSearchResultData(
                        mCurrentPage,
                        mCurrentSearchKeyWord.value!!
                    )
            }
        )
        return loadMoreSearchResultListData
    }
}