package com.yzdsmart.Dingdingwen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/6/17.
 */
public class CustomRoundProgress extends View {
    //画笔对象的引用
    private Paint mPaint;
    //圆环的颜色
    private int roundColor;
    //圆环进度的颜色
    private int roundProgressColor;
    //中间进度百分比的字符串颜色
    private int textColor;
    //中间进度百分比的字符串的字体大小
    private float textSize;
    //圆环的宽度
    private float roundWidth;
    //最大进度
    private int max;
    //当前进度
    private int progress;
    //是否显示中间的进度
    private boolean textIsDisplayable;
    //进度的风格,实心或者空心
    private int style;
    //进度的风格
    public static final int STROKE = 0;
    public static final int FILL = 1;

    public CustomRoundProgress(Context context) {
        this(context, null);
    }

    public CustomRoundProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundProgress);
        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.CustomRoundProgress_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.CustomRoundProgress_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.CustomRoundProgress_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.CustomRoundProgress_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.CustomRoundProgress_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.CustomRoundProgress_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.CustomRoundProgress_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.CustomRoundProgress_style, 0);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画最外环的大圆环
         */
        int center = getWidth() / 2;//获取圆心的x坐标
        int radius = (int) (center - roundWidth / 2);//圆环的半径
        mPaint.setColor(roundColor);//设置圆环的颜色
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setStrokeWidth(roundWidth);//设置圆环的宽度
        mPaint.setAntiAlias(true);//消除锯齿
        canvas.drawCircle(center, center, radius, mPaint);//画出圆环
        /**
         * 画进度百分比
         */
        mPaint.setStrokeWidth(0);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setTypeface(Typeface.DEFAULT);//设置字体
        int percent = (int) (((float) progress / (float) max) * 100);//中间的进度百分比，先转换成float再进行除法运算，不然都为0
        float textWidth = mPaint.measureText(percent + "%");//测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            canvas.drawText(percent + "%", center - textWidth / 2, center + textSize / 2, mPaint);//画出进度百分比
        }
        /**
         * 画圆弧，画圆环的进度
         */
        //设置进度是实心还是空心
        mPaint.setStrokeWidth(roundWidth);//设置圆环的宽度
        mPaint.setColor(roundProgressColor);//设置进度的颜色
        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);//用于定义圆环的形和大小的界限
        switch (style) {
            case STROKE:
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, 360 * progress / max, false, mPaint);//根据进度画圆弧
                break;
            case FILL:
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0) {
                    canvas.drawArc(oval, -90, 360 * progress / max, true, mPaint);//根据进度画圆弧
                }
                break;
        }
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度，需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
