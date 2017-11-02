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

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

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

    public static AlertDialog dialog(Context context) {
        return new AlertDialogImpl(context);
    }


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
         * @param buttonId  OK|CANCEL YES|NO
         * @param content 填写内容
         */
        void onInput(int buttonId,String content);
    }


    public interface SelectListener{
        /**
         * 选择了列表中的一个内容
         * @param buttonId      OK|CANCEL YES|NO
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
        if(finishing(context))return;
        new AlertDialogImpl(context).setButtons(context.getString(R.string.ok)).setTitle(title).setContent(content).setAlertListener(listener).show();
    }

    /**
     * 通知
     * @param content
     * @param listener
     */
    public static void alert(Context context,String content,AlertListener listener){
        if(finishing(context))return;
        new AlertDialogImpl(context).setButtons(context.getString(R.string.ok)).setTitle(content).setAlertListener(listener).show();
    }


    /**
     * 确认对话框
     * @param title
     * @param content
     * @param listener
     */
    public static void confirm(Context context,String title,String content,AlertListener listener){
        if(finishing(context))return;
        new AlertDialogImpl(context).setButtons(context.getString(R.string.cancel),context.getString(R.string.ok))
                .setTitle(title).setContent(content).setAlertListener(listener).show();
    }



    /**
     * 确认对话框
     * @param content
     * @param listener
     */
    public static void confirm(Context context,String content,AlertListener listener){
        new AlertDialogImpl(context).setButtons(context.getString(R.string.cancel),context.getString(R.string.ok))
                .setTitle(content).setAlertListener(listener).show();
    }






    /**
     * 填写
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
     * @param selectData              用于选择的数据
     * @param selectedIndex     当前选中下标
     * @param listener          选择监听
     * @param <T>
     */
    public static <T> void select(Context context, String title, final List<T> selectData, int selectedIndex, final SelectListener listener){
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

        new AlertDialogImpl(context).setTitle(title).setButtons(context.getString(R.string.ok)).setContentView(listView).setAlertListener(new AlertListener() {
            @Override
            public void onAlert(int buttonId) {
                if(buttonId==Alert.OK){
                    int position = listView.getCheckedItemPosition();
                    if(position>=0 && position < selectData.size()){
                        listener.onSelect(Alert.OK, position );;
                    }else{
                        listener.onSelect(Alert.CANCEL,position);
                    }
                }else{
                    listener.onSelect(Alert.CANCEL,-1);
                }
            }
        }).show();


    }


    /**
     * toast
     * @param context
     * @param title
     */
    public static void toast(Context context, String title){
        Toast.makeText(context,title,Toast.LENGTH_SHORT).show();
    }

    static WaitDialogImpl waitDialog;

    /**
     * 等待框
     * @param context
     * @param title
     */
    public static void showWait(Context context,String title){
        if(finishing(context))return;
        if(waitDialog==null){
            waitDialog = new WaitDialogImpl(context);
            waitDialog.show();
        }
        waitDialog.setText(title);

    }

    private static boolean finishing(Context conte){
        if(conte instanceof Activity)return ((Activity)conte).isFinishing();
        return false;
    }

    /**
     * 取消等待
     */
    public static void hidelWait(){
        if(waitDialog!=null){
            waitDialog.dismiss();
            waitDialog = null;
        }
    }



}
