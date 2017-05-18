package com.hskj.damnicomniplusvic.ctsvideo.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

/**
 * Created by DAMNICOMNIPLUSVIC on 2017/4/28.
 * (c) 2017 DAMNICOMNIPLUSVIC Inc,All Rights Reserved.
 */

public class SwipeView extends FloatingActionButton {


    ClickCallBack mClickCallBack;
    float oldx,oldy,relativeX,relativeY;
    public SwipeView(Context context) {
        super(context);
    }

    public SwipeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.mClickCallBack=clickCallBack;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setElevation(10);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldx= event.getX();
                oldy=event.getY();
                relativeX=event.getRawX();
                relativeY=event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float newx,newy;
                newx=event.getX();
                newy=event.getY();
                ViewGroup.MarginLayoutParams par= (ViewGroup.MarginLayoutParams) this.getLayoutParams();
                par.leftMargin= (int) (getLeft()+newx-oldx);
                par.topMargin= (int) (getTop()+newy-oldy);
                if(par.topMargin<0||par.rightMargin<0||par.leftMargin<0||par.bottomMargin<0)
                    break;
                setLayoutParams(par);
                break;
            case MotionEvent.ACTION_UP:
                float x,y;
                x=event.getRawX();
                y=event.getRawY();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.setElevation(0);
                }
                Log.i(TAG, "onTouchEvent: oldx:"+relativeX+" oldy:"+relativeY);
                Log.i(TAG, "onTouchEvent:    x:"+x+"       y:"+y);
                x=Math.abs(x-relativeX);
                y=Math.abs(y-relativeY);
                if(mClickCallBack!=null&&x<10&&y<10)
                    mClickCallBack.click();
                break;
            default:
                break;
        }
        return true;
    }

    public interface ClickCallBack{
        void click();
    }
}
