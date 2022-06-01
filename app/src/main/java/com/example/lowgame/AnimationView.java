package com.example.lowgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

public class AnimationView extends androidx.appcompat.widget.AppCompatImageView {

    private OnAnimationEndListener mOnAnimationEndListener;

    private interface OnAnimationEndListener{
        void onAnimationEnd(View v);
    }

    public AnimationView(Context context) {
        super(context);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAnimationEndListener(OnAnimationEndListener l){
        mOnAnimationEndListener = l ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        AnimationDrawable anim = (AnimationDrawable) getDrawable();
        try{
            //反射调用AnimationDrawable里的mCurFrame值
            Field field = AnimationDrawable.class
                    .getDeclaredField("mCurFrame");
            field.setAccessible(true);
            int curFrame = field.getInt(anim);// 获取anim动画的当前帧
            if (curFrame == anim.getNumberOfFrames() - 1)// 如果已经到了最后一帧
            {
                mOnAnimationEndListener.onAnimationEnd(this);
            }
        }catch (Exception e){e.printStackTrace();}
        super.onDraw(canvas);
    }
}
