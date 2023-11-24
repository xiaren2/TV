package com.github.tvbox.gongjin.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.tvbox.gongjin.bean.Vod;

public abstract class BaseVodHolder extends RecyclerView.ViewHolder {

    public BaseVodHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void initView(Vod item);
}
