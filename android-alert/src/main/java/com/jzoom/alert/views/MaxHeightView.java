/*
 * Copyright (c) 2017-present, JZoom, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the Apache-2.0 license found in the
 * LICENSE page
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 */

package com.jzoom.alert.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;


/**
 * Created by renxueliang on 16/12/13.
 */

public class MaxHeightView extends LinearLayout {


    private int mMaxHeight;

    public MaxHeightView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float height = dm.heightPixels;
        mMaxHeight = (int) (height * 0.7);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
                     heightSize = heightSize <= mMaxHeight ? heightSize
                               : (int) mMaxHeight;
                 }

          if (heightMode == MeasureSpec.UNSPECIFIED) {
                       heightSize = heightSize <= mMaxHeight ? heightSize
                         : (int) mMaxHeight;
              }
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = heightSize <= mMaxHeight ? heightSize
                           : (int) mMaxHeight;
                }
           int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                     heightMode);
           super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
      }
}
