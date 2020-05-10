package com.mengzz.musicalarm.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mengzz.musicalarm.R;
import com.mzz.zandroidcommon.view.BaseActivity;

/**
 * The type Remind image preview activity.
 */
public class RemindImagePreviewActivity extends BaseActivity {

    public static final String REMIND_IMAGE_PATH = "REMIND_IMAGE_PATH";
    private String remindImagePath;

    public static void startNewActivity(FragmentActivity activity, String value) {
        Intent intent =
                new Intent(activity, RemindImagePreviewActivity.class).putExtra(REMIND_IMAGE_PATH
                        , value);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        remindImagePath = getIntent().getStringExtra(REMIND_IMAGE_PATH);
        showImage(remindImagePath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_image_refresh) {
            showImage(remindImagePath);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showImage(String remindImagePath) {
        String remark = "备注示例";
        RemindFragment remindFragment = RemindFragment.newInstance("铃声", remindImagePath, remark);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_remind_image,
                remindFragment).commit();
    }
}
