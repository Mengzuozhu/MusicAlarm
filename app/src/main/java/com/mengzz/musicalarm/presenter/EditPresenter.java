package com.mengzz.musicalarm.presenter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mengzz.musicalarm.contract.EditContract;
import com.mengzz.musicalarm.edit.EditItemInfo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import tyrantgit.explosionfield.ExplosionField;

/**
 * author : Mzz
 * date : 2019 2019/5/26 16:06
 * description :
 */
public class EditPresenter implements EditContract.Presenter {
    @Getter
    private ArrayList <Integer> deleteIds = new ArrayList <>();
    private List <EditItemInfo> editData;
    private BaseQuickAdapter adapter;
    private ExplosionField explosionField;

    public EditPresenter(EditContract.View view, List <EditItemInfo> editData,
                         BaseQuickAdapter adapter) {
        this.editData = editData;
        this.adapter = adapter;
        explosionField = ExplosionField.attach2Window(view.getActivity());
    }

    @Override
    public void deleteAll() {
        for (EditItemInfo editItemInfo : editData) {
            deleteIds.add(editItemInfo.getId());
        }
        adapter.setNewData(new ArrayList());
    }

    @Override
    public void setDeleteAlarmListener() {
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            EditItemInfo item = (EditItemInfo) adapter1.getItem(position);
            if (item == null) {
                return;
            }
            explosionField.explode(view);
            view.setOnClickListener(null);
            deleteIds.add(item.getId());
            adapter1.remove(position);
        });
    }

}
