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
import android.content.res.Configuration;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import jp.s64.android.insetviews.util.NavigationBarHelper;
import jp.s64.android.insetviews.util.NavigationBarUtils;

public abstract class AbsHorizontalPositionNavigationBarInsetFrameLayout extends FrameLayout
        implements
        NavigationBarHelper.INavigationBarView {

    private static final String RES_NAVIGATION_BAR_WIDTH_NAME = "navigation_bar_width";

    private static final String RES_NAVIGATION_BAR_HEIGHT_DEFTYPE = "dimen";
    private static final String RES_NAVIGATION_BAR_HEIGHT_DEFPACKAGE = "android";

    private final NavigationBarHelper<AbsHorizontalPositionNavigationBarInsetFrameLayout> mHelper;

    private Integer navigationBarWidth = null;
    private boolean zeroIfMultiWindow = false;
    private boolean zeroIfDisabled = false;

    public AbsHorizontalPositionNavigationBarInsetFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public AbsHorizontalPositionNavigationBarInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsHorizontalPositionNavigationBarInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = NavigationBarHelper.instantiate(this);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                navigationBarWidth = insets.getSystemWindowInsetBottom();
                ViewCompat.postInvalidateOnAnimation(AbsHorizontalPositionNavigationBarInsetFrameLayout.this);
                return insets.consumeSystemWindowInsets();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Boolean isMultiWindow;
        if (zeroIfDisabled && !mHelper.hasNavigationBar() || ((isMultiWindow = mHelper.isMultiWindow()) == null && zeroIfDisabled) || zeroIfMultiWindow && isMultiWindow) {
            setMeasuredDimension(
                    MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY)),
                    MeasureSpec.getSize(heightMeasureSpec)
            );
        } else {
            if (navigationBarWidth == null) {
                navigationBarWidth = getHorizontalPositionNavigationWidthFromResource();
            }
            int width, height;
            {
                width = navigationBarWidth != null ? resolveSize(navigationBarWidth, widthMeasureSpec) : MeasureSpec.getSize(widthMeasureSpec);
                height = MeasureSpec.getSize(heightMeasureSpec);
            }
            setMeasuredDimension(width, height);
        }
    }

    @Nullable
    private Integer getHorizontalPositionNavigationWidthFromResource() {
        int resId;

        NavigationBarUtils.NavigationBarPosition position = NavigationBarUtils.getNavigationBarPosition(
                getResources().getConfiguration(),
                (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)
        );

        if (!isSupportedSide(position)) {
            return 0;
        } else {
            resId = getResources().getIdentifier(RES_NAVIGATION_BAR_WIDTH_NAME, RES_NAVIGATION_BAR_HEIGHT_DEFTYPE, RES_NAVIGATION_BAR_HEIGHT_DEFPACKAGE);
        }

        return resId != 0 ? getResources().getDimensionPixelSize(resId) : null;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navigationBarWidth = null;
    }

    @Override
    public void setZeroHeightIfNavigationBarDisabled(boolean doZeroHeight) {
        this.zeroIfDisabled = doZeroHeight;
        requestLayout();
    }

    @Override
    public void setZeroHeightIfMultiWindow(boolean doZeroHeight) {
        this.zeroIfMultiWindow = doZeroHeight;
        requestLayout();
    }

    protected abstract boolean isSupportedSide(NavigationBarUtils.NavigationBarPosition position);

}
