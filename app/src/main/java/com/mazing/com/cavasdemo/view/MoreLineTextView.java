package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mazing.com.cavasdemo.R;

/**
 * @description: 支持伸缩高度 全部显示\部分显示 的TextView
 * @author: Toma
 * @date: 2018-01-22 11:38
 * @version: 1.0
 */

public class MoreLineTextView extends LinearLayout {

    private Context mContext;

    /**
     * 最大可见行数
     */
    private int maxLine = 7;

    /**
     * 是否展示更多
     */
    private boolean isFirst = true;

    private boolean mIsShowAll = false;

    private TextView mTvContent;

    private TextView mTvHint;

    public MoreLineTextView(Context context) {
        this(context, null);
    }

    public MoreLineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        if (attrs != null) {

        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_moreline, null);
        mTvContent = (TextView) view.findViewById(R.id.more_line_tv);
        mTvHint = (TextView) view.findViewById(R.id.more_line_hint);

        setOrientation(LinearLayout.VERTICAL);

//        if (maxLine > 0) {
//            mTvContent.setMaxLines(maxLine);
//            mTvContent.setEllipsize(TextUtils.TruncateAt.END);
//
//            if (mTvContent.getLineCount() < maxLine) {
//                mTvHint.setVisibility(View.GONE);
//            }
//        }

        mTvContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isFirst){
                    isFirst = false;
//                    checkContentStyle();
                }
            }
        });

        mTvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                checkContentStyle();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvHint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatus(2);
            }
        });
    }

    private void checkContentStyle() {
        if(isMoreLines(mTvContent)){
            setStatus(1);
        }
        else{
            setStatus(0);
        }
    }

    private boolean isMoreLines(TextView textView){
        float allTextPx = textView.getPaint().measureText(textView.getText().toString());
        float showViewPx = (textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight()) * maxLine;
        return allTextPx > showViewPx;
    }

    private void setStatus(int status){
        switch (status){
            case 0:{
                mTvHint.setVisibility(View.GONE);
            }
            break;

            case 1:{
                mTvHint.setVisibility(View.VISIBLE);
                mTvHint.setText("查看全部");
            }
            break;

            case 2:{
                if(mIsShowAll){
                    mTvHint.setText("查看全部");
                    mTvContent.setMaxLines(maxLine);
                    mTvContent.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                }
                else{
                    mTvHint.setText("收起");
                    mTvContent.setSingleLine(false);
                    mTvContent.setEllipsize(null);
                }

                mIsShowAll = !mIsShowAll;
            }
            break;
        }
    }

    public void setText(String str){
        mTvContent.setText("会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换的思考和顾客对结核杆菌会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换的思考和顾客对结核杆菌考生高考的黄金矿工的话司空见惯合适的借口和高考结束对韩国很多事户贷记卡韩国肯定会尽快更会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换的思考和顾客对结核杆菌考生高考的黄金矿工的话司空见惯合适的借口和高考结束对韩国很多事换的思考和顾客对结核杆菌考生高考的黄金矿工的话司空见惯合适的借口和高考结束对韩国很多事的思考和顾客对结核杆菌考生高考的黄金矿工的话司空见惯合适的借口和高考结束对韩国很多事考生高考的黄金矿工的话司空见惯合适的借口和高考结束对韩国很多事" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换的思考和顾客对结核杆菌考生高考的黄金矿工的话司空见惯合适的借口和高考结束对韩国很多事的思考和顾客对结核杆菌考生高考的黄金矿工的话司空会尽快换个角度换个健康的生活轨迹的回答和国际快递很快就干哈干哈空间啊感" +
                "        觉快回家肯定会给客户贷记卡韩国肯定会尽快更换的思考和顾客对结核杆菌考生高考的黄金矿工的话司空见惯合适的借口和高考结束对");

        mTvContent.requestLayout();
    }
}
