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

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

/**
 * The type Pressed drawable.
 *
 * @author ceabie
 */
public class PressedDrawable extends TintDrawable {
    static ColorFilter colorFilter = new PorterDuffColorFilter(0x33000000, PorterDuff.Mode.SRC_ATOP);
    private boolean mTintSelected = false;

    public static Drawable get(Drawable drawable) {
        return drawable != null? new PressedDrawable(drawable): null;
    }

    public PressedDrawable(Drawable drawable) {
        super(drawable);
    }

    public void setTintSelected(boolean tintSelected) {
        mTintSelected = tintSelected;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        if (mDrawable != null) {
            if (state != null) {
                for (int i : state) {
                    if (i == android.R.attr.state_pressed
                        || (mTintSelected && i == android.R.attr.state_selected)
                            || i == android.R.attr.state_focused) {
                        setColorTint(colorFilter);
                        return true;
                    }
                }
            }

            if (getColorTint() != null) {
                setColorTint(null);
                return true;
            }
        }

        return super.onStateChange(state);
    }
}
