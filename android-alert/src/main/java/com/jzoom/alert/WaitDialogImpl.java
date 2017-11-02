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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

/**
 * Created by renxueliang on 2017/10/29.
 */

class WaitDialogImpl extends BaseDialog {


    private TextView titleView;

    WaitDialogImpl(Context context) {
        super(context, R.style.jzoom_alert_wait,R.layout.alert_dialog_wait);

    }

    @Override
    protected void initDialog(Dialog dialog) {
        super.initDialog(dialog);
        titleView = (TextView) dialog.findViewById(R.id.alert_dialog_title);
        //dialog.setOnDismissListener(this);
        ///dialog.setOnCancelListener();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    /**
     * 设置等待说明
     * @param title
     */
    public void setText(String title) {
        titleView.setText(title);
    }
}
