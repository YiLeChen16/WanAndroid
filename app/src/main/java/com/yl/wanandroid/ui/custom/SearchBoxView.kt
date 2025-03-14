package com.yl.wanandroid.ui.custom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.ui.activity.search.SearchActivity
import com.yl.wanandroid.utils.LogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @description: 自定义搜索控件 实现搜索热词提示，点击搜索按钮直接搜索提示的热词，点击搜索框跳转到搜索界面
 * @author YL Chen
 * @date 2024/10/21 13:48
 * @version 1.0
 */
class SearchBoxView : LinearLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defy: Int) : super(context, attrs, defy) {
        initView(context, attrs)
    }

    private lateinit var mCurrentSearchHotKey: String
    private  var job: Job? = null

    //搜索热词数据
    private var mDatas: List<SearchHotKeyDataBean> = mutableListOf()

    private  val mDefaultMainColor: Int = resources.getColor(R.color.md_theme_primary,context.theme)
    private var mDefaultDuration: Int = 10000

    private var mDuration: Int = mDefaultDuration
    private var mMainColor: Int = mDefaultMainColor

    private lateinit var searchButton: TextView
    private lateinit var editSearchBox: EditText

    //创建协程作用域
    private val scope = CoroutineScope(Job() + Dispatchers.Main)


    private fun initView(context: Context, attrs: AttributeSet?) {
        //获取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.SearchBoxView)
        mMainColor = a.getColor(R.styleable.SearchBoxView_main_color, mDefaultMainColor)
        mDuration = a.getInteger(R.styleable.SearchBoxView_change_duration, mDefaultDuration)
        a.recycle()
        //载入布局
        LayoutInflater.from(context).inflate(R.layout.custom_search_box_view, this, true)
        //获取布局控件
        editSearchBox = this.findViewById(R.id.ed_search_box)
        searchButton = this.findViewById(R.id.btn_search)

        //设置监听事件
        //此按钮被点击: 跳转到SearchActivity界面,展示SearchFragment界面 + 将携带的数据设置为hint
        editSearchBox.setOnClickListener{
            //将目前搜索框显示的数据也一并传递给搜索界面
            LogUtils.d(this@SearchBoxView,"editSearchBox.setOnClickListener")
            val intent = Intent(context, SearchActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(Constant.IS_SEARCH,false)//表示从搜索框跳转过去
            bundle.putString(Constant.CURRENT_SEARCH_HOTKEY,mCurrentSearchHotKey)//当前推荐搜索关键词的order
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        //此按钮被点击: 跳转到SearchActivity界面,展示SearchResultFragment界面 + 将携带的数据设置为hint + 搜索携带的数据
        searchButton.setOnClickListener{
            LogUtils.d(this@SearchBoxView,"searchButton.setOnClickListener")
            val intent = Intent(context, SearchActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(Constant.IS_SEARCH,true)//表示从搜索按钮跳转过去
            bundle.putString(Constant.CURRENT_SEARCH_HOTKEY,mCurrentSearchHotKey)//当前推荐搜索关键词
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    //暴露方法给外界设置搜索热词数据
    fun setData(datas:List<SearchHotKeyDataBean>){
        this.mDatas = datas
        //开始热词轮播
        startLoopHotkey()
    }

    //开始轮播热词
    private fun startLoopHotkey() {
        job = scope.launch {
            while (true) {
                mDatas.forEach {
                    editSearchBox.hint = it.name
                    //记录当前轮播的热词
                    mCurrentSearchHotKey = it.name
                    delay(mDuration.toLong())
                }
            }
        }
    }

}