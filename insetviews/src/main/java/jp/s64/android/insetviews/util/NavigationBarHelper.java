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

import android.view.View;

public class NavigationBarHelper<SELF extends View & NavigationBarHelper.INavigationBarView> {

    public static <SELF extends View & NavigationBarHelper.INavigationBarView> NavigationBarHelper<SELF> instantiate(SELF self) {
        return new NavigationBarHelper(self);
    }

    private static final String RES_NAVIGATION_BAR_ENABLED_NAME = "config_showNavigationBar";
    private static final String RES_NAVIGATION_BAR_ENABLED_DEFTYPE = "bool";
    private static final String RES_NAVIGATION_BAR_ENABLED_DEFPACKAGE = "android";

    private final SELF self;

    protected NavigationBarHelper(SELF self) {
        this.self = self;
    }

    public boolean hasNavigationBar() {
        int resId = self.getResources().getIdentifier(RES_NAVIGATION_BAR_ENABLED_NAME, RES_NAVIGATION_BAR_ENABLED_DEFTYPE, RES_NAVIGATION_BAR_ENABLED_DEFPACKAGE);
        if (resId != 0) {
            return self.getResources().getBoolean(resId);
        }
        return true;
    }

    public interface INavigationBarView {

        void setZeroHeightIfNavigationBarDisabled(boolean doZeroHeight);

    }

}
