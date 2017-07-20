package com.hskj.damnicomniplusvic.ctsvideo.customview;

/**
 * Created by Administrator on 2016/8/27.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.hskj.damnicomniplusvic.ctsvideo.R;

/**
 * Created by damnicomniplus on 2016/4/8.
 */
 
 /**
 add the code to your style.xml
 *<declare-styleable name="CircleView">
        <attr name="maxProgressWidth" format="dimension"/>
        <attr name="maxProgressColor" format="color"/>
        <attr name="firstProgressWidth" format="dimension"/>
        <attr name="firstProgressColor" format="color"/>
        <attr name="secondProgressWidth" format="dimension"/>
        <attr name="secondProgressColor" format="color"/>
        <attr name="dotDiameter" format="dimension"/>
        <attr name="dotColor" format="color"/>
        <attr name="firstProgress" format="integer"/>
        <attr name="secondProgress" format="integer"/>
        <attr name="canDisplayDot" format="boolean"/>
    </declare-styleable>
 *
 */
public class CircleView extends View {
    private final int maxProgress = 99;
    private int firstProgress;
    private int secondProgress;
    private Paint maxProgressPaint;
    private Paint firstProgressPaint;
    private Paint secondProgressPaint;
    private Paint dotPaint;
    private int maxProgressColor;
    private int firstProgressColor;
    private int secondProgressColor;
    private int dotColor;
    private int width;
    private int height;
    private float maxProgressWidth;
    private float firstProgressWidth;
    private float secondProgressWidth;
    /**小圆点的 直径*/
    private float dotDiameter;

    private RectF maxRectF;
    private RectF firstRectF;
    private RectF secondRectF;

    private static final int whatFirstProgress = 100;
    private static final int whatSecondProgress = 100;
    private ProgressHandler progressHandler;

    /**是否展示 小圆点*/
    private boolean canDisplayDot;
    public CircleView(Context context) {
        super(context,null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.AlertDialog);
        canDisplayDot = typedArray.getBoolean(R.styleable.CircleView_canDisplayDot,true);
        firstProgress=typedArray.getInteger(R.styleable.CircleView_firstProgress,0);
        secondProgress=typedArray.getInteger(R.styleable.CircleView_secondProgress,0);

        progressHandler = new ProgressHandler();
        maxProgressWidth = (int) dp2Px(typedArray.getDimension(R.styleable.CircleView_maxProgressWidth,5));
        maxProgressColor = typedArray.getColor(R.styleable.CircleView_maxProgressColor,Color.parseColor("#F3F3F3"));
        maxProgressPaint = new Paint();
        maxProgressPaint.setAntiAlias(true);
        maxProgressPaint.setStyle(Paint.Style.STROKE);
        maxProgressPaint.setStrokeWidth(maxProgressWidth);
        maxProgressPaint.setColor(maxProgressColor);

        firstProgressWidth = (int) dp2Px(typedArray.getDimension(R.styleable.CircleView_firstProgressWidth,5));
        firstProgressColor = typedArray.getColor(R.styleable.CircleView_firstProgressColor,Color.parseColor("#FD8957"));
        firstProgressPaint = new Paint();
        firstProgressPaint.setAntiAlias(true);
        firstProgressPaint.setStyle(Paint.Style.STROKE);
        firstProgressPaint.setStrokeWidth(firstProgressWidth);
        firstProgressPaint.setColor(firstProgressColor);

        secondProgressWidth = (int) dp2Px(typedArray.getDimension(R.styleable.CircleView_secondProgressWidth,5));
        secondProgressColor = typedArray.getColor(R.styleable.CircleView_secondProgressColor,Color.parseColor("#6AA84F"));
        secondProgressPaint = new Paint();
        secondProgressPaint.setAntiAlias(true);
        secondProgressPaint.setStyle(Paint.Style.STROKE);
        secondProgressPaint.setStrokeWidth(secondProgressWidth);
        secondProgressPaint.setColor(secondProgressColor);

        dotColor = typedArray.getColor(R.styleable.CircleView_dotColor,Color.parseColor("#FF5722"));
        dotDiameter =  (int) dp2Px(typedArray.getDimension(R.styleable.CircleView_dotDiameter,7));
        dotPaint = new Paint();
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setAntiAlias(true);
        dotPaint.setColor(dotColor);

        firstRectF = new RectF(0, 0, 0, 0);
        secondRectF = new RectF(0, 0, 0, 0);
        maxRectF = new RectF(0, 0, 0, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int padding = getPaddingLeft() + getPaddingRight();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.max(result, size);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int padding = getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.max(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
		/*偏移量*/
        float dMax = (dotDiameter - maxProgressWidth)*0.5F;
        float dFirst = (dotDiameter - firstProgressWidth)*0.5F;
        float dSecond = (dotDiameter - secondProgressWidth)*0.5F;
		/*1.圆心（x,y）坐标值*/
        float centerX = (width - getPaddingLeft() - getPaddingRight()) / 2.0f;
        float centerY = (height - getPaddingTop() - getPaddingBottom()) / 2.0f;
		/*2.圆环半径*/
        float maxRadius = centerY - maxProgressWidth / 2;
        float firstRadius = centerY - firstProgressWidth / 2;
        float secondRadius = centerY - secondProgressWidth / 2;
        if (getWidth() >= getHeight()) {
            maxRadius = centerY - maxProgressWidth / 2;
            firstRadius = centerY - firstProgressWidth / 2;
            secondRadius = centerY - secondProgressWidth / 2;
        } else {
            maxRadius = centerX - maxProgressWidth / 2;
            firstRadius = centerX - firstProgressWidth / 2;
            secondRadius = centerX - secondProgressWidth / 2;
        }

        maxRectF.left =  centerX - maxRadius + dMax;
        maxRectF.right = centerX + maxRadius - dMax;
        maxRectF.top = centerY - maxRadius + dMax;
        maxRectF.bottom = centerY + maxRadius - dMax;

        firstRectF.left =  centerX - firstRadius + dFirst;
        firstRectF.right = centerX + firstRadius - dFirst;
        firstRectF.top = centerY - firstRadius + dFirst;
        firstRectF.bottom = centerY + firstRadius - dFirst;

        secondRectF.left = centerX - secondRadius + dSecond;
        secondRectF.right = centerX + secondRadius - dSecond;
        secondRectF.top = centerY - secondRadius + dSecond;
        secondRectF.bottom = centerY + secondRadius - dSecond;

        canvas.drawArc(maxRectF, 0, 360 , false, maxProgressPaint);

        float firstAngle = (float) 360 * firstProgress / (float) maxProgress;
        float secondAngle = ((float) 360 * secondProgress / (float) maxProgress);
        float dotAngle =  (float) (Math.PI*secondAngle/180.0F);

        canvas.drawArc(firstRectF, 0 - 90, firstAngle, false, firstProgressPaint);
        canvas.drawArc(secondRectF, 0 - 90, secondAngle , false, secondProgressPaint);

        float dotCx = (float) (width*0.5 + (width - dotDiameter)*0.5 * Math.sin(dotAngle));
        float dotCy = (float) (height*0.5 - (height -dotDiameter) *0.5 * Math.cos(dotAngle));
        if(canDisplayDot){
            canvas.drawCircle(dotCx, dotCy, dotDiameter * 0.5F, dotPaint);
        }

    }
    /**
     * 设置 圆点 的直径，单位 dp
     */
    public void setDotDiameter(float dotDiameter) {
        this.dotDiameter = dp2Px(dotDiameter);
        dotPaint.setStrokeWidth(this.dotDiameter);
        invalidate();
    }
    /**
     * 给  maxProgress 设置颜色
     */
    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
        dotPaint.setColor(dotColor);
    }
    /**
     * 给  maxProgress 设置颜色
     */
    public void setMaxProgressColor(int maxProgressColor) {
        this.maxProgressColor = maxProgressColor;
        maxProgressPaint.setColor(maxProgressColor);
    }

    /**
     * 给  firstProgress 设置颜色
     */
    public void setFirstProgressColor(int firstProgressColor) {
        this.firstProgressColor = firstProgressColor;
        firstProgressPaint.setColor(firstProgressColor);
    }

    public int getFirstProgressColor() {
        return firstProgressColor;
    }

    /**
     * 给  firstProgress 设置颜色
     */
    public void setSecondProgressColor(int secondProgressColor) {
        this.secondProgressColor = secondProgressColor;
        secondProgressPaint.setColor(secondProgressColor);
    }

    public int getSecondProgressColor() {
        return secondProgressColor;
    }

    /**
     * 获取 firstProgress  的进度值
     */
    public int getFirstProgress() {
        return firstProgress;
    }

    /**
     * 给  firstProgress 设置进度， [0,100 ]
     */
    public void setFirstProgress(int firstProgress) {
        if (firstProgress > maxProgress) {
            firstProgress = maxProgress;
        } else if (firstProgress < 0) {
            firstProgress = 0;
        }
        if (secondProgress > firstProgress) {
            secondProgress = firstProgress;
        } else if (secondProgress < 0) {
            secondProgress = 0;
        }
        this.firstProgress = firstProgress;
        invalidate();
    }

    /**
     * 给  firstProgress 设置进度， [0,100 ]
     */
    public void setFirstProgress(int firstProgress, long delay) {
        new ProgressThread(firstProgress, whatFirstProgress, delay).start();
    }
    /**
     * 获取 secondProgress  的进度值
     */
    public int getSecondProgress() {
        return secondProgress;
    }

    /**
     * 给  secondProgress 设置进度, [0,100 ]
     */
    public void setSecondProgress(int secondProgress) {
        if (secondProgress > maxProgress) {
            secondProgress = maxProgress;
        } else if (secondProgress < 0) {
            secondProgress = 0;
        }
        if (secondProgress > firstProgress) {
            firstProgress = secondProgress;
        }
        this.secondProgress = secondProgress;
        invalidate();
    }
    /**
     * 给  secondProgress 设置进度, [0,100 ]
     */
    public void setSecondProgress(int secondProgress, long delay) {
        new ProgressThread(secondProgress, whatSecondProgress, delay).start();
    }


    public float getMaxProgressWidth() {
        return maxProgressWidth;
    }

    /**可以展示 小圆点*/
    public void setCanDisplayDot(boolean canDisplayDot){
        this.canDisplayDot = canDisplayDot;
        invalidate();
    }

    /**
     * 设置 Max 的宽度，单位 dp
     * 必须在 setDotDiameter 之后调用
     */
    public void setMaxProgressWidth(float maxProgressWidth) {
        this.maxProgressWidth = dp2Px(maxProgressWidth);
        maxProgressPaint.setStrokeWidth(this.maxProgressWidth);
        invalidate();
    }

    public float getFirstProgressWidth() {
        return firstProgressWidth;
    }

    /**
     * 设置 第一个进度条 的宽度，单位 dp
     * 必须在 setDotDiameter 之后调用
     */
    public void setFirstProgressWidth(float firstProgressWidth) {
        this.firstProgressWidth =  dp2Px(firstProgressWidth);
        if(this.firstProgressWidth > dotDiameter){
            this.firstProgressWidth = dotDiameter;
        }
        firstProgressPaint.setStrokeWidth(this.firstProgressWidth);
        invalidate();
    }

    public float getSecondProgressWidth() {
        return secondProgressWidth;
    }

    /**
     * 设置 第二个进度条 的宽度，单位 dp
     * 必须在 setDotDiameter 之后调用
     */
    public void setSecondProgressWidth(float secondProgressWidth) {
        this.secondProgressWidth = dp2Px(secondProgressWidth);
        if(this.secondProgressWidth > dotDiameter){
            this.secondProgressWidth = dotDiameter;
        }
        secondProgressPaint.setStrokeWidth(this.secondProgressWidth);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    /**
     * 数据转换: dp---->px
     */
    private float dp2Px(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    private final class ProgressThread extends Thread
    {
        private int progress;
        private int what;
        private long delay;
        public ProgressThread(int progress, int what, long delay) {
            this.progress = progress;
            this.what = what;
            this.delay = delay;
        }
        @Override
        public void run()
        {
            for (int i = 0; i < progress; i++){
                Message msg = Message.obtain();
                msg.arg1 = i;
                msg.what = what;
                progressHandler.sendMessage(msg);
                SystemClock.sleep(delay/progress);
            }
        }
    }
    @SuppressLint("HandlerLeak")
    private final class ProgressHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if(whatFirstProgress == msg.what){
                if (firstProgress > maxProgress) {
                    firstProgress = maxProgress;
                } else if (firstProgress < 0) {
                    firstProgress = 0;
                }
                firstProgress = msg.arg1;
                invalidate();
            }else if(whatSecondProgress == msg.what){
                if (secondProgress > maxProgress) {
                    secondProgress = maxProgress;
                } else if (secondProgress < 0) {
                    secondProgress = 0;
                }
                secondProgress = msg.arg1;
                invalidate();
            }
        }
    }

}

