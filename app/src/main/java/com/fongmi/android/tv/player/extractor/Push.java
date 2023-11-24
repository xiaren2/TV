package com.github.tvbox.gongjin.player.extractor;

import android.os.SystemClock;

import com.github.tvbox.gongjin.App;
import com.github.tvbox.gongjin.player.Source;
import com.github.tvbox.gongjin.ui.activity.VideoActivity;

public class Push implements Source.Extractor {

    @Override
    public boolean match(String scheme, String host) {
        return scheme.equals("push");
    }

    @Override
    public String fetch(String url) throws Exception {
        VideoActivity.start(App.activity(), url.substring(7));
        SystemClock.sleep(500);
        return "";
    }

    @Override
    public void stop() {
    }

    @Override
    public void exit() {
    }
}
