package com.github.tvbox.gongjin.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.viewbinding.ViewBinding;

import com.github.tvbox.gongjin.R;
import com.github.tvbox.gongjin.databinding.ActivityCrashBinding;
import com.github.tvbox.gongjin.ui.base.BaseActivity;

import java.util.Objects;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

public class CrashActivity extends BaseActivity {

    private ActivityCrashBinding mBinding;

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityCrashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEvent() {
        mBinding.details.setOnClickListener(v -> showError());
        mBinding.restart.setOnClickListener(v -> CustomActivityOnCrash.restartApplication(this, Objects.requireNonNull(CustomActivityOnCrash.getConfigFromIntent(getIntent()))));
    }

    private void showError() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.crash_details_title)
                .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent()))
                .setPositiveButton(R.string.crash_details_close, null)
                .show();
    }
}
