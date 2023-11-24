package com.github.tvbox.gongjin.ui.holder;

import androidx.annotation.NonNull;

import com.github.tvbox.gongjin.bean.Vod;
import com.github.tvbox.gongjin.databinding.AdapterVodOvalBinding;
import com.github.tvbox.gongjin.ui.base.BaseVodHolder;
import com.github.tvbox.gongjin.ui.presenter.VodPresenter;
import com.github.tvbox.gongjin.utils.ImgUtil;

public class VodOvalHolder extends BaseVodHolder {

    private final VodPresenter.OnClickListener listener;
    private final AdapterVodOvalBinding binding;

    public VodOvalHolder(@NonNull AdapterVodOvalBinding binding, VodPresenter.OnClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    public VodOvalHolder size(int[] size) {
        binding.image.getLayoutParams().width = size[0];
        binding.image.getLayoutParams().height = size[1];
        return this;
    }

    @Override
    public void initView(Vod item) {
        binding.name.setText(item.getVodName());
        binding.name.setVisibility(item.getNameVisible());
        binding.getRoot().setOnClickListener(v -> listener.onItemClick(item));
        binding.getRoot().setOnLongClickListener(v -> listener.onLongClick(item));
        ImgUtil.oval(item.getVodName(), item.getVodPic(), binding.image);
    }
}
