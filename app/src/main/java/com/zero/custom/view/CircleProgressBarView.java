package com.zero.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;
import com.zero.custom.R;

/**
 * 自定义：圆圈view
 * 可以设置弧圆
 */
public class CircleProgressBarView extends View {

    private CircleBarAnimation anim;
    private RectF mRectF;//绘制圆弧的矩形区域
    private Paint bgPaint;//绘制背景圆弧的画笔
    private Paint progressPaint;//绘制圆弧的画笔
    private float maxNum;//进度条最大值
    private float progressNum;//可以更新的进度条数值
    private float progressSweepAngle;//进度条圆弧扫过的角度
    private float barWidth;//圆弧进度条宽度
    private float startAngle;//背景圆弧的起始角度
    private float sweepAngle;//背景圆弧扫过的角度
    private int bgColor;//背景圆弧颜色
    private int defaultSize;//自定义View默认的宽高
    private int progressColor;//进度条圆弧颜色
    private TextView textView; //控件内设置TextView
    private OnAnimationListener onAnimationListener; //动画监听

    public CircleProgressBarView(Context context) {
        this(context, null);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);
        progressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progress_color, Color.GREEN);//进度进行颜色，默认为绿色
        bgColor = typedArray.getColor(R.styleable.CircleProgressBarView_bg_color, Color.GRAY);//进步背景色，默认为灰色
        startAngle = typedArray.getFloat(R.styleable.CircleProgressBarView_start_angle, 0);//圆弧起始角度，默认为0
        sweepAngle = typedArray.getFloat(R.styleable.CircleProgressBarView_sweep_angle, 360);//圆弧扫过的角度，默认为360
        barWidth = typedArray.getDimension(R.styleable.CircleProgressBarView_bar_width, dip2px(context, 10));//圆弧进度条宽度。默认为10dp
        typedArray.recycle();//typedArray用完之后需要回收，防止内存泄漏

        progressNum = 0;
        maxNum = 100;
        defaultSize = dip2px(context, 100);

        mRectF = new RectF(); //矩形

        bgPaint = new Paint(); //圆弧背景
        bgPaint.setStyle(Paint.Style.STROKE); //只描边，不填充
        bgPaint.setColor(bgColor); //颜色
        bgPaint.setAntiAlias(true); //设置抗锯齿
        bgPaint.setStrokeWidth(barWidth); //宽度
        bgPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔为圆角

        progressPaint = new Paint(); //圆弧进度
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(progressColor);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(barWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        anim = new CircleBarAnimation(); //获取动画对象
    }

    /**
     * @param maxNum 设置进度条最大值
     */
    public void setProgressMaxNum(float maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * @param progressNum 设置动画进度 进度值
     * @param time        设置动画进时间 毫秒
     */
    public void setProgressNumber(float progressNum, int time) {
        this.progressNum = progressNum;
        anim.setDuration(time);
        this.startAnimation(anim);
    }


    public void setProgressSweep(float sweepAngle, int time) {
        this.sweepAngle = sweepAngle;
        anim.setDuration(time);
        this.startAnimation(anim);
    }

    // TODO【自定义view 重要步骤 1 】测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultSize, heightMeasureSpec);
        int width = measureSize(defaultSize, widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        if (min >= barWidth * 2) {//这里简单限制了圆弧的最大宽度
            mRectF.set(barWidth / 2, barWidth / 2, min - barWidth / 2, min - barWidth / 2);
        }
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    // TODO【自定义view 重要步骤 2 】定位
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    // TODO【自定义view 重要步骤 3 】绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, bgPaint);
        canvas.drawArc(mRectF, startAngle, progressSweepAngle, false, progressPaint);
    }


    /**
     * @param textView 设置显示文字的TextView
     */
    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener) {
        this.onAnimationListener = onAnimationListener;
    }

    /**
     * 进度改变动画
     */
    private class CircleBarAnimation extends Animation {

        CircleBarAnimation() {
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            //调用动画更新函数applyTransformation(interpolatedTime, transformation)计算出动画数据
            progressSweepAngle = interpolatedTime * sweepAngle * progressNum / maxNum;//这里计算进度条的比例
            if (onAnimationListener != null) {
                if (textView != null) {
                    textView.setText(onAnimationListener.howToChangeText(interpolatedTime, progressNum, maxNum));
                }
                onAnimationListener.howTiChangeProgressColor(progressPaint, interpolatedTime, progressNum, maxNum);
            }
            postInvalidate();
        }
    }


    /**
     * 监听动画
     */
    public interface OnAnimationListener {
        /**
         * 如何处理要显示的文字内容
         *
         * @param interpolatedTime 从0渐变成1,到1时结束动画
         * @param updateNum        进度条数值
         * @param maxNum           进度条最大值
         * @return
         */
        String howToChangeText(float interpolatedTime, float updateNum, float maxNum);

        /**
         * 如何处理进度条的颜色
         *
         * @param paint            进度条画笔
         * @param interpolatedTime 从0渐变成1,到1时结束动画
         * @param updateNum        进度条数值
         * @param maxNum           进度条最大值
         */
        void howTiChangeProgressColor(Paint paint, float interpolatedTime, float updateNum, float maxNum);
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
