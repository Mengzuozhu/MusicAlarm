package com.example.randomalarm.setting;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.example.randomalarm.common.ViewerHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

/**
 * author : Mzz
 * date : 2019 2019/5/19 17:04
 * description :
 */
public class MatisseHelper {

    public static final int REQUEST_CODE_CHOOSE = 23;

    public static void showMatisse(@NonNull FragmentActivity activity, int requestCode) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                initMatisse(activity, requestCode);
            } else {
                ViewerHelper.showToast(activity, "无权限访问");
            }
        });
    }

    private static void initMatisse(@NonNull FragmentActivity activity, int requestCode) {
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }
}
