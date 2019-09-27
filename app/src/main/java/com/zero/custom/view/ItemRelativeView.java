package com.zero.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zero.custom.R;

public class ItemRelativeView extends RelativeLayout {

    private float itemHeight; //定义的属性：itemHeight 值（控件高度）
    private int leftDrawableId; //左边图片的id
    private String leftTextContent; //左边文字
    private String rightTextContent; //右边文字
    private TextView tvRight;

    public ItemRelativeView(Context context) {
        this(context, null);
    }

    public ItemRelativeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemRelativeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs, defStyleAttr);
        inflateView(context);
    }

    /**
     * 获取自定义属性
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 默认属性
     */
    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemRelativeView);
        //TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemRelativeView, defStyleAttr, 0);
        itemHeight = typedArray.getDimension(R.styleable.ItemRelativeView_itemHeight, dip2px(context, 48));
        leftDrawableId = typedArray.getResourceId(R.styleable.ItemRelativeView_leftDrawable, View.NO_ID);
        leftTextContent = typedArray.getString(R.styleable.ItemRelativeView_leftText);
        rightTextContent = typedArray.getString(R.styleable.ItemRelativeView_rightText);
        typedArray.recycle();//typedArray用完之后需要回收，防止内存泄漏
    }


    /**
     * @param context 自定义布局传递一个上下文
     */
    private void inflateView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_relative, this, true);
        RelativeLayout layoutView = view.findViewById(R.id.layoutView);
        ImageView ivInfo = view.findViewById(R.id.ivInfo);
        TextView tvInfo = view.findViewById(R.id.tvInfo);
        tvRight = view.findViewById(R.id.tvRight);
        //给自定义布局设置属性传递的数据
        ViewGroup.LayoutParams params = layoutView.getLayoutParams();
        params.height = (int) itemHeight;
        layoutView.setLayoutParams(params);
        if (leftDrawableId > 0) {
            ivInfo.setImageResource(leftDrawableId);
        }
        if (TextUtils.isEmpty(leftTextContent)) {
            tvInfo.setVisibility(INVISIBLE);
        } else {
            tvInfo.setVisibility(VISIBLE);
            tvInfo.setText(leftTextContent);
        }
        if (TextUtils.isEmpty(rightTextContent)) {
            tvRight.setVisibility(INVISIBLE);
        } else {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightTextContent);
        }
        this.setEnabled(true);
        this.setClickable(true);
    }

    /**
     * @param visible 设置右边文字显示和隐藏。0 显示, 其他隐藏
     */
    public void setRightTextVisible(int visible) {
        if (visible == VISIBLE) {
            tvRight.setVisibility(VISIBLE);
        } else {
            tvRight.setVisibility(INVISIBLE);
        }
    }

    /**
     * @param content 设置右边文字显示
     */
    public void setRightTextContent(String content) {
        if (tvRight != null) {
            tvRight.setText(content);
        }
    }

    /**
     * @param resource 设置右侧背景
     */
    public void setRightDrawable(int resource) {
        if (tvRight != null && resource > 0) {
            tvRight.setText("");
            tvRight.setBackgroundResource(resource);
        }
    }


    //dp 转 px
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    //px 转 dp
    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
