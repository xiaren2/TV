package com.github.tvbox.gongjin.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;

import com.github.tvbox.gongjin.api.LiveConfig;
import com.github.tvbox.gongjin.bean.Live;
import com.github.tvbox.gongjin.databinding.DialogLiveBinding;
import com.github.tvbox.gongjin.impl.LiveCallback;
import com.github.tvbox.gongjin.ui.adapter.LiveAdapter;
import com.github.tvbox.gongjin.ui.custom.SpaceItemDecoration;
import com.github.tvbox.gongjin.utils.ResUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LiveDialog implements LiveAdapter.OnClickListener {

    private final DialogLiveBinding binding;
    private final LiveCallback callback;
    private final LiveAdapter adapter;
    private final AlertDialog dialog;

    public static LiveDialog create(Activity activity) {
        return new LiveDialog(activity);
    }

    public LiveDialog(Activity activity) {
        this.adapter = new LiveAdapter(this);
        this.callback = (LiveCallback) activity;
        this.binding = DialogLiveBinding.inflate(LayoutInflater.from(activity));
        this.dialog = new MaterialAlertDialogBuilder(activity).setView(binding.getRoot()).create();
    }

    public void show() {
        setRecyclerView();
        setDialog();
    }

    private void setRecyclerView() {
        binding.recycler.setAdapter(adapter);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(1, 16));
        binding.recycler.setLayoutManager(new GridLayoutManager(dialog.getContext(), 1));
        binding.recycler.post(() -> binding.recycler.scrollToPosition(LiveConfig.getHomeIndex()));
    }

    private void setDialog() {
        if (adapter.getItemCount() == 0) return;
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (ResUtil.getScreenWidth() * 0.4f);
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setDimAmount(0);
        dialog.show();
    }

    @Override
    public void onItemClick(Live item) {
        callback.setLive(item);
        dialog.dismiss();
    }
}
