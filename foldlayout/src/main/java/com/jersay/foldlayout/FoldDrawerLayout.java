package com.jersay.foldlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangjie on 2018/10/25.
 */
public class FoldDrawerLayout extends DrawerLayout {

    private static final String TAG = "FoldDrawerLayout";

    public FoldDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (isDrawerView(child)) {
                Log.e(TAG, "onAttachedToWindow: " + i);
                FoldLayout foldLayout = new FoldLayout(getContext());
                foldLayout.setAnchor(1);
                removeView(child);
                foldLayout.addView(child);
                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                addView(foldLayout, i, layoutParams);
            }
        }

        addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (drawerView instanceof FoldLayout) {
                    FoldLayout foldLayout = (FoldLayout) drawerView;
                    foldLayout.setFactor(slideOffset);
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        return (absGravity & (Gravity.LEFT | Gravity.RIGHT)) != 0;
    }
}
