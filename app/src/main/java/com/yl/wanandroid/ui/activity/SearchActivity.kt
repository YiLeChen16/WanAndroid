package com.yl.wanandroid.ui.activity

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import com.yl.wanandroid.BR
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMActivity
import com.yl.wanandroid.databinding.ActivitySearchBinding
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.ui.adapter.SearchResultListAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel
import com.yl.wanandroid.viewmodel.search.SearchShareViewModel.search_fragment_visibility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @description: 搜索界面Activity
 * @author YL Chen
 * @date 2024/10/21 16:50
 * @version 1.0
 */
@AndroidEntryPoint
class SearchActivity :
    BaseVMActivity<ActivitySearchBinding, SearchShareViewModel>(R.layout.activity_search) {

    private var defaultHintKeyword: String? = null
    private var isSearch: Boolean? = false

    @Inject
    lateinit var searchResultListAdapter: SearchResultListAdapter//搜索结果列表适配器

    override fun initView() {
        super.initView()
        //禁止刷新和加载
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)


    }

    override fun initData() {
        //获取推荐页搜索框搜索按钮跳转传递过来的数据
        val extras = intent.extras
        isSearch = extras?.getBoolean(Constant.isSearch, false)//是否由搜索按钮跳转过来
        defaultHintKeyword =
            extras?.getString(
                Constant.currentSearchHotKey,
                ""
            )//由推荐页跳转携带过来的推荐搜索热词,作为搜索框为空时填充的提示词,若跳转展示的是搜索结果列表fragment则此词同时作为当前搜索词,
        mBinding.edSearchBox.hint = defaultHintKeyword
        super.initData()
    }


    override fun initVMData() {
        mViewModel.search_fragment_visibility.value = isSearch
        mViewModel.mCurrentSearchKeyWord.value = defaultHintKeyword
        mViewModel.searchHintKeyWord = defaultHintKeyword.toString()
        mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)

        //为输入框设置监听事件
        mBinding.edSearchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                mViewModel.editData.value = s.toString()//更新当前搜索关键词
                if (s.toString().isNotEmpty()) {
                    //当前搜索框有输入数据
                    mBinding.btnCancelSearch.visibility = View.VISIBLE//取消搜索按钮 可见
                } else {
                    //当前搜索框没有输入数据
                    mBinding.btnCancelSearch.visibility = View.INVISIBLE//取消搜索按钮 不可见
                }
            }
        })
        //在该Editview获得焦点的时候将键盘的“回车”键改为“搜索”
        mBinding.edSearchBox.imeOptions = EditorInfo.IME_ACTION_SEARCH
        mBinding.edSearchBox.setSingleLine(true)

        mBinding.edSearchBox.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent ->
            if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH) && keyEvent != null) {
                //点击搜索要做的操作
                mViewModel.search_fragment_visibility.value = true
                return@OnEditorActionListener true
            }
            false
        })

    }

    override fun observeLiveData() {
        super.observeLiveData()

        //返回按钮
        mViewModel.isBack.observe(this) {
            //返回上一层
            if (it) {
                mViewModel.isBack.value = false//将值重置
                finish()
            }
        }
        //判断当前可见的fragment为哪一个
        mViewModel.search_fragment_visibility.observe(this) {
            LogUtils.d(this, "it-->$it")
            //显示搜索结果列表fragment
            if (it) {
                //收起键盘
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(window.decorView.windowToken,0)
                //显示搜索结果列表
                showSearchResult()
                //将当前搜索关键词装载到搜索框中
                mBinding.edSearchBox.setText(mViewModel.mCurrentSearchKeyWord.value)
                //移动光标至末尾
                mBinding.edSearchBox.setSelection(mViewModel.mCurrentSearchKeyWord.value?.length!!)
            } else {
                //显示搜索界面fragment
                showSearch()
            }
        }


        //取消搜索按钮
        //TODO::
        mViewModel.cancelSearch.observe(this) {
            if (it) {
                //取消搜索
                //将当前搜索框数据置为空
                mBinding.edSearchBox.text = null
                //显示搜索界面,隐藏搜索结果界面
                search_fragment_visibility.value = false
                mViewModel.cancelSearch.value = false
            }
        }
    }

    private fun showSearch() {
        mBinding.searchFragment.visibility = View.VISIBLE
        mBinding.searchResultFragment.visibility = View.GONE
    }

    private fun showSearchResult() {
        mBinding.searchFragment.visibility = View.GONE
        mBinding.searchResultFragment.visibility = View.VISIBLE
    }

    //重写此方法,获取xml布局中声明的变量id,以为xml布局中声明的变量赋值
    override fun getVariableId(): Int {
        return BR.searchShareViewModel
    }
}