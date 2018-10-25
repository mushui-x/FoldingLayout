package com.jersay.foldlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangjie on 2018/10/24.
 */
public class FoldSlidingPanelLayout extends SlidingPaneLayout {

    public FoldSlidingPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View child = getChildAt(0);
        if (child != null) {
            removeView(child);
            final FoldLayout foldLayout = new FoldLayout(getContext());
            foldLayout.addView(child);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            addView(foldLayout, 0, layoutParams);

            setPanelSlideListener(new PanelSlideListener() {
                @Override
                public void onPanelSlide(@NonNull View panel, float slideOffset) {
                    foldLayout.setFactor(slideOffset);
                }

                @Override
                public void onPanelOpened(@NonNull View panel) {
                    
                }

                @Override
                public void onPanelClosed(@NonNull View panel) {

                }
            });
        }
    }
}
