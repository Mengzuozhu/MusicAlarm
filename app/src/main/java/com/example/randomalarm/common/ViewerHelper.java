package com.example.randomalarm.common;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.widget.CheckBox;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

public class ViewerHelper {

    /**
     * 显示提示消息
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 在左上角显示返回按钮
     *
     * @param actionBar
     */
    public static void displayHomeAsUp(ActionBar actionBar) {
        if (actionBar != null) {
            //取消显示标题
//            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void setOnItemClickWithCheckBox(BaseQuickAdapter baseAdapter, @IdRes int id) {
        baseAdapter.setOnItemClickListener((adapter, view, position) -> {
            CheckBox checkBox = view.findViewById(id);
            checkBox.performClick();
        });
    }

}
