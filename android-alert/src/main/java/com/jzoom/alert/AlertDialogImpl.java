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
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * alert dialig implements
 *
 */
class AlertDialogImpl extends BaseDialog implements AlertDialog, View.OnClickListener {

    private String title;
    private String[] buttons;
    private int[] styles;

    private View contentView;

    private Alert.AlertListener alertListener;

    AlertDialogImpl(Context context) {
        super(context, R.style.jzoom_alert_dialog, R.layout.alert_dialog_content);
    }

    @Override
    public AlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public AlertDialog setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        return this;
    }

    @Override
    public AlertDialog setCanceledOnTouchOutside(boolean cancelOnTouchOutside) {
        super.setCanceledOnTouchOutside(cancelOnTouchOutside);
        return this;
    }

    @Override
    public AlertDialog setContent(String content) {
        TextView contentTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.alert_msg_text_view,null);
        contentTextView.setText(content);
        setContentView(contentTextView);
        return this;
    }

    @Override
    public AlertDialog setButtons(String... buttons) {
        this.buttons = buttons;
        return this;
    }

    @Override
    public AlertDialog setButtonStyles(int... styles) {
        this.styles = styles;
        return this;
    }

    @Override
    public AlertDialog setAlertListener(Alert.AlertListener listener) {
        this.alertListener = listener;
        return this;
    }

    @Override
    public AlertDialog setContentView(View view) {
        this.contentView = view;
        return this;
    }

    @Override
    public View getContentView() {
        return contentView;
    }


    @Override
    protected void initDialog(Dialog dialog) {
        super.initDialog(dialog);
        create(dialog);
        if(contentView!=null){
            if(contentView!=null){
                ViewGroup container = (ViewGroup) dialog.findViewById(R.id.alert_dialog_content);
                container.addView(contentView);
            }
        }
    }


    private void create(Dialog dialog){
        if(buttons==null){
            throw new NullPointerException("buttons is null");
        }
        if(buttons.length < 1 || buttons.length > 2){
            throw new RuntimeException("按钮的数量必须为1或者2");
        }
        ViewGroup container = (ViewGroup) dialog.findViewById(R.id.alert_dialog_container);
        TextView titlteView = (TextView) dialog.findViewById(R.id.alert_dialog_title);
        titlteView.setText(title);
        //按钮
        if(buttons.length==2){
            View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_buttons_ok_cancel,null);
            container.addView(view);
            initButton(view,R.id.cancel,0);
            initButton(view,R.id.ok,1);
        }else{
            Button view = (Button) LayoutInflater.from(context).inflate(R.layout.alert_dialog_button,null);
            container.addView(view);
            initButton(view,R.id.ok,0);
        }

    }

    private void initButton(View parent,int id,int index){
        Button btn = (Button) parent.findViewById(id);
        btn.setText(buttons[index]);
        btn.setOnClickListener(this);
        if(styles!=null && styles.length >= index+1){
            setButtonStyle(context,btn,styles[index]);
        }
    }

    private static void setButtonStyle(Context context,Button btn,int style){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn.setTextAppearance(style);
        }else {
            btn.setTextAppearance(context,style);
        }
    }

    @Override
    public void onClick(View v) {
        int id= v.getId();
        if(id==R.id.ok){
            notify(Alert.OK);
        }else{
            onCancel(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        notify(Alert.CANCEL);
    }

    private void notify(int buttonId) {
        if(alertListener!=null){
            alertListener.onAlert(buttonId);
            alertListener = null;
        }
        dialog.dismiss();;
        dialog = null;
    }
}
