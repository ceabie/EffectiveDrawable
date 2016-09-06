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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 带按下效果的ImageView.
 *
 * @author ceabie
 */
public class PressedImageButton extends ImageView {

    private boolean mTintSelected = false;

    public PressedImageButton(Context context) {
        super(context);
    }

    public PressedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTintSelected(boolean tintSelected) {
        mTintSelected = tintSelected;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        setTint(gainFocus);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        setTint(pressed);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (mTintSelected) {
            setTint(selected);
        }
    }

    private void setTint(boolean pressed) {
        setColorFilter(pressed? PressedDrawable.colorFilter: null);
    }
}
