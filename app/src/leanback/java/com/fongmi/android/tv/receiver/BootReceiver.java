package com.github.tvbox.gongjin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.cast.dlna.dmr.DLNARendererService;
import com.github.tvbox.gongjin.App;
import com.github.tvbox.gongjin.R;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DLNARendererService.Companion.start(App.get(), R.drawable.ic_logo);
    }
}
