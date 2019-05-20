package com.example.randomalarm.base;

import com.example.randomalarm.base.BasePresenter;

/**
 * author : Mzz
 * date : 2019 2019/4/27 18:35
 * description :
 */
public interface BaseView<P extends BasePresenter> {
    void setPresenter(P presenter);
}