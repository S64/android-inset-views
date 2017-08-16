# android-inset-views

Custom views for Android's inset areas.

This is contains below components:

- StatusBarInsetFrameLayout
- BottomNavigationBarInsetFrameLayout
- HorizontalPositionNavigationBarInsetFrameLayout
- LeftNavigationBarInsetFrameLayout
- RightNavigationBarInsetFrameLayout
- DisplayCompat

Japanese details is available here: [blog.s64.jp](http://blog.s64.jp/entry/2017/07/21/Android%E3%81%AEStatusBar%2C_NavigationBar%E3%81%AE%E9%AB%98%E3%81%95%E3%82%92%E6%8C%81%E3%81%A4View%E7%BE%A4_%22android-inset-views%22_%E3%82%92OSS%E3%81%A8%E3%81%97%E3%81%A6%E5%85%AC%E9%96%8B%E3%81%97%E3%81%BE)

## Usages

Add following lines to your buildscripts.

```groovy
buildscript {
    ext {
        inset_views_version = '0.6.0'
    }
}
```

```groovy
repositories {
    maven {
        url 'http://dl.bintray.com/s64/maven'
    }
}

dependencies {
    compile("jp.s64.android:insetviews:${inset_views_version}")
}
```

## Apps using android-inset-views

- [Wantedly Visit](https://play.google.com/store/apps/details?id=com.wantedly.android.visit) & [Intern](https://play.google.com/store/apps/details?id=com.wantedly.android.student)

## Donate

<a href="https://donorbox.org/android-inset-views"><img src="https://d1iczxrky3cnb2.cloudfront.net/button-small-blue.png"/></a>

## License

```
Copyright 2017 Shuma Yoshioka

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
