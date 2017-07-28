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

package jp.s64.android.insetviews.util;

import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class NavigationBarUtils {

    public static NavigationBarPosition getNavigationBarPosition(Configuration configuration, WindowManager wm) {
        return getNavigationBarPosition(configuration, wm.getDefaultDisplay());
    }

    public static NavigationBarPosition getNavigationBarPosition(Configuration configuration, Display display) {
        Point point = new Point();
        {
            DisplayCompat.getSize(display, point);
        }
        return getNavigationBarPosition(point.x, point.y, configuration.smallestScreenWidthDp, display.getRotation());
    }

    @Nullable
    public static NavigationBarPosition getNavigationBarPosition(int screenWidth, int screenHeight, int smallestScreenWidthDp, int displaySurfaceRotation) {
        if (canNavigationBarMove(screenWidth, screenHeight, smallestScreenWidthDp) && screenWidth > screenHeight) {
            return displaySurfaceRotation == Surface.ROTATION_270 ? NavigationBarPosition.LEFT : NavigationBarPosition.RIGHT;
        }
        return NavigationBarPosition.BOTTOM;
    }

    public static boolean canNavigationBarMove(Configuration configuration, WindowManager wm) {
        return canNavigationBarMove(configuration, wm.getDefaultDisplay());
    }

    public static boolean canNavigationBarMove(Configuration configuration, Display display) {
        Point point = new Point();
        {
            DisplayCompat.getSize(display, point);
        }
        return canNavigationBarMove(point.x, point.y, configuration.smallestScreenWidthDp);
    }

    public static boolean canNavigationBarMove(int screenWidth, int screenHeight, int smallestScreenWidthDp) {
        return screenWidth != screenHeight && smallestScreenWidthDp < 600;
    }

    public static boolean isHorizontalPosition(NavigationBarPosition position) {
        return position == NavigationBarPosition.LEFT || position == NavigationBarPosition.RIGHT;
    }

    public enum NavigationBarPosition {
        BOTTOM,
        LEFT,
        RIGHT,
        //
        ;
    }

}
