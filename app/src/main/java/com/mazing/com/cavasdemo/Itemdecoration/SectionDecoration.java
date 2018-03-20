package com.mazing.com.cavasdemo.Itemdecoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.mazing.com.cavasdemo.R;

import java.util.List;

/**
 * 可以吸顶的 ItemDecoration
 *
 * 记录一下实现思路  首先 我们根据 getItemOffsets 给每个 group 组的第一个view 的顶部增加一个 topGap 用于画吸附view
 * 然后在 onDraw 方法中轮巡 所有的itemview 确定每个组的第一个view 就在它的顶部  topGap 的高度中画出吸附view
 * 最后 onDrawOver 方法中轮训所有的 itemview 确定当我们滚动时，如果是普通情况，那么就在顶部固定一个画一个 topGap 高的view，
 * 如果滚动列表造成当前 group 内最后的一个view 底部的 bottom 高度低于 topGap 高时，画出来的吸附view 的位置就需要进行 textY - topGap 的调整，
 * 直到完全为0从新变成普通情况
 *
 */

public class SectionDecoration extends RecyclerView.ItemDecoration {

    private List<String> dataList;
    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int alignBottom;
    private Paint.FontMetrics fontMetrics;

    public SectionDecoration(List<String> dataList, Context context, DecorationCallback decorationCallback){
        Resources res = context.getResources();
        this.dataList = dataList;
        this.callback = decorationCallback;
        //设置悬浮栏的画笔---paint
        paint = new Paint();
        paint.setColor(res.getColor(R.color.colorPrimary));

        //设置悬浮栏中文本的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dipToPx(context, 14));
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = new Paint.FontMetrics();

        //决定悬浮栏的高度等
        topGap = res.getDimensionPixelSize(R.dimen.sectioned_top);
        //决定文本的显示位置等
        alignBottom = res.getDimensionPixelSize(R.dimen.sectioned_alignBottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pos = parent.getChildAdapterPosition(view);

        String groupId = callback.getGroupId(pos);
        if (groupId.equals("-1")) return;
        //只有是同一组的第一个才显示悬浮栏
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
            if (dataList.get(pos).equals("") ) {
                outRect.top = 0;
            }
        } else {
            outRect.top = 0;
        }
    }

    //代表了onDraw(),可以实现类似绘制背景的效果，内容在上面
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String groupId = callback.getGroupId(position);
            if (groupId.equals("-1")) return;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (textLine.equals("")) {
                float top = view.getTop();
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);
                return;
            }
            else{
                if (position == 0 || isFirstInGroup(position)) {
                    float top = view.getTop() - topGap;
                    float bottom = view.getTop();
                    //绘制悬浮栏
                    c.drawRect(left, top, right, bottom, paint);
                    //绘制文本
                    c.drawText(textLine, left, bottom, textPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    private boolean isLastInGroup(int pos){
        if (pos == 0) {
            return false;
        }
        else {
            String prevGroupId = callback.getGroupId(pos);
            String groupId = callback.getGroupId(pos + 1);

            if (prevGroupId.equals(groupId)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            // 因为是根据 字符串内容的相同与否 来判断是不是同意组的，所以此处的标记id 要是String类型
            // 如果你只是做联系人列表，悬浮框里显示的只是一个字母，则标记id直接用 int 类型就行了
            String prevGroupId = callback.getGroupId(pos - 1);
            String groupId = callback.getGroupId(pos);
            //判断前一个字符串 与 当前字符串 是否相同
            if (prevGroupId.equals(groupId)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public interface DecorationCallback {
        String getGroupId(int position);
        String getGroupFirstLine(int position);
    }

    public static int dipToPx(Context context, int dipValue) {
        DisplayMetrics sDisplayMetrics;
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, sDisplayMetrics);
    }
}
