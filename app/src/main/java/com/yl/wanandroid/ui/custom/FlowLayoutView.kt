package com.yl.wanandroid.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.yl.wanandroid.R
import com.yl.wanandroid.room.entity.SearchHistoryItem
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.SizeUtils

/**
 * @description: 搜索历史记录自定义View
 * @author YL Chen
 * @date 2024/11/5 16:41
 * @version 1.0
 */
class FlowLayoutView(
    val mContext: Context,
    val attrs: AttributeSet?,
    defStyleAttr: Int
) : ViewGroup(mContext, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    private var isDeleteOk: Boolean = true
    private lateinit var mSearchHistoriesHead: View
    private var mItemHeight: Int = 0
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private lateinit var mIsFold: ImageView
    private lateinit var mDelete: ImageView
    private lateinit var mDetailDelete: LinearLayout
    private lateinit var mDeleteOk: TextView
    private lateinit var mAllDelete: TextView


    private val DEFAULT_LINE_SPAC = SizeUtils.dip2px(mContext, 10f)
    private val DEFAULT_COLUMN_SPAC = SizeUtils.dip2px(mContext, 10f)
    private val DEFAULT_MAX_LINES = 3//默认显示的最大行数
    private val DEFAULT_REAL_MAX_LINES: Int = 8//实际展开后的最大行数

    private var mLineSpc: Int = DEFAULT_LINE_SPAC//行间距
    private var mColumnSpc: Int = DEFAULT_COLUMN_SPAC//列间距
    private var mMaxLines: Int = DEFAULT_MAX_LINES//显示的最大行数
    private var mRealMaxLines: Int = DEFAULT_REAL_MAX_LINES//实际展开的最大行数


    //历史搜索数据
    private var mSearchHistoriesData = mutableListOf<SearchHistoryItem>()

    private var listener: OnItemClickListener? = null

    //创建集合用于存储每一行的子View集合
    private var lines: MutableList<MutableList<View>> = mutableListOf()

    private var isExpanded = false//布局是否全部展开

    init {
        initView()
    }

    //初始化界面
    fun initView() {
        //获取自定义属性
        val a =
            mContext.obtainStyledAttributes(attrs, R.styleable.FlowLayoutView)
        mLineSpc = a.getInt(R.styleable.FlowLayoutView_lineSpac, DEFAULT_LINE_SPAC)
        mColumnSpc = a.getInt(R.styleable.FlowLayoutView_lineSpac, DEFAULT_COLUMN_SPAC)
        mMaxLines = a.getInt(R.styleable.FlowLayoutView_maxLines, DEFAULT_MAX_LINES)
        mRealMaxLines = a.getInt(R.styleable.FlowLayoutView_realMaxLines, DEFAULT_REAL_MAX_LINES)
        a.recycle()
        //载入布局
        LayoutInflater.from(mContext).inflate(R.layout.custom_flow_layout_view, this, true)
        //获取布局控件
        mIsFold = findViewById(R.id.is_fold)//折叠按钮
        mDelete = findViewById(R.id.delete)//删除按钮
        mDetailDelete = findViewById(R.id.detail_delete)//详细删除操作布局
        mAllDelete = findViewById(R.id.all_delete)//全部删除按钮
        mDeleteOk = findViewById(R.id.delete_ok)//完成删除按钮
        mIsFold.visibility = VISIBLE
        mIsFold.setOnClickListener {
            //点击切换展开状态
            toggle()
        }
        mDelete.setOnClickListener {
            //展示详细删除操作,并隐藏删除图标,展示每个条目后面的删除按钮并显示所有条目
            showAllDeleteButton()
            //设置删除记录变量
            isDeleteOk = false
        }
        mDeleteOk.setOnClickListener{
            //隐藏详细删除操作,并显示删除图标,隐藏每个条目后面的删除按钮并显示所有条目
            hideAllDeleteButton()
            isDeleteOk = true
        }
        mAllDelete.setOnClickListener{
            //将历史记录全部删除
            deleteAllHistories()

            isDeleteOk = true
        }
    }

    private fun deleteAllHistories() {
        //移除全部历史记录
        for (line in lines) {
            for (view in line) {
                removeView(view)
            }
        }
        //将数据从本地删除
        mSearchHistoriesData.clear()
        //通知对应ViewModel删除数据
        listener?.onAllHistoriesDelete()
        //请求重新测量和布局
        requestLayout()
    }

    private fun hideAllDeleteButton() {
        isExpanded = false
        //将删除图标隐藏,显示详细删除操作
        mDelete.isVisible = true
        mDetailDelete.isVisible = false
        //遍历lines中的所有子View将删除按钮修改为可见状态
        for (line in lines) {
            for (itemView in line) {
                val deleteItem = itemView.findViewById<ImageView>(R.id.delete_item)
                deleteItem.visibility = GONE
            }
        }
        //重新测量和布局
        requestLayout()
    }

    private fun showAllDeleteButton() {
        isExpanded = true
        //将删除图标隐藏显示详细删除操作
        mDelete.isVisible = false
        mDetailDelete.isVisible = true
        //遍历lines中的所有子View将删除按钮修改为可见状态
        for (line in lines) {
            for (itemView in line) {
                val deleteItem = itemView.findViewById<ImageView>(R.id.delete_item)
                deleteItem.visibility = VISIBLE
            }
        }
        //重新测量和布局
        requestLayout()
    }

    //暴露方法给外界设置数据
    fun setData(data: List<SearchHistoryItem>) {
        LogUtils.d(this,"data-->$data")
        if(data.isNotEmpty()){
            visibility = VISIBLE
        }else{
            //表示当前没有历史搜索记录
            visibility = GONE
        }
        mSearchHistoriesData.clear()
        for (line in lines) {
            for (view in line) {
                removeView(view)
            }
        }
        mSearchHistoriesData.addAll(data)
        //根据设置进来的数据数目动态创建子View
        createChildView()
    }

    /**
     * 创建历史数据子View并添加到界面中
     */
    private fun createChildView() {
        for ((index) in mSearchHistoriesData.withIndex()) {
            val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_flow_layout_view, this, false)//载入布局
            val keyword = itemView.findViewById<TextView>(R.id.keyword)
            val deleteItem = itemView.findViewById<ImageView>(R.id.delete_item)
            keyword.text = mSearchHistoriesData[index].query

            //给每个ItemView设置监听事件
            keyword.setOnClickListener {
                listener?.onKeyWordClick(keyword.text.toString())
            }
            deleteItem.setOnClickListener {
                removeView(itemView)//移除当前itemView
                //通知监听者删除ViewModel中的数据源
                listener?.onDeleteButtonClick(mSearchHistoriesData[index].id)//将要删除的数据传回
                //从本地数据源中删除
                mSearchHistoriesData.removeAt(index)
                // 通知布局刷新
                // 如果数据已经发生变化，调用 requestLayout() 来重绘布局
                requestLayout()
            }
            addView(itemView)//添加到布局中
        }
    }

    /**
     * 测量 设置自身的大小以及设置子View的大小
     * 子view共分为两个部分:头部和存放区
     * @param widthMeasureSpec Int
     * @param heightMeasureSpec Int
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount == 0) {
            return
        }
        LogUtils.d(this, "childCount==>$childCount")
        lines.clear()
        //创建集合用于存储一行要装载的子View
        var line = mutableListOf<View>()
        //测量当前父布局的大小
        mWidth = MeasureSpec.getSize(widthMeasureSpec)

        LogUtils.d(this, "mWidth==$mWidth")
        //测量子View
        mSearchHistoriesHead = getChildAt(0)
        measureChild(mSearchHistoriesHead, widthMeasureSpec, heightMeasureSpec)
        //测量存放区子View的大小并计算共占多少行
        for (i in 1 until childCount) {//从1开始
            //获取子View视图
            val itemView = getChildAt(i)
            if(!isDeleteOk){
                //还未完成删除
                //继续展示每个条目后面的删除图标
                //防止想同时删除多个时,出现删除一个后,其他记录后的删除图标因为删除操作导致界面重绘默认又变回不可见状态
                val deleteItemButton = itemView.findViewById<ImageView>(R.id.delete_item)
                deleteItemButton.visibility = VISIBLE
            }
            //测量子View
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec)
            //将子View添加到集合中
            if (!isCanAdd(itemView, line) || line.isEmpty()) {
                //创建新的一行
                line = mutableListOf()
                line.add(itemView)
                //将此行添加到总的行集合中
                lines.add(line)
            } else {
                //直接添加
                line.add(itemView)
            }
        }
        LogUtils.d(this, "lines.size==>${lines.size}")

        //获取存放区每个item的高度
        mItemHeight = getChildAt(0).measuredHeight
        LogUtils.d(this, "每个子View的高度==>${mItemHeight}")

        //父布局总高度
        //若大于3行才需考虑展开状态问题
        val mSearchHistoriesLayoutHeight = if (lines.size > 3) {
            //将展开按钮设置为可点击状态
            mIsFold.isClickable = true
            mIsFold.isSelected = false
            if (isExpanded) {
                // 如果是展开状态，则测量存放区所有子视图的高度
                //计算存放区总高度
                ((lines.size * mItemHeight) + mLineSpc * (lines.size.plus(1)))
            } else {
                // 如果是收起状态，则只测量存放区前三行搜索记录的高度
                ((3 * mItemHeight) + mLineSpc * 3)
            }
        } else {
            //将展开按钮设置为不可点击状态
            mIsFold.isClickable = false
            //计算存放区总高度
            ((lines.size * mItemHeight) + mLineSpc * (lines.size.plus(1)))
        }
        mHeight = mSearchHistoriesLayoutHeight + mSearchHistoriesHead.measuredHeight
        LogUtils.d(this, "父布局总高度==$mHeight")
        //测量父布局自身的大小
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun isCanAdd(itemView: View?, line: MutableList<View>): Boolean {
        var totalWidth = 0
        for (view in line) {
            totalWidth += view.measuredWidth
        }
        totalWidth =
            (totalWidth + itemView?.measuredWidth!! + mColumnSpc * (line.size + 1))
        return totalWidth <= mWidth
    }

    /**
     * 控制展开状态
     */
    private fun toggle() {
        isExpanded = !isExpanded
        mIsFold.isSelected = isExpanded
        requestLayout()
    }

    /**
     * 布局 摆放子View
     * @param changed Boolean
     * @param left Int
     * @param top Int
     * @param right Int
     * @param bottom Int
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mSearchHistoriesHead.layout(
            left,
            top,
            left + mSearchHistoriesHead.measuredWidth,
            top + mSearchHistoriesHead.measuredHeight
        )
        //摆放存放区子View
        var t =  mSearchHistoriesHead.measuredHeight
        //遍历lines集合,将其中每行的子View摆放在存放区中
        if (isExpanded) {
            //展开状态,展示全部
            //从后往前遍历
            for (line in lines.reversed()) {
                var l = mColumnSpc
                for (itemView in line.reversed()) {
                    itemView.layout(
                        l,
                        t,
                        itemView.measuredWidth + l,
                        itemView.measuredHeight + t
                    )
                    //更新left
                    l += itemView.measuredWidth + mColumnSpc
                }
                //更新top
                t += mItemHeight + mLineSpc
            }
        } else {
            //只展示后三行
            val linesToDisplay = if (lines.size > 3) {
                lines.takeLast(3) // 如果集合大于 3 行，则只取最后 3 行
            } else {
                lines // 如果集合小于等于 3 行，则展示全部
            }
            for (line in linesToDisplay.reversed()) {
                var l = mColumnSpc
                for (itemView in line.reversed()) {
                    itemView.layout(
                        l,
                        t,
                        itemView.measuredWidth + l,
                        itemView.measuredHeight + t
                    )
                    //更新left
                    l += itemView.measuredWidth + mColumnSpc
                }
                //更新top
                t += mItemHeight + mLineSpc
            }
        }

    }


    //暴露方法给外界设置接口
    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onKeyWordClick(k: String)
        fun onDeleteButtonClick(id: Int)//删除按钮点击事件
        fun onAllHistoriesDelete()//删除全部历史记录
    }
}