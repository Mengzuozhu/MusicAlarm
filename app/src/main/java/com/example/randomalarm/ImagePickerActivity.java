package com.example.randomalarm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.randomalarm.setting.MatisseHelper;
import com.zhihu.matisse.Matisse;

import java.util.List;

public class ImagePickerActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        MatisseHelper.showMatisse(this, REQUEST_CODE_CHOOSE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List <Uri> result = Matisse.obtainResult(data);
            for (Uri uri : result) {
                Log.w("image", uri.toString());
            }
        }
    }
}
