package com.github.tvbox.gongjin.ui.base;

import android.view.View;

import androidx.leanback.widget.Presenter;

import com.github.tvbox.gongjin.bean.Vod;

public abstract class BaseVodHolder extends Presenter.ViewHolder {

    public BaseVodHolder(View view) {
        super(view);
    }

    public abstract void initView(Vod item);
}
