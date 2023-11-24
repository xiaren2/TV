package com.github.tvbox.gongjin.ui.holder;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.tvbox.gongjin.bean.Vod;
import com.github.tvbox.gongjin.databinding.AdapterVodListBinding;
import com.github.tvbox.gongjin.ui.adapter.VodAdapter;
import com.github.tvbox.gongjin.ui.base.BaseVodHolder;
import com.github.tvbox.gongjin.utils.ImgUtil;

public class VodListHolder extends BaseVodHolder {

    private final VodAdapter.OnClickListener listener;
    private final AdapterVodListBinding binding;

    public VodListHolder(@NonNull AdapterVodListBinding binding, VodAdapter.OnClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    @Override
    public void initView(Vod item) {
        binding.name.setText(item.getVodName());
        binding.remark.setText(item.getVodRemarks());
        binding.name.setVisibility(item.getNameVisible());
        binding.remark.setVisibility(item.getRemarkVisible());
        binding.getRoot().setOnClickListener(v -> listener.onItemClick(item));
        binding.getRoot().setOnLongClickListener(v -> listener.onLongClick(item));
        ImgUtil.load(item.getVodName(), item.getVodPic(), binding.image, ImageView.ScaleType.FIT_CENTER, false);
    }
}
