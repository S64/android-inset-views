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

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NavigationBarHelper<SELF extends View & NavigationBarHelper.INavigationBarView> {

    public static <SELF extends View & NavigationBarHelper.INavigationBarView> NavigationBarHelper<SELF> instantiate(SELF self) {
        return new NavigationBarHelper(self);
    }

    private static final String SYSTEM_PROP_QEMU_MAINKEY_DISABLED_KEY = "qemu.hw.mainkeys";

    private static final String SYSTEM_PROP_CLASS_NAME = "android.os.SystemProperties";
    private static final String SYSTEM_PROP_CLASS_GET_METHOD_NAME = "get";

    private static final String WINDOW_MANAGER_GLOBAL_CLASS_NAME = "android.view.WindowManagerGlobal";
    private static final String WINDOW_MANAGER_GLOBAL_CLASS_GET_WS_METHOD_NAME = "getWindowManagerService";

    private static final String I_WINDOW_MANAGER_CLASS_NAME = "android.view.IWindowManager";
    private static final String I_WINDOW_MANAGER_CLASS_HAS_NVB_METHOD_NAME = "hasNavigationBar";

    private static final String RES_NAVIGATION_BAR_ENABLED_NAME = "config_showNavigationBar";
    private static final String RES_NAVIGATION_BAR_ENABLED_DEFTYPE = "bool";
    private static final String RES_NAVIGATION_BAR_ENABLED_DEFPACKAGE = "android";

    private final SELF self;

    protected NavigationBarHelper(SELF self) {
        this.self = self;
    }

    public boolean hasNavigationBar() {
        boolean ret = false;

        try {
            Boolean hasSystem = hasSystemWideNavigationBar();
            if (hasSystem != null) {
                ret = hasSystem;
            }
        } catch (NavigationBarHelperException e) {
            int resId = self.getResources().getIdentifier(RES_NAVIGATION_BAR_ENABLED_NAME, RES_NAVIGATION_BAR_ENABLED_DEFTYPE, RES_NAVIGATION_BAR_ENABLED_DEFPACKAGE);
            if (resId != 0) {
                ret = self.getResources().getBoolean(resId);
            }
        }

        String qemuMainKey = getSystemProperty(SYSTEM_PROP_QEMU_MAINKEY_DISABLED_KEY);
        if ("1".equals(qemuMainKey)) {
            ret = false;
        } else if ("0".equals(qemuMainKey)) {
            ret = true;
        }

        return ret;
    }

    @Nullable
    public Boolean isMultiWindow() {
        Activity activity;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return false;
        } else if ((activity = getActivity()) == null) {
            return null; // unknown
        } else {
            return activity.isInMultiWindowMode();
        }
    }

    protected Class<?> getSystemPropertyClass() {
        try {
            return self.getContext().getClassLoader().loadClass(SYSTEM_PROP_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new NavigationBarHelperException(e);
        }
    }

    @Nullable
    protected String getSystemProperty(String key) {
        try {
            Method method = getSystemPropertyClass().getDeclaredMethod(SYSTEM_PROP_CLASS_GET_METHOD_NAME, String.class);
            Object ret = method.invoke(null, key);
            if (ret != null) {
                return (String) ret;
            }
            return null;
        } catch (NoSuchMethodException e) {
            throw new NavigationBarHelperException(e);
        } catch (InvocationTargetException e) {
            throw new NavigationBarHelperException(e);
        } catch (IllegalAccessException e) {
            throw new NavigationBarHelperException(e);
        }
    }

    protected Class<?> getWindowManagerGlobalClass() {
        try {
            return self.getContext().getClassLoader().loadClass(WINDOW_MANAGER_GLOBAL_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new NavigationBarHelperException(e);
        }
    }

    protected Class<?> getIWindowManagerClass() {
        try {
            return self.getContext().getClassLoader().loadClass(I_WINDOW_MANAGER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new NavigationBarHelperException(e);
        }
    }

    protected Object getWindowManagerService() {
        try {
            Method method = getWindowManagerGlobalClass().getMethod(WINDOW_MANAGER_GLOBAL_CLASS_GET_WS_METHOD_NAME);
            return method.invoke(null);
        } catch (NoSuchMethodException e) {
            throw new NavigationBarHelperException(e);
        } catch (IllegalAccessException e) {
            throw new NavigationBarHelperException(e);
        } catch (InvocationTargetException e) {
            throw new NavigationBarHelperException(e);
        }
    }

    @Nullable
    protected Boolean hasSystemWideNavigationBar() {
        try {
            Method method = getIWindowManagerClass().getDeclaredMethod(I_WINDOW_MANAGER_CLASS_HAS_NVB_METHOD_NAME);
            Object ret = method.invoke(getWindowManagerService());
            if (ret != null) {
                return (boolean) ret;
            }
        } catch (NoSuchMethodException e) {
            throw new NavigationBarHelperException(e);
        } catch (IllegalAccessException e) {
            throw new NavigationBarHelperException(e);
        } catch (InvocationTargetException e) {
            throw new NavigationBarHelperException(e);
        }
        return null;
    }

    @Nullable
    protected Activity getActivity() {
        Context current = self.getContext();
        while (current instanceof ContextWrapper) {
            if (current instanceof Activity) {
                return (Activity) current;
            }
            current = ((ContextWrapper) current).getBaseContext();
        }
        return null;
    }


    public interface INavigationBarView {

        void setZeroHeightIfNavigationBarDisabled(boolean doZeroHeight);

        void setZeroHeightIfMultiWindow(boolean doZeroHeight);

    }

    public static class NavigationBarHelperException extends RuntimeException {

        public NavigationBarHelperException(Throwable throwable) {
            super(throwable);
        }

    }

}
