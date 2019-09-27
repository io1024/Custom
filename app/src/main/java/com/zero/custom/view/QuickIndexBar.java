package com.zero.custom.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义：快速索引
 * 分析：
 * .    假设：控件 高度 均分成 26个 方块，方块的宽度和控件宽度相同。
 * .    继续假设：每个字母 被一个方形的单元格 贴边包裹。单元格在方块内居中
 * .    每个字母的坐标（X轴、Y轴）是其左下角的位置，也就是单元格的左下角位置
 * .    以 A 为例：0 索引
 * .            x轴坐标：float x = measuredWidth * 0.5f - textWidth * 0.5f (控件宽度的一半 - 单元格宽度的一半)
 * .            y轴坐标：float y = cellHeight * 0.5f + textWidth * 0.5f (控件宽度的一半 + 单元格宽度的一半)
 * .    以 B 为例：1 索引
 * .            x轴坐标：float x = measuredWidth * 0.5f - textWidth * 0.5f (控件宽度的一半 - 单元格宽度的一半)
 * .            y轴坐标：float y = cellHeight * 0.5f + textWidth * 0.5f + i * cellHeight (控件宽度的一半 + 单元格宽度的一半 + 前面所有单元格的高度)
 * <p>
 * onTouchEvent 会导致界面重绘，进而引发 onDraw 被执行
 */
public class QuickIndexBar extends View {

    private Paint paint;
    private int currentIndex = -1;//被选中的字母索引
    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    private int measuredHeight; //当前控件的高度
    private int cellWidth; //方块的宽度
    private float cellHeight; //方块的高度（均分控件的高度）

    /**
     * java new
     *
     * @param context
     */
    public QuickIndexBar(Context context) {
        this(context, null);
    }

    /**
     * xml 创建
     *
     * @param context
     * @param attrs
     */
    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * xml 创建,并配置style
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //创建一个画笔
        paint = new Paint();
        //设置抗锯齿
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //设置画笔颜色
        paint.setColor(Color.WHITE);
        //设置加粗
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //设置字体大小
        paint.setTextSize(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制 A-Z
        for (int i = 0; i < LETTERS.length; i++) {
            String letter = LETTERS[i];
            //计算每个字母的坐标位置，通过画笔可以获取字母的宽度：measureText()
            float x = cellWidth * 0.5f - paint.measureText(letter) * 0.5f;
            //获取指定字符串指定区域的宽高信息
            Rect rect = new Rect();
            paint.getTextBounds(letter, 0, letter.length(), rect);
            //获取字母的高度
            int textHeight = rect.height();
            //获取字母的宽度 和 paint.measureText(letter) 结果一样
            //int textWidth = rect.width();
            float y = cellHeight * 0.5f + textHeight * 0.5f + i * cellHeight;

            //【改变选择字母】字母被选中时，重绘字母时，改变字母状态
            paint.setColor(i == currentIndex ? Color.GRAY : Color.WHITE);

            canvas.drawText(letter, x, y, paint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = getMeasuredHeight();
        cellWidth = getMeasuredWidth();
        //将控件高度均分成26个单元格
        cellHeight = measuredHeight * 1.0f / LETTERS.length;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断当前触摸的位置：触摸的Y轴的坐标 除以 方块高度 可以得出 当前所处的索引位置
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getCurrentIndex(event);
                break;

            case MotionEvent.ACTION_MOVE:
                getCurrentIndex(event);
                break;

            case MotionEvent.ACTION_UP:
                currentIndex = -1;
                break;
        }
        //【改变选择字母】该方法是重绘界面，会引发 onDraw 方法被调用
        invalidate();
        return true;//返回true表示触摸事件被消费了
    }

    private void getCurrentIndex(MotionEvent event) {
        //获取当前所处位置的-Y轴-坐标（取值可能会小于0）
        float y = event.getY();
        //得到当前触摸位置是第几个索引位置
        int index = (int) (y / cellHeight);
        //获取当前索引和上次获取索引对比，相同则不处理
        if (index != currentIndex) {
            if (index >= 0 && index < LETTERS.length) {
                currentIndex = index;
                if (updateListener != null) {
                    updateListener.onLetterUpdate(LETTERS[index]);
                }
            }
        }
    }

    private LetterUpdateListener updateListener;

    public void setOnLetterUpdateListener(LetterUpdateListener listener) {
        this.updateListener = listener;
    }

    public interface LetterUpdateListener {
        void onLetterUpdate(String letter);
    }
}
