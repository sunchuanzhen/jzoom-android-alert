package com.jzoom.alert;

/*
 * *
 *  * Copyright (c) $year-present, JZoom, Inc.
 *  * All rights reserved.
 *  *
 *  * This source code is licensed under the Apache-2.0 license found in the
 *  * LICENSE page
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *
 *
 *
 */

import android.view.View;

import com.jzoom.popup.Popup;

/**
 * Created by renxueliang on 2017/10/29.
 */

public interface AlertDialog extends Popup {

    /**
     * 设置标题
     * @param title
     * @return
     */
    AlertDialog setTitle(String title);
    /**
     * 能否被取消
     * @param cancelable
     * @return
     */
    AlertDialog setCancelable(boolean cancelable);

    /**
     * 设置在外部点击是否取消
     * @param cancelOnTouchOutside      在外部点击是否取消
     * @return
     */
    AlertDialog setCanceledOnTouchOutside(boolean cancelOnTouchOutside);

    /**
     * 内容
     * @param content
     * @return
     */
    AlertDialog setContent(String content);

    /**
     * 设置按钮
     * @param buttons
     * @return
     */
    AlertDialog setButtons(String...buttons);

    /**
     * 设置样式
     * @param styles
     * @return
     */
    AlertDialog setButtonStyles(int...styles);

    /**
     * 设置监听
     * @param listener  监听
     * @return
     */
    AlertDialog setAlertListener(Alert.AlertListener listener);

    /**
     * 设置内容视图
     * @param view
     * @return
     */
    AlertDialog setContentView(View view);

    /**
     * 获取内容视图
     * @return
     */
    View getContentView();

}
