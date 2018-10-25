# FoldingLayout
带折叠效果的侧滑菜单

![折叠侧滑01](https://github.com/JersayZhang/FoldingLayout/blob/master/image/fold01.gif)
![折叠侧滑02](https://github.com/JersayZhang/FoldingLayout/blob/master/image/fold02.gif)

### Use
#### 1、引入依赖
```
        implementation 'com.jersay.android:foldlayout:1.0.1'
```
#### 2、在XML中引入view
```
    <com.jersay.foldlayout.FoldDrawerLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SlidingPanelLayoutSampleActivity">

    ...

    </com.jersay.foldlayout.FoldDrawerLayout>
```
    或者
```
    <com.jersay.foldlayout.FoldSlidingPanelLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SlidingPanelLayoutSampleActivity">

    ...

    </com.jersay.foldlayout.FoldDrawerLayout>
```
