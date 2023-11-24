package com.github.tvbox.gongjin.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.github.tvbox.gongjin.App;
import com.github.tvbox.gongjin.R;
import com.github.tvbox.gongjin.Updater;
import com.github.tvbox.gongjin.api.ApiConfig;
import com.github.tvbox.gongjin.api.LiveConfig;
import com.github.tvbox.gongjin.api.WallConfig;
import com.github.tvbox.gongjin.bean.Config;
import com.github.tvbox.gongjin.databinding.ActivityMainBinding;
import com.github.tvbox.gongjin.db.AppDatabase;
import com.github.tvbox.gongjin.event.RefreshEvent;
import com.github.tvbox.gongjin.event.ServerEvent;
import com.github.tvbox.gongjin.impl.Callback;
import com.github.tvbox.gongjin.player.Source;
import com.github.tvbox.gongjin.server.Server;
import com.github.tvbox.gongjin.ui.base.BaseActivity;
import com.github.tvbox.gongjin.ui.custom.FragmentStateManager;
import com.github.tvbox.gongjin.ui.fragment.SettingFragment;
import com.github.tvbox.gongjin.ui.fragment.SettingPlayerFragment;
import com.github.tvbox.gongjin.ui.fragment.VodFragment;
import com.github.tvbox.gongjin.utils.FileChooser;
import com.github.tvbox.gongjin.utils.Notify;
import com.github.tvbox.gongjin.utils.UrlUtil;
import com.google.android.material.navigation.NavigationBarView;
import com.permissionx.guolindev.PermissionX;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding mBinding;
    private FragmentStateManager mManager;
    private boolean confirm;

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkAction(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initFragment(savedInstanceState);
        Updater.get().release().start();
        Server.get().start();
        initConfig();
    }

    @Override
    protected void initEvent() {
        mBinding.navigation.setOnItemSelectedListener(this);
    }

    private void checkAction(Intent intent) {
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            VideoActivity.push(this, intent.getStringExtra(Intent.EXTRA_TEXT));
        } else if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            if ("text/plain".equals(intent.getType()) || UrlUtil.path(intent.getData()).endsWith(".m3u")) {
                loadLive("file:/" + FileChooser.getPathFromUri(this, intent.getData()));
            } else {
                VideoActivity.push(this, intent.getData().toString());
            }
        }
    }

    private void initFragment(Bundle savedInstanceState) {
        mManager = new FragmentStateManager(mBinding.container, getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) return VodFragment.newInstance();
                if (position == 1) return SettingFragment.newInstance();
                if (position == 2) return SettingPlayerFragment.newInstance();
                return null;
            }
        };
        if (savedInstanceState == null) mManager.change(0);
    }

    private void initConfig() {
        WallConfig.get().init();
        LiveConfig.get().init();
        ApiConfig.get().init().load(getCallback());
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void success() {
                checkAction(getIntent());
                RefreshEvent.config();
                RefreshEvent.video();
            }

            @Override
            public void error(String msg) {
                if (TextUtils.isEmpty(msg) && AppDatabase.getBackupKey().exists()) onRestore();
                else RefreshEvent.empty();
                RefreshEvent.config();
                Notify.show(msg);
            }
        };
    }

    private void onRestore() {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).request((allGranted, grantedList, deniedList) -> AppDatabase.restore(new Callback() {
            @Override
            public void success() {
                initConfig();
            }
        }));
    }

    private void loadLive(String url) {
        LiveConfig.load(Config.find(url, 1), new Callback() {
            @Override
            public void success() {
                openLive();
            }
        });
    }

    private void setNavigation() {
        mBinding.navigation.getMenu().findItem(R.id.vod).setVisible(true);
        mBinding.navigation.getMenu().findItem(R.id.setting).setVisible(true);
        mBinding.navigation.getMenu().findItem(R.id.live).setVisible(LiveConfig.hasUrl());
    }

    private boolean openLive() {
        LiveActivity.start(this);
        return false;
    }

    private void setConfirm() {
        confirm = true;
        Notify.show(R.string.app_exit);
        App.post(() -> confirm = false, 5000);
    }

    public void change(int position) {
        mManager.change(position);
    }

    @Override
    public void onRefreshEvent(RefreshEvent event) {
        super.onRefreshEvent(event);
        if (event.getType().equals(RefreshEvent.Type.CONFIG)) setNavigation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServerEvent(ServerEvent event) {
        if (event.getType() != ServerEvent.Type.PUSH) return;
        VideoActivity.push(this, event.getText());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (mBinding.navigation.getSelectedItemId() == item.getItemId()) return false;
        if (item.getItemId() == R.id.vod) return mManager.change(0);
        if (item.getItemId() == R.id.setting) return mManager.change(1);
        if (item.getItemId() == R.id.live) return openLive();
        return false;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        RefreshEvent.video();
    }

    @Override
    public void onBackPressed() {
        if (!mBinding.navigation.getMenu().findItem(R.id.vod).isVisible()) {
            setNavigation();
        } else if (mManager.isVisible(2)) {
            change(1);
        } else if (mManager.isVisible(1)) {
            mBinding.navigation.setSelectedItemId(R.id.vod);
        } else if (mManager.canBack(0)) {
            if (!confirm) setConfirm();
            else finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WallConfig.get().clear();
        LiveConfig.get().clear();
        ApiConfig.get().clear();
        AppDatabase.backup();
        Source.get().exit();
        Server.get().stop();
    }
}