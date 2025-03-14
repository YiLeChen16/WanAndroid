package com.yl.wanandroid.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginTop
import com.yl.wanandroid.R
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.SizeUtils

/**
 * @description: 通用的自定义流式布局
 * @author YL Chen
 * @date 2025/1/21 15:53
 * @version 1.0
 */
class CommonFlowLayoutView(
    val mContext: Context, val attrs: AttributeSet?, defStyleAttr: Int
) : ViewGroup(mContext, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val DEFAULT_LINE_SPAC = SizeUtils.dip2px(mContext, 10f)
    private val DEFAULT_COLUMN_SPAC = SizeUtils.dip2px(mContext, 10f)

    private var mLineSpc: Int = DEFAULT_LINE_SPAC//行间距
    private var mColumnSpc: Int = DEFAULT_COLUMN_SPAC//列间距

    init {
        initView()
    }

    //初始化界面
    fun initView() {
        //获取自定义属性
        val a = mContext.obtainStyledAttributes(attrs, R.styleable.CommonFlowLayoutView)
        mLineSpc = a.getInt(R.styleable.CommonFlowLayoutView_line_spac, DEFAULT_LINE_SPAC)
        mColumnSpc = a.getInt(R.styleable.CommonFlowLayoutView_column_spac, DEFAULT_COLUMN_SPAC)
        a.recycle()

    }

    private var mItemHeight: Int = 0
    private var mWidth: Int = 0
    private var listener: OnItemClickListener? = null

    //创建集合用于存储每一行的子View集合
    private var lines: MutableList<MutableList<View>> = mutableListOf()

    //数据
    private var mData = mutableListOf<Children>()

    //暴露方法给外界设置数据
    fun setData(data: List<Children>) {
        LogUtils.d(this, "data-->$data")
        mData.clear()
        for (line in lines) {
            for (view in line) {
                removeView(view)
            }
        }
        mData.addAll(data)
        //根据设置进来的数据数目动态创建子View
        createChildView()
    }

    private fun createChildView() {
        for ((index) in mData.withIndex()) {
            val itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_flow_layout_view, this, false)//载入布局
            val keyword = itemView.findViewById<TextView>(R.id.keyword)
            keyword.text = mData[index].name
            //给每个ItemView设置监听事件
            itemView.setOnClickListener {
                //点击item将当前数据传递过去
                listener?.onKeyWordClick(mData[index])
            }
            addView(itemView)//添加到布局中
        }
    }

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
        //测量存放区子View的大小并计算共占多少行
        for (i in 0 until childCount) {//从0开始
            //获取子View视图
            val itemView = getChildAt(i)
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

        //获取每个item的高度
        mItemHeight = getChildAt(0).measuredHeight
        LogUtils.d(this, "每个子View的高度==>${mItemHeight}")

        //父布局总高度
        val mHeight = ((lines.size * mItemHeight) + mLineSpc * (lines.size.plus(1))) + marginTop + marginBottom
        LogUtils.d(this, "父布局总高度==$mHeight")
        //测量父布局自身的大小
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun isCanAdd(itemView: View?, line: MutableList<View>): Boolean {
        var totalWidth = 0
        for (view in line) {
            totalWidth += view.measuredWidth
        }
        totalWidth = (totalWidth + itemView?.measuredWidth!! + mColumnSpc * (line.size + 1))
        return totalWidth <= mWidth
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //摆放存放区子View
        var top = marginTop
        //遍历lines集合,将其中每行的子View摆放
        for (line in lines) {
            var left = if ((marginLeft != 0)) marginLeft else mColumnSpc
            for (itemView in line) {
                itemView.layout(
                    left, top, itemView.measuredWidth + left, itemView.measuredHeight + top
                )
                //更新left
                left += itemView.measuredWidth + mColumnSpc
            }
            //更新top
            top += mItemHeight + mLineSpc
        }
    }

    //暴露方法给外界设置接口
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onKeyWordClick(child: Children)
    }
}