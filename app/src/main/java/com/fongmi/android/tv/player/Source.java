package com.github.tvbox.gongjin.player;

import com.github.tvbox.gongjin.bean.Channel;
import com.github.tvbox.gongjin.bean.Result;
import com.github.tvbox.gongjin.player.extractor.BiliBili;
import com.github.tvbox.gongjin.player.extractor.Force;
import com.github.tvbox.gongjin.player.extractor.JianPian;
import com.github.tvbox.gongjin.player.extractor.Push;
import com.github.tvbox.gongjin.player.extractor.TVBus;
import com.github.tvbox.gongjin.player.extractor.Thunder;
import com.github.tvbox.gongjin.player.extractor.Youtube;
import com.github.tvbox.gongjin.player.extractor.ZLive;
import com.github.tvbox.gongjin.utils.UrlUtil;

import java.util.ArrayList;
import java.util.List;

public class Source {

    private final List<Extractor> extractors;

    private static class Loader {
        static volatile Source INSTANCE = new Source();
    }

    public static Source get() {
        return Loader.INSTANCE;
    }

    public Source() {
        extractors = new ArrayList<>();
        extractors.add(new BiliBili());
        extractors.add(new Force());
        extractors.add(new JianPian());
        extractors.add(new Push());
        extractors.add(new Thunder());
        extractors.add(new TVBus());
        extractors.add(new Youtube());
        extractors.add(new ZLive());
    }

    private Extractor getExtractor(String url) {
        String host = UrlUtil.host(url);
        String scheme = UrlUtil.scheme(url);
        for (Extractor extractor : extractors) if (extractor.match(scheme, host)) return extractor;
        return null;
    }

    public String fetch(Result result) throws Exception {
        String url = result.getUrl().v();
        Extractor extractor = getExtractor(url);
        if (extractor != null) result.setParse(0);
        return extractor == null ? url : extractor.fetch(url);
    }

    public String fetch(Channel channel) throws Exception {
        String url = channel.getCurrent().split("\\$")[0];
        Extractor extractor = getExtractor(url);
        return extractor == null ? url : extractor.fetch(url);
    }

    public void stop() {
        if (extractors == null) return;
        for (Extractor extractor : extractors) extractor.stop();
    }

    public void exit() {
        if (extractors == null) return;
        for (Extractor extractor : extractors) extractor.exit();
    }

    public interface Extractor {

        boolean match(String scheme, String host);

        String fetch(String url) throws Exception;

        void stop();

        void exit();
    }
}
