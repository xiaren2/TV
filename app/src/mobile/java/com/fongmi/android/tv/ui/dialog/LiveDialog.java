package com.github.tvbox.gongjin.ui.dialog;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.github.tvbox.gongjin.api.LiveConfig;
import com.github.tvbox.gongjin.bean.Live;
import com.github.tvbox.gongjin.databinding.DialogLiveBinding;
import com.github.tvbox.gongjin.impl.LiveCallback;
import com.github.tvbox.gongjin.ui.adapter.LiveAdapter;
import com.github.tvbox.gongjin.ui.custom.SpaceItemDecoration;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LiveDialog implements LiveAdapter.OnClickListener {

    private final LiveCallback callback;
    private DialogLiveBinding binding;
    private LiveAdapter adapter;
    private AlertDialog dialog;

    public static LiveDialog create(Activity activity) {
        return new LiveDialog(activity);
    }

    public static LiveDialog create(Fragment fragment) {
        return new LiveDialog(fragment);
    }

    public LiveDialog(Activity activity) {
        this.callback = (LiveCallback) activity;
        init(activity);
    }

    public LiveDialog(Fragment fragment) {
        this.callback = (LiveCallback) fragment;
        init(fragment.getActivity());
    }

    private void init(Activity activity) {
        this.binding = DialogLiveBinding.inflate(LayoutInflater.from(activity));
        this.dialog = new MaterialAlertDialogBuilder(activity).setView(binding.getRoot()).create();
        this.adapter = new LiveAdapter(this);
    }

    public void show() {
        setRecyclerView();
        setDialog();
    }

    private void setRecyclerView() {
        binding.recycler.setAdapter(adapter);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(1, 8));
        binding.recycler.post(() -> binding.recycler.scrollToPosition(LiveConfig.getHomeIndex()));
    }

    private void setDialog() {
        if (adapter.getItemCount() == 0) return;
        dialog.getWindow().setDimAmount(0);
        dialog.show();
    }

    @Override
    public void onItemClick(Live item) {
        callback.setLive(item);
        dialog.dismiss();
    }
}
