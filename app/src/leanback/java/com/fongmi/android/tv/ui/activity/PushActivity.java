package com.github.tvbox.gongjin.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.github.tvbox.gongjin.R;
import com.github.tvbox.gongjin.databinding.ActivityPushBinding;
import com.github.tvbox.gongjin.server.Server;
import com.github.tvbox.gongjin.ui.base.BaseActivity;
import com.github.tvbox.gongjin.utils.QRCode;
import com.github.tvbox.gongjin.utils.ResUtil;
import com.github.tvbox.gongjin.utils.Sniffer;
import com.github.tvbox.gongjin.utils.Util;

public class PushActivity extends BaseActivity {

    private ActivityPushBinding mBinding;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, PushActivity.class));
    }

    @Override
    protected ViewBinding getBinding() {
        return mBinding = ActivityPushBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        String address = Server.get().getAddress();
        mBinding.code.setImageBitmap(QRCode.getBitmap(address, 250, 1));
        mBinding.info.setText(ResUtil.getString(R.string.push_info, address));
        mBinding.clip.setOnClickListener(this::onClip);
    }

    private void onClip(View view) {
        CharSequence text = Util.getClipText();
        if (!TextUtils.isEmpty(text)) VideoActivity.start(this, Sniffer.getUrl(text.toString()), false);
    }
}
