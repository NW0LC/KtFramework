[![](https://jitpack.io/v/NW0LC/KtFramework.svg)](https://jitpack.io/#NW0LC/KtFramework)
Android-KtFramework
===========================


![MyFramework Library](http://chuantu.biz/t6/52/1505655126x3084152555.jpg)


This project contains a lot of convenient and development of the libraries, tools, and controls.Offline popular network framework
This libraly include all important methods for serial port profile on bluetooth communication. It has built-in bluetooth device list.



Feature
--------------

• It's very easy to use for me 

•    //====================分包错误包========================

	compile 'com.android.support:multidex:1.0.1'
	
•    //====================类似相对布局========================

	compile 'com.android.support.constraint:constraint-layout:1.0.2'
	
•    //====================毛玻璃view========================

	compile 'com.github.mmin18:realtimeblurview:1.1.0'
	
•    //====================上下拉刷新========================

	compile 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.4-alpha-1'

•    //====================recyclerView adapter封装类========================

	compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.28'

•    //====================图片加载========================

	compile 'com.lzy.widget:imagepicker:0.6.1'
	
	compile 'com.github.bumptech.glide:glide:4.1.1'
	
	annotationProcessor 'com.github.bumptech.glide:compiler:4.1.1'
	
•    //    compile 'com.github.chrisbanes:PhotoView:+'

	compile 'pl.droidsonroids.gif:android-gif-drawable:1.2.8'

•    //====================小工具========================

    compile 'com.blankj:utilcode:latest.release'

•    //====================网络请求========================

•//    okhttp = 'com.squareup.okhttp3:okhttp:latest.release'

•//    retrofit = 'com.squareup.retrofit2:retrofit:latest.release'

    compile 'com.lzy.net:okgo:3.0.4'

•    //====================圆头像SimpleDraweeView========================

    compile 'com.facebook.fresco:fresco:1.5.0'

•    //====================lambda ========================

    compile 'me.tatarka:gradle-retrolambda:latest.release'

•    //====================侧滑删除 ========================

    compile 'com.github.mcxtzhang:SwipeDelMenuLayout:V1.2.5'

•    //====================webView ========================

    compile 'com.github.delight-im:Android-AdvancedWebView:v3.0.0'

•    //====================通用popWindow ========================

    compile 'com.github.razerdp:BasePopup:latest.release'

•    //====================权限申请 ========================

    compile 'com.github.hotchemi:permissionsdispatcher:3.0.0'

    annotationProcessor 'com.github.hotchemi:permissionsdispatcher-processor:3.0.0'

•    //====================轮播 ========================

    compile 'com.jude:rollviewpager:1.4.6'

    compile 'com.youth.banner:banner:1.4.10'

•    //====================json解析 ========================

    compile 'com.alibaba:fastjson:latest.release'

    compile 'com.google.code.gson:gson:2.8.0'

•    //====================// Anko kotlin 插件 代码布局库========================

    compile 'org.jetbrains.anko:anko-sdk15:0.10.1' // sdk19 sdk21 sdk23 are also available

    compile 'org.jetbrains.anko:anko-support-v4:0.10.1' // In case you need support-v4 bindings

    compile 'org.jetbrains.anko:anko-appcompat-v7:0.10.1' // For appcompat-v7 bindings

    compile 'org.jetbrains.anko:anko-recyclerview-v7:0.10.1' // For ankoRecyclerview bindings

•    //====================星星之间的间距========================

    compile 'com.xingliuhua:xlhratingbar_lib:1.0.1'

•    //====================俗名：垂直跑马灯学名：垂直翻页公告========================

    compile 'com.sunfusheng:marqueeview:1.3.2'

•    //====================产品标签========================

	compile 'com.zhy:flowlayout-lib:1.0.3'

•    //====================圆圈加数字指示========================

	compile 'q.rorbin:badgeview:1.1.2'

•    //====================//HTML Parser html解析========================

	compile 'org.jsoup:jsoup:1.10.3'

•    //====================//用于解析Adobe After Effects动画========================

	compile 'com.airbnb.android:lottie:2.1.0'

•    //====================圆形图片========================

	compile 'de.hdodenhof:circleimageview:2.1.0'

•    //====================回调========================
	
	compile 'com.hwangjr.rxbus:rxbus:2.0.0'

•    //====================RxAndroid========================

•//    compile 'io.reactivex:rxandroid:1.2.1'

•//    compile 'io.reactivex:rxjava:1.3.2'

•    //====================崩溃页========================

	compile 'cat.ereza:customactivityoncrash:2.1.0'

•    //====================dialog========================

	compile 'com.github.PopFisher:SmartAlertPop:c505c4b4b1'
	
	compile 'me.drakeet.materialdialog:library:1.3.1'

•    //====================flyco========================

•    //tabLayout

	compile 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
	
•    //圆角view

	compile 'com.flyco.roundview:FlycoRoundView_Lib:1.1.4@aar'

•    //====================ButterKnife========================

	compile 'com.jakewharton:butterknife:8.8.1'
	
	compile 'com.jakewharton:butterknife-compiler:8.8.1'

•    //====================上下滑动选择器========================

	compile 'com.bigkoo:pickerview:2.1.1'

•    //====================沉浸状态栏========================

	compile 'com.readystatesoftware.systembartint:systembartint:1.0.4'

•    //====================umeng统计========================

    compile "com.umeng.analytics:analytics:latest.integration"

•    //====================kotlin========================

    compile "org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version"


Download
--------------

Maven
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

      	<dependency>
	    <groupId>com.github.NW0LC</groupId>
	    <artifactId>KtFramework</artifactId>
	    <version>1.0.0</version>
	</dependency>

```

Gradle
```
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.NW0LC:KtFramework:1.0.0'
	}
```

注意！！
--------------
• 在自己的gradle里 要加上以下代码
  用来支持realm数据库、kotlin及其扩展库，和防止support包冲突

Plugin
```
    apply plugin: 'kotlin-android'
    apply plugin: 'realm-android'
    apply plugin: 'kotlin-android-extensions'
```
防止support包冲突
```
    configurations.all {
         resolutionStrategy.eachDependency { DependencyResolveDetails details ->
             def requested = details.requested
            if (requested.group == 'com.android.support') {
                if (!requested.name.startsWith("multidex")) {
                    details.useVersion '26.0.0-alpha1'//此处的版本号可以替换
                }
            }
        }
    }
```

Simple Usage
--------------

• Import this library to your workspace and include in to your android project 
For Eclipse ADT : Download this library and import into your workspace and include this library to your project
For Android Studio : Use Gradle to download this library from Maven




License
--------------

Copyright (c) 2014 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
