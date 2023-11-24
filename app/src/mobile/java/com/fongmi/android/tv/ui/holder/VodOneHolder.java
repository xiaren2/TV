package com.github.tvbox.gongjin.ui.holder;

import androidx.annotation.NonNull;

import com.github.tvbox.gongjin.bean.Vod;
import com.github.tvbox.gongjin.databinding.AdapterVodOneBinding;
import com.github.tvbox.gongjin.ui.adapter.VodAdapter;
import com.github.tvbox.gongjin.ui.base.BaseVodHolder;
import com.github.tvbox.gongjin.utils.ImgUtil;

public class VodOneHolder extends BaseVodHolder {

    private final VodAdapter.OnClickListener listener;
    private final AdapterVodOneBinding binding;

    public VodOneHolder(@NonNull AdapterVodOneBinding binding, VodAdapter.OnClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;
    }

    @Override
    public void initView(Vod item) {
        binding.name.setText(item.getVodName());
        binding.site.setText(item.getSiteName());
        binding.remark.setText(item.getVodRemarks());
        binding.site.setVisibility(item.getSiteVisible());
        binding.remark.setVisibility(item.getRemarkVisible());
        binding.getRoot().setOnClickListener(v -> listener.onItemClick(item));
        binding.getRoot().setOnLongClickListener(v -> listener.onLongClick(item));
        ImgUtil.rect(item.getVodName(), item.getVodPic(), binding.image);
    }
}
