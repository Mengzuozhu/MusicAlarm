package com.example.randomalarm.alarm;

/**
 * author : Mzz
 * date : 2019 2019/5/9 19:34
 * description :
 */
public class AlarmSongInfo {

    private Long id;
    private String playedSongPath;
    private int duration;

    public AlarmSongInfo(Long id, String playedSongPath, int duration) {
        this.id = id;
        this.playedSongPath = playedSongPath;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayedSongPath() {
        return playedSongPath;
    }

    public void setPlayedSongPath(String playedSongPath) {
        this.playedSongPath = playedSongPath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
