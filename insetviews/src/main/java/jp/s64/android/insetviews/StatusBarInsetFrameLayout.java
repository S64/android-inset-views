/*
 * Copyright (C) 2017 Shuma Yoshioka
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

package jp.s64.android.insetviews;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class StatusBarInsetFrameLayout extends FrameLayout {

    private static final String RES_STATUS_BAR_HEIGHT_NAME = "status_bar_height";
    private static final String RES_STATUS_BAR_HEIGHT_DEFTYPE = "dimen";
    private static final String RES_STATUS_BAR_HEIGHT_DEFPACKAGE = "android";

    private Integer statusBarHeight = null;

    public StatusBarInsetFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public StatusBarInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                statusBarHeight = insets.getSystemWindowInsetTop();
                ViewCompat.postInvalidateOnAnimation(StatusBarInsetFrameLayout.this);
                return insets.consumeSystemWindowInsets();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (statusBarHeight == null) {
            int resId = getResources().getIdentifier(RES_STATUS_BAR_HEIGHT_NAME, RES_STATUS_BAR_HEIGHT_DEFTYPE, RES_STATUS_BAR_HEIGHT_DEFPACKAGE);
            if (resId != 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resId);
            }
        }
        int width, height;
        {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = statusBarHeight != null ? resolveSize(statusBarHeight, heightMeasureSpec) : MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

}
