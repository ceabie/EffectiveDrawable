/*
 * Copyright (C) 2016 ceabie (hhttp://blog.csdn.net/ceabie)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ceabie.EffectiveLib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ceabie.magiclib.R;


/**
 * The type Effect image view.
 *
 * @author ceabie
 */
public class EffectImageView extends ImageView {
    private RectF mRectF;

    private Paint mPaint;
    private boolean mHardware;
    private Drawable mEffectDrawable;

    private static PorterDuffXfermode mXfermodeDstIn = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    public EffectImageView(Context context) {
        super(context);
        initView(null);
    }

    public EffectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public EffectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.EffectImageView);
            mEffectDrawable = array.getDrawable(R.styleable.EffectImageView_EffectMark);
            array.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mHardware = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                && (ViewCompat.getLayerType(this) != ViewCompat.LAYER_TYPE_SOFTWARE);
        if (mHardware) {
            // 使用硬件加速裁剪
            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_HARDWARE, mPaint);
        }
    }

    public void setEffectDrawable(Drawable drawable) {
        mEffectDrawable = drawable;
        if (mEffectDrawable != null) {
            mEffectDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
    }

    public Drawable getEffectDrawable() {
        return mEffectDrawable;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mEffectDrawable != null) {
            mEffectDrawable.setBounds(0, 0, w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mEffectDrawable == null
                || mEffectDrawable.getOpacity() == PixelFormat.TRANSPARENT // 透明
                || getDrawable() == null) {
            super.onDraw(canvas);
            return;
        }

        if (mRectF == null) {
            mRectF = new RectF();
        }

        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = getMeasuredWidth();
        mRectF.bottom = getMeasuredHeight();

        int saveBottom = 0;
        //
        if (!mHardware) {
            saveBottom = canvas.saveLayer(mRectF, null,
                    Canvas.CLIP_TO_LAYER_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        }

        super.onDraw(canvas);

        mPaint.setXfermode(mXfermodeDstIn);
        int saveCount = canvas.saveLayer(mRectF, mPaint, Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        mPaint.setXfermode(null);

        mEffectDrawable.draw(canvas);

        canvas.restoreToCount(saveCount);

        if (!mHardware) {
            canvas.restoreToCount(saveBottom);
        }
    }
}
