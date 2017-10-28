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

package com.jzoom.alert;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

/**
 * 弹出框
 *
 * 支持如下功能：
 *
 * 告知
 * 确认
 *
 *
 *
 *
 */
public class Alert {


    public static final int OK = 0;
    public static final int CANCEL = 1;

    public static final int YES = OK;
    public static final int NO = CANCEL;




    public interface AlertListener{
        /**
         * 用户点击了某个alert上面的按钮
         * @param buttonId  OK|CANCEL YES|NO
         */
        void onAlert(int buttonId);
    }


    public interface InputListener{
        /**
         * 用户编辑了/取消编辑信息
         * @param buttonId
         */
        void onInput(int buttonId,String content);
    }


    public interface SelectListener{
        /**
         * 选择了列表中的一个内容
         * @param buttonId
         * @param index         列表中选项的下标
         */
        void onSelect(int buttonId,int index);
    }



    /**
     * 通知
     * @param title
     * @param content
     * @param listener
     */
    public static void alert(Context context,String title, String content, AlertListener listener){
        builder(context).setButtons(context.getString(R.string.ok) )
                .setTitle(title)
                .setContent(content).alert(listener);
    }

    /**
     * 通知
     * @param content
     * @param listener
     */
    public static void alert(Context context,String content,AlertListener listener){
        builder(context).setButtons(context.getString(R.string.ok) )
                .setTitle(content).alert(listener);
    }


    /**
     * 确认对话框
     * @param title
     * @param content
     * @param listener
     */
    public static void confirm(Context context,String title,String content,AlertListener listener){
        builder(context).setButtons(context.getString(R.string.cancel),context.getString(R.string.ok))
                .setTitle(title).setContent(content).alert(listener);
    }



    /**
     * 确认对话框
     * @param content
     * @param listener
     */
    public static void confirm(Context context,String content,AlertListener listener){

    }




    private static class AlertBuilderImpl implements  AlertBuilder, DialogInterface.OnCancelListener {

        private String title;
        private String content;
        private String[] buttons;
        private int[] styles;
        private boolean cancelOnTouchOutside;
        private boolean cancelable = true;
        private final Context context;

        public AlertBuilderImpl(Context context) {
            this.context = context;
        }

        @Override
        public AlertBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        @Override
        public AlertBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        @Override
        public AlertBuilder setButtons(String... buttons) {
            this.buttons = buttons;
            return this;
        }

        @Override
        public AlertBuilder setButtonStyles(int... styles) {
            this.styles = styles;
            return this;
        }

        @Override
        public AlertBuilder setCancelOnTouchOutside(boolean cancelOnTouchOutside) {
            this.cancelOnTouchOutside = cancelOnTouchOutside;
            return this;
        }


        private AlertListener alertListener;

        @Override
        public void alert(AlertListener listener) {
            if(finishing(context))return;
            TextView contentTextView =null;
            if(content!=null){
                contentTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.alert_msg_text_view,null);
                contentTextView.setText(content);
            }
            popup(contentTextView,listener);
        }

        @Override
        public void showWait() {
            if(finishing(context))return;
            if(waitDialog==null){
                waitDialog = initDialog(R.style.jzoom_alert_wait,R.layout.alert_dialog_wait);
                waitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        waitDialog = null;
                        Alert.builder = null;
                    }
                });
                waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if(alertListener!=null){
                            alertListener.onAlert(Alert.CANCEL);
                            alertListener = null;
                        }
                        Alert.builder = null;
                    }
                });
                waitDialog.show();
            }
            TextView titleView = waitDialog.findViewById(R.id.alert_dialog_title);
            titleView.setText(title);
        }

        private Dialog initDialog(int style,int layout){
            Dialog dialog = new Dialog(context,style);
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
            if(cancelOnTouchOutside){
                dialog.setOnCancelListener(this);
                dialog.setCancelable(true);
            }else{
                dialog.setCancelable(cancelable);
            }
            return dialog;
        }

        @Override
        public void hidelWait() {
            if(waitDialog!=null){
                waitDialog.dismiss();
                waitDialog = null;
            }
            Alert.builder = null;
        }

        private Dialog waitDialog;


        private Dialog createDialog(){
            final Dialog dialog = initDialog(R.style.jzoom_alert_dialog,R.layout.alert_dialog_content);

            if(buttons==null){
                throw new NullPointerException("buttons is null");
            }
            if(buttons.length < 1 || buttons.length > 2){
                throw new RuntimeException("按钮的数量必须为1或者2");
            }
            ViewGroup container = dialog.findViewById(R.id.cus_dialog_container);

            TextView titlteView = dialog.findViewById(R.id.cus_dialog_title);
            titlteView.setText(title==null ? content : title);
            //按钮

            if(buttons.length==2){
                View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_buttons_ok_cancel,null);
                container.addView(view);
                Button cancel = view.findViewById(R.id.cancel);
                cancel.setText(buttons[0]);
                cancel.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        if(alertListener!=null){
                            alertListener.onAlert(Alert.CANCEL);
                            alertListener = null;
                        }
                        Alert.builder = null;
                        dialog.cancel();
                    }
                });
                Button ok = view.findViewById(R.id.ok);
                ok.setText(buttons[1]);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(alertListener!=null){
                            alertListener.onAlert(Alert.OK);
                            alertListener = null;
                        }
                        Alert.builder = null;
                        dialog.cancel();
                    }
                });

                if(styles!=null){
                    if(styles.length >= 1){
                        setButtonStyle(context,ok,styles[0]);
                    }
                    if(styles.length >= 2){
                        setButtonStyle(context,ok,styles[1]);
                    }
                }
            }else{
                Button view = (Button) LayoutInflater.from(context).inflate(R.layout.alert_dialog_button,null);
                container.addView(view);
                view.setId(R.id.ok);
                view.setText(buttons[0]);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(alertListener!=null){
                            alertListener.onAlert(Alert.OK);
                            alertListener = null;
                        }
                        Alert.builder = null;
                        dialog.cancel();
                    }
                });
                if(styles!=null){
                    if(styles.length >= 1){
                        setButtonStyle(context,view,styles[0]);
                    }
                }

            }
            return dialog;

        }



        private void popup(View contentView,AlertListener listener){
            alertListener = listener;
            final Dialog dialog = createDialog();
            if(contentView!=null){
                ViewGroup container = dialog.findViewById(R.id.alert_dialog_content);
                container.addView(contentView);
            }
            dialog.show();
        }

        private SelectListener listener;

        public <T> void select(final Collection<T> selectData,int selectedIndex,  final SelectListener listener){
            if(finishing(context))return;
            final ListView listView = new ListView(context);
            final String[] options = new String[selectData.size()];
            int index = 0;
            for (T data : selectData) {
                options[index++] = data.toString();
            }
            listView.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_single_choice, options));
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            if (selectedIndex >= 0) {
                listView.setItemChecked(selectedIndex, true);
            }

            popup(listView,new AlertListener() {
                @Override
                public void onAlert(int buttonId) {
                    if(buttonId==Alert.OK){
                        int position = listView.getCheckedItemPosition();
                        if(position>=0 && position < selectData.size()){
                            listener.onSelect(buttonId, position );;
                        }else{
                            listener.onSelect(Alert.CANCEL,position);
                        }
                    }else{
                        listener.onSelect(Alert.CANCEL,-1);
                    }
                }
            });
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if(alertListener!=null){
                alertListener.onAlert(R.id.cancel);
                alertListener = null;
            }
            Alert.builder = null;
        }

        public AlertBuilder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }
    }


    private static void setButtonStyle(Context context,Button btn,int style){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn.setTextAppearance(style);
        }else {
            btn.setTextAppearance(context,style);
        }
    }

    public static interface AlertBuilder{
        /**
         * 设置标题
         * @param title
         * @return
         */
        AlertBuilder setTitle(String title);

        /**
         * 内容
         * @param content
         * @return
         */
        AlertBuilder setContent(String content);

        /**
         * 设置按钮
         * @param buttons
         * @return
         */
        AlertBuilder setButtons(String...buttons);

        /**
         * 设置样式
         * @param styles
         * @return
         */
        AlertBuilder setButtonStyles(int...styles);

        /**
         * 能否被取消
         * @param cancelable
         * @return
         */
        AlertBuilder setCancelable(boolean cancelable);

        /**
         * 在外部点击一下，是否触发消失
         * @param cancelOnOutside
         * @return
         */
        AlertBuilder setCancelOnTouchOutside(boolean cancelOnOutside);

        /**
         * 弹出框
         * @param listener
         */
        void alert(AlertListener listener);




        /**
         * 展示等待框
         */
        void showWait();


        /**
         * 等待框消失
         */
        void hidelWait();


        <T> void select(Collection<T> data, int selectedIndex, SelectListener listener);
    }

    /**
     * 填写一个东西
     * @param context
     * @param title
     * @param placeHodler
     * @param listener
     */
    public static void input(Context context,String title,String placeHodler,InputListener listener){



    }


    /**
     * 选择框
     * @param context
     * @param title             标题
     * @param data              用于选择的数据
     * @param selectedIndex     当前选中下标
     * @param listener          选择监听
     * @param <T>
     */
    public static <T> void select(Context context, String title, Collection<T> data,int selectedIndex,SelectListener listener){

        builder(context).setCancelOnTouchOutside(true).setButtons(context.getString(R.string.ok)).setTitle(title).select(data,selectedIndex,listener);
    }


    /**
     * toast
     * @param context
     * @param title
     */
    public static void toast(Context context, String title){
        Toast.makeText(context,title,Toast.LENGTH_SHORT).show();
    }

    /**
     * 等待框
     * @param context
     * @param title
     */
    public static void showWait(Context context,String title){

        builder(context).setCancelOnTouchOutside(false).setCancelable(false).setTitle(title).showWait();
    }

    private static boolean finishing(Context conte){
        if(conte instanceof Activity)return ((Activity)conte).isFinishing();
        return false;
    }

    /**
     * 取消等待
     */
    public static void hidelWait(){
        if(builder!=null){
            builder.hidelWait();
        }
    }

    private static AlertBuilder builder;


    public static AlertBuilder builder(Context context){
        if(builder==null){
            builder = new AlertBuilderImpl(context);
        }
        return builder;
    }



}
