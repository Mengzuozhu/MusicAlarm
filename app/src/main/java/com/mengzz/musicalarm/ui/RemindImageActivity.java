package com.mengzz.musicalarm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.edit.EditItemInfo;
import com.mengzz.musicalarm.image.RemindImage;
import com.mengzz.musicalarm.setting.AppSetting;
import com.mzz.zandroidcommon.adapter.CheckableAndDraggableAdapter;
import com.mzz.zandroidcommon.common.MatisseHelper;
import com.mzz.zandroidcommon.view.BaseActivity;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemindImageActivity extends BaseActivity {

    public static final int SPAN_COUNT = 3;
    @BindView(R.id.rv_remind_image)
    SwipeRecyclerView rvRemindImage;
    private CheckableAndDraggableAdapter<RemindImage> adapter;
    private MenuItem changedModeItem;
    private List<RemindImage> remindImagePaths;
    private AppSetting appSetting;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_select_all, menu);
        getMenuInflater().inflate(R.menu.menu_image_change_mode, menu);
        changedModeItem = menu.findItem(R.id.action_image_change_mode);
        changedModeItem.setTitle(appSetting.getImageChangedMode().getDesc());
        return true;
    }

    public void save() {
        appSetting.applySetting(this);
    }

    @OnClick(R.id.fab_image_add)
    public void addImageOnClick(View view) {
        MatisseHelper.showMatisse(this, MatisseHelper.REQUEST_CODE_CHOOSE);
    }

    @OnClick(R.id.fab_image_edit)
    public void editImageOnClick(View view) {
        ArrayList<EditItemInfo> editData = new ArrayList<>();
        for (int i = 0; i < remindImagePaths.size(); i++) {
            String filePath = remindImagePaths.get(i).getFilePath();
            editData.add(new EditItemInfo(i, filePath));
        }
        GridEditActivity.startForResult(this, editData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_image);
        ButterKnife.bind(this);

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                this.finish();
                return true;
            case R.id.action_select_all:
                adapter.selectAll(true);
                break;
            case R.id.action_image_change_mode:
                changedModeItem.setTitle(appSetting.setNextImageChangedMode().getDesc());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MatisseHelper.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<String> result = Matisse.obtainPathResult(data);
            if (result != null && !result.isEmpty()) {
                for (String s : result) {
                    remindImagePaths.add(new RemindImage(s, true));
                }
                adapter.setNewData(remindImagePaths);
            }
        } else if (resultCode == GridEditActivity.EDIT_SAVE) {
            saveEdit(data);
        }
    }

    private void init() {
        appSetting = AppSetting.readSetting(this);
        remindImagePaths = appSetting.getRemindImagePaths();
        adapter = new CheckableAndDraggableAdapter<RemindImage>(R.layout.item_image,
                remindImagePaths, rvRemindImage) {
            private static final int CHB_REMIND_ID = R.id.chb_remind;

            @Override
            protected void convert(BaseViewHolder helper, RemindImage remindImage) {
                File file = new File(remindImage.getFilePath());
                if (!file.exists()) {
                    return;
                }
                helper.setChecked(CHB_REMIND_ID, remindImage.isChecked()).addOnClickListener(CHB_REMIND_ID);
                ImageView view = helper.getView(R.id.iv_remind_image);
                Glide.with(RemindImageActivity.this).load(file).into(view);
            }

            @Override
            protected int getCheckableViewId() {
                return CHB_REMIND_ID;
            }
        };
        adapter.setOnItemClickListener((adapter1, view, position) -> preview(position));
        rvRemindImage.setLongPressDragEnabled(true);
        setOnItemMoveListener();
        rvRemindImage.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
        rvRemindImage.setAdapter(adapter);
    }

    private void setOnItemMoveListener() {
        rvRemindImage.setOnItemMoveListener(new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder,
                                      RecyclerView.ViewHolder targetHolder) {
                int fromPosition = srcHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(remindImagePaths, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(remindImagePaths, i, i - 1);
                    }
                }

                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                // Do nothing
            }
        });
    }

    private void saveEdit(Intent data) {
        ArrayList<Integer> deleteNumbers =
                data.getIntegerArrayListExtra(GridEditActivity.DELETE_NUM);
        if (deleteNumbers == null) {
            return;
        }
        deleteNumbers.sort(Comparator.reverseOrder());
        //从大索引处开始删除
        for (int i = 0; i < deleteNumbers.size(); i++) {
            int num = deleteNumbers.get(i);
            remindImagePaths.remove(num);
        }
        adapter.setNewData(remindImagePaths);
    }

    private void preview(int position) {
        RemindImage remindImage = (RemindImage) adapter.getItem(position);
        if (remindImage != null) {
            RemindImagePreviewActivity.startNewActivity(this, remindImage.getFilePath());
        }
    }

}
