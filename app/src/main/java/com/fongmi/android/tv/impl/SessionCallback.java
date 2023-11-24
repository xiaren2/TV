package com.github.tvbox.gongjin.impl;

import android.support.v4.media.session.MediaSessionCompat;

import com.github.tvbox.gongjin.event.ActionEvent;
import com.github.tvbox.gongjin.player.Players;

public class SessionCallback extends MediaSessionCompat.Callback {

    private final Players players;

    public static SessionCallback create(Players players) {
        return new SessionCallback(players);
    }

    private SessionCallback(Players players) {
        this.players = players;
    }

    @Override
    public void onSeekTo(long pos) {
        players.seekTo(pos, true);
    }

    @Override
    public void onPlay() {
        ActionEvent.send(ActionEvent.PLAY);
    }

    @Override
    public void onPause() {
        ActionEvent.send(ActionEvent.PAUSE);
    }

    @Override
    public void onSkipToPrevious() {
        ActionEvent.send(ActionEvent.PREV);
    }

    @Override
    public void onSkipToNext() {
        ActionEvent.send(ActionEvent.NEXT);
    }
}
