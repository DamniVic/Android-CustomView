package com.hskj.damnicomniplusvic.wevenation.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hskj.damnicomniplusvic.wevenation.R;
import com.hskj.damnicomniplusvic.wevenation.util.LogUtils;
import com.hskj.damnicomniplusvic.wevenation.util.Utils;


/**
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="ExpandableView">

        <attr name="titleText" format="string" />
        <attr name="titleTextSize" format="dimension" />
        <attr name="titleTextColor" format="color" />
        <attr name="titleTextPadding" format="dimension" />
        <attr name="titleGroupIndicator" format="reference" />
        <attr name="titleDrawableLeft" format="reference" />
        <attr name="titleDrawableTop" format="reference" />
        <attr name="titleDrawableRight" format="reference" />
        <attr name="titleDrawableBottom" format="reference" />
        <attr name="titleDrawablePadding" format="dimension" />

    </declare-styleable>
</resources>
*/

/**
 * Created by CJJ on 2017/08/04 16:47.
 * Copyright © 2015-2017 HSKJ. All rights reserved.
 */

public class ExpandableView extends LinearLayout {

    private LinearLayout llGroupHolder;
    private LinearLayout llChildHolder;
    private TextView tvTitle;
    private ImageView ivGroupIndicator;

    private boolean isExpandable = true;
    private int measuredHeight;
    private View lineView;
    private int titleTextColor;
    private float titleTextSize;
    private String titleText;
    private int drawablePadding;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableRight;
    private Drawable drawableBottom;
    private Drawable groupIndicatorDrawable;

    public ExpandableView(Context context) {
        this(context, null);
    }

    public ExpandableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public ExpandableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableView);
            titleTextColor = typedArray.getColor(R.styleable.ExpandableView_titleTextColor, Utils.getColor(R.color.sup2));
            titleTextSize = typedArray.getDimension(R.styleable.ExpandableView_titleTextSize, Utils.dip2px(14));
            titleText = typedArray.getString(R.styleable.ExpandableView_titleText);
            drawableLeft = typedArray.getDrawable(R.styleable.ExpandableView_titleDrawableLeft);
            drawableTop = typedArray.getDrawable(R.styleable.ExpandableView_titleDrawableTop);
            drawableRight = typedArray.getDrawable(R.styleable.ExpandableView_titleDrawableRight);
            drawableBottom = typedArray.getDrawable(R.styleable.ExpandableView_titleDrawableBottom);
            groupIndicatorDrawable = typedArray.getDrawable(R.styleable.ExpandableView_titleGroupIndicator);
            drawablePadding = (int) (typedArray.getDimension(R.styleable.ExpandableView_titleDrawablePadding, Utils.dip2px(6)) + 0.5f);
            typedArray.recycle();
        }

        init(context);
    }

    private void init(Context context) {
        this.setOrientation(LinearLayout.VERTICAL);
        llGroupHolder = new LinearLayout(context);
        LayoutParams groupHolderLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(44F));
        llGroupHolder.setLayoutParams(groupHolderLayoutParams);

        llChildHolder = new LinearLayout(context);
        LayoutParams childHolderLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        llChildHolder.setLayoutParams(childHolderLayoutParams);

        lineView = new View(context);
        lineView.setBackgroundColor(Utils.getColor(R.color.darkgray));
        LayoutParams lineViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lineView.setLayoutParams(lineViewLayoutParams);


        LayoutParams titleLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tvTitle = new TextView(context);
        tvTitle.setLayoutParams(titleLayoutParams);
        tvTitle.setText(titleText);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        tvTitle.setPadding(Utils.dip2px(20), 0, 0, 0);
        setTitleCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        setTitleCompoundDrawablePadding(drawablePadding);

        LayoutParams groupIndicatorLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        groupIndicatorLayoutParams.rightMargin = Utils.dip2px(20);
        ivGroupIndicator = new ImageView(context);
        llGroupHolder.setOrientation(LinearLayout.HORIZONTAL);

        if (groupIndicatorDrawable == null) {
            groupIndicatorDrawable = Utils.getAppDrawable(R.mipmap.jiantou);
        }
        ivGroupIndicator.setImageDrawable(groupIndicatorDrawable);
        ivGroupIndicator.setLayoutParams(groupIndicatorLayoutParams);

        llGroupHolder.addView(tvTitle);
        llGroupHolder.addView(ivGroupIndicator);

        llGroupHolder.setOnClickListener(new GroupHolderClickListener());
        addView(llGroupHolder);
        addView(lineView);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (measuredHeight == 0) {
            measuredHeight = getMeasuredHeight();
            LogUtils.e("measuredHeight:" + measuredHeight);
        }
    }

    private class GroupHolderClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final int startValue = measuredHeight;
            final int endValue = llGroupHolder.getMeasuredHeight() + lineView.getMeasuredHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0F);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedValue = valueAnimator.getAnimatedFraction();
                    int height;

                    if (isExpandable) {
                        height = (int) (Utils.evaluate(animatedValue, startValue, endValue) + 0.5F);
                        ivGroupIndicator.setRotationX(animatedValue * 180F);
                    } else {
                        height = (int) (Utils.evaluate(animatedValue, endValue, startValue) + 0.5F);
                        ivGroupIndicator.setRotationX((animatedValue - 1) * 180F);
                    }
//                    LogUtils.e(llChildHolder.getMeasuredHeight() + "\t" + height);
                    getLayoutParams().height = height;
                    requestLayout();
                    if (animatedValue == 1.0F) {
                        isExpandable = !isExpandable;
                    }
                }
            });
            valueAnimator.start();
//            isExpandable = !isExpandable;

        }
    }

    public void setChileView(View view) {

        llChildHolder.addView(view);
    }

    public void setTitleText(String text) {
        tvTitle.setText(text);
    }

    public void setTitleSize(float size) {
        tvTitle.setTextSize(size);
    }

    public void setTitleSize(int unit, float size) {
        tvTitle.setTextSize(unit, size);
    }

    public void setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
    }

    public void setTitleCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (null != left) {
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        }
        if (null != top) {
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        }
        if (null != right) {
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        }
        if (null != bottom) {
            bottom.setBounds(0, 0, bottom.getMinimumWidth(), bottom.getMinimumHeight());
        }
        tvTitle.setCompoundDrawables(left, top, right, bottom);

    }

    public void setTitleCompoundDrawablePadding(int padding) {
        tvTitle.setCompoundDrawablePadding(padding);
        tvTitle.requestLayout();

    }


}
