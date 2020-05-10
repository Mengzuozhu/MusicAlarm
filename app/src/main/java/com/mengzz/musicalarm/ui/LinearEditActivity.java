package com.mengzz.musicalarm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.contract.EditContract;
import com.mengzz.musicalarm.edit.EditItemInfo;
import com.mengzz.musicalarm.presenter.EditPresenter;
import com.mzz.zandroidcommon.view.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearEditActivity extends BaseActivity implements EditContract.View {

    public static final String EDIT_DATA = "EDIT_DATA";
    public static final String DELETE_NUM = "DELETE_NUM";
    public static final int EDIT_SAVE = 3;
    @BindView(R.id.rv_edit)
    RecyclerView rvEdit;
    EditContract.Presenter editPresenter;

    /**
     * Start for result.
     *
     * @param activity the activity
     * @param value    the value
     */
    public static void startForResult(FragmentActivity activity, ArrayList <?
            extends Parcelable> value) {
        Intent intent =
                new Intent(activity, LinearEditActivity.class).putParcelableArrayListExtra(EDIT_DATA, value);
        activity.startActivityForResult(intent, EDIT_SAVE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        ButterKnife.bind(this);

        ArrayList <EditItemInfo> editData = getParcelableArrayListExtra(EDIT_DATA);
        init(editData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_delete_all, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            save();
            this.finish();
            return true;
        } else if (itemId == R.id.action_delete_all) {
            editPresenter.deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(ArrayList <EditItemInfo> editData) {
        BaseQuickAdapter adapter =
                new BaseQuickAdapter <EditItemInfo, BaseViewHolder>(R.layout.item_linear_edit,
                        editData) {
                    @Override
                    protected void convert(BaseViewHolder helper, EditItemInfo item) {
                        helper.setText(R.id.tv_edit_name, item.getInfo());
                        helper.addOnClickListener(R.id.iv_edit_name_del);
                    }
                };
        rvEdit.setLayoutManager(new LinearLayoutManager(this));
        rvEdit.setAdapter(adapter);
        editPresenter = new EditPresenter(this, editData, adapter);
        editPresenter.setDeleteAlarmListener();
    }

    private void save() {
        Intent intent = getIntent().putIntegerArrayListExtra(DELETE_NUM,
                editPresenter.getDeleteIds());
        setResult(EDIT_SAVE, intent);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
