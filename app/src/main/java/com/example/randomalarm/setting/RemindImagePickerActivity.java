package com.example.randomalarm.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.randomalarm.R;
import com.example.randomalarm.common.MatisseHelper;
import com.example.randomalarm.common.ViewerHelper;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.ButterKnife;

public class RemindImagePickerActivity extends AppCompatActivity {

    private AppSetting appSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        appSetting = AppSetting.readSetting(this);
        showImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:
                save();
                this.finish();
                return true;
            case R.id.action_image_select:
                MatisseHelper.showMatisse(this, MatisseHelper.REQUEST_CODE_CHOOSE);
                return true;
            case R.id.action_image_default:
                appSetting.setRemindImagePath("");
                showImage();
                return true;
            case R.id.action_image_refresh:
                showImage();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        appSetting.applySetting(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MatisseHelper.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List <String> result = Matisse.obtainPathResult(data);
            if (result != null && !result.isEmpty()) {
                appSetting.setRemindImagePath(result.get(0));
                Log.w("image", appSetting.getRemindImagePath());
                showImage();
            }
        }
    }

    private void showImage() {
        String remindImagePath = appSetting.getRemindImagePath();
        RemindFragment remindFragment = RemindFragment.newInstance("铃声", remindImagePath);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_remind_image, remindFragment).commit();
    }
}
