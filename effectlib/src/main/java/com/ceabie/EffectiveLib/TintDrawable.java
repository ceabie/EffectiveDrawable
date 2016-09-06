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

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * The type Pressed drawable.
 *
 * @author ceabie
 */
public class TintDrawable extends Drawable implements Drawable.Callback {
    private RectF mRectF;
    protected Drawable mDrawable;
    private Paint mPaintTint = null;
    private int mColorMark = 0;

    public static Drawable get(Drawable drawable, ColorFilter colorFilter) {
        TintDrawable tintDrawable = drawable != null ? new TintDrawable(drawable) : null;
        if (tintDrawable != null) {
            tintDrawable.setColorTint(colorFilter);
        }
        return tintDrawable;
    }

    public static Drawable get(Drawable drawable, int colorMark) {
        TintDrawable tintDrawable = drawable != null ? new TintDrawable(drawable) : null;
        if (tintDrawable != null) {
            tintDrawable.setColorMark(colorMark);
        }
        return tintDrawable;
    }

    public TintDrawable(Drawable drawable) {
        setWrappedDrawable(drawable);
    }

    public void setColorTint(ColorFilter colorFilter) {
        if (mPaintTint == null) {
            mPaintTint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        mPaintTint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public ColorFilter getColorTint() {
        if (mPaintTint != null) {
            return mPaintTint.getColorFilter();
        }

        return null;
    }


    public void setColorMark(int colorMark) {
        mColorMark = colorMark;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null) {
            if (mPaintTint != null && mPaintTint.getColorFilter() != null) {
                if (mRectF == null) {
                    mRectF = new RectF();
                }

                mRectF.set(getBounds());
                int i = canvas.saveLayer(mRectF, mPaintTint,
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG);
                mDrawable.draw(canvas);
                canvas.restoreToCount(i);
            } else {
                mDrawable.draw(canvas);
            }

            if (mColorMark != 0) {
                canvas.drawColor(mColorMark);
            }
        }
    }

    @Override
    public boolean setState(int[] stateSet) {
        boolean handleState = super.setState(stateSet);
        if (mDrawable != null) {
            handleState = handleState || mDrawable.setState(stateSet);
            if (handleState) {
                invalidateSelf();
            }
        }
        return handleState;
    }

    @Override
    public Region getTransparentRegion() {
        return mDrawable.getTransparentRegion();
    }

    @Override
    public int getIntrinsicWidth() {
        return mDrawable.getIntrinsicWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMinimumWidth() {
        return mDrawable.getMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return mDrawable.getMinimumHeight();
    }

    @Override
    public boolean getPadding(Rect padding) {
        return mDrawable.getPadding(padding);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mDrawable.setBounds(bounds);
    }

    @Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    @Override
    public Drawable mutate() {
        Drawable wrapped = mDrawable;
        Drawable mutated = wrapped.mutate();
        if (mutated != wrapped) {
            // If mutate() returned a new instance, update our reference
            setWrappedDrawable(mutated);
        }
        // We return ourselves, since only the wrapped drawable needs to mutate
        return this;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int getAlpha() {
        if (mDrawable != null) {
            return mDrawable.getAlpha();
        }

        return super.getAlpha();
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    public int[] getState() {
        return mDrawable.getState();
    }

    @Override
    public void setChangingConfigurations(int configs) {
        mDrawable.setChangingConfigurations(configs);
    }

    @Override
    public int getChangingConfigurations() {
        return mDrawable.getChangingConfigurations();
    }

    @Override
    public Drawable getCurrent() {
        return mDrawable.getCurrent();
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        return super.setVisible(visible, restart) || mDrawable.setVisible(visible, restart);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mDrawable.setColorFilter(cf);
    }

    @Override
    public void setDither(boolean dither) {
        mDrawable.setDither(dither);
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mDrawable.setFilterBitmap(filter);
    }

    @Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }

    public void setWrappedDrawable(Drawable drawable) {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }

        mDrawable = drawable;

        if (drawable != null) {
            drawable.setCallback(this);
        }
        invalidateSelf();
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }
}
