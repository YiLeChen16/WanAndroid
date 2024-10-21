package com.yl.wanandroid.ui.custom

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.model.SearchHotKeyDataBean
import com.yl.wanandroid.ui.activity.SearchActivity
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

    private var mCurrentSearchHotKeyOrder: Int = 1
    private  var job: Job? = null

    //搜索热词数据
    private var mDatas: List<SearchHotKeyDataBean> = mutableListOf()

    private var DefaultMainColor: Int = resources.getColor(R.color.md_theme_primary)
    private var DefaultDuration: Int = 10000

    private var mDuration: Int = DefaultDuration
    private var mMainColor: Int = DefaultMainColor

    private lateinit var searchButton: TextView
    private lateinit var editSearchBox: EditText

    //创建协程作用域
    val scope = CoroutineScope(Job() + Dispatchers.Main)


    private fun initView(context: Context, attrs: AttributeSet?) {
        //获取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.SearchBoxView)
        mMainColor = a.getColor(R.styleable.SearchBoxView_main_color, DefaultMainColor)
        mDuration = a.getInteger(R.styleable.SearchBoxView_change_duration, DefaultDuration)
        a.recycle()
        //载入布局
        LayoutInflater.from(context).inflate(R.layout.custom_search_box_view, this, true)
        //获取布局控件
        editSearchBox = this.findViewById(R.id.ed_search_box)
        searchButton = this.findViewById(R.id.btn_search)

        //设置监听事件
        editSearchBox.setOnClickListener{
            //TODO:跳转到搜索界面
            //将目前搜索框显示的数据也一并传递给搜索界面
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(Constant.currentSearchHotKeyOrder,mCurrentSearchHotKeyOrder)
            context.startActivity(intent)
        }

        searchButton.setOnClickListener{
            //TODO:搜索搜索框中的热词并跳转到搜索列表

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
                    //记录当前轮播的热词id
                    mCurrentSearchHotKeyOrder = it.order
                    delay(mDuration.toLong())
                }
            }
        }
    }

}