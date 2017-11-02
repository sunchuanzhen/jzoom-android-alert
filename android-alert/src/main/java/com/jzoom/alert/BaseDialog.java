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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jzoom.popup.Popup;

/**
 * Created by renxueliang on 2017/10/29.
 */

abstract class BaseDialog implements DialogInterface.OnCancelListener,Popup {

    protected Dialog dialog;
    protected Context context;
    protected int style;
    protected int layout;
    protected boolean cancelOnTouchOutside;
    protected boolean cancelable;

    BaseDialog(Context context,int style,int layout){
        this.context = context;
        this.style = style;
        this.layout = layout;
    }


    protected void initDialog(Dialog dialog){

        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        if(cancelOnTouchOutside){
            dialog.setOnCancelListener(this);
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(cancelable);
        }
    }


    @Override
    public Popup show(){
        dialog = new Dialog(context,style);
        initDialog(dialog);
        dialog.show();
        return this;
    }


    @Override
    public Popup setCanceledOnTouchOutside(boolean cancelOnTouchOutside) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        return this;
    }

    @Override
    public Popup setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public void dismiss(){
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
