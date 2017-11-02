package com.jzoom.popup;

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

/**
 * Created by renxueliang on 2017/10/29.
 */

public interface Popup {
    /**
     * 设置外部点击是否消失
     *
     * @param cancelOnTouchOutside  当这个参数为true的时候，相当于自动设置setCancelable(true)
     * @return
     */
    Popup setCanceledOnTouchOutside(boolean cancelOnTouchOutside);

    /**
     * 是否可以取消
     * @param cancelable
     * @return
     */
    Popup setCancelable(boolean cancelable);

    /**
     * 展示出来
     * @return
     */
    Popup show();
}
