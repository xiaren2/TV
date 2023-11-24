package com.github.tvbox.gongjin.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.tvbox.gongjin.Constant;
import com.github.tvbox.gongjin.api.LiveParser;
import com.github.tvbox.gongjin.bean.Channel;
import com.github.tvbox.gongjin.bean.Group;
import com.github.tvbox.gongjin.bean.Live;
import com.github.tvbox.gongjin.player.Source;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LiveViewModel extends ViewModel {

    private static final int LIVE = 1;
    private static final int CHANNEL = 2;

    public MutableLiveData<Channel> channel;
    public MutableLiveData<Live> live;
    public ExecutorService executor;

    public LiveViewModel() {
        this.channel = new MutableLiveData<>();
        this.live = new MutableLiveData<>();
    }

    public void getLive(Live item) {
        execute(LIVE, () -> {
            LiveParser.start(item);
            verify(item);
            return item;
        });
    }

    private void verify(Live item) {
        Iterator<Group> iterator = item.getGroups().iterator();
        while (iterator.hasNext()) if (iterator.next().isEmpty()) iterator.remove();
    }

    public void fetch(Channel item) {
        execute(CHANNEL, () -> {
            Source.get().stop();
            item.setUrl(Source.get().fetch(item));
            return item;
        });
    }

    private void execute(int type, Callable<?> callable) {
        if (executor != null) executor.shutdownNow();
        executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> {
            try {
                if (Thread.interrupted()) return;
                if (type == LIVE) live.postValue((Live) executor.submit(callable).get(Constant.TIMEOUT_LIVE, TimeUnit.MILLISECONDS));
                if (type == CHANNEL) channel.postValue((Channel) executor.submit(callable).get(Constant.TIMEOUT_PARSE_LIVE, TimeUnit.MILLISECONDS));
            } catch (Throwable e) {
                if (e instanceof InterruptedException || Thread.interrupted()) return;
                if (type == LIVE) live.postValue(new Live());
                if (type == CHANNEL) channel.postValue(new Channel());
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCleared() {
        if (executor != null) executor.shutdownNow();
    }
}
