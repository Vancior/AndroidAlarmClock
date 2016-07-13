package com.vancior.deskclock.bean;

/**
 * Created by H on 2016/7/13.
 */
public class AudioInfo {

    private String ringName;
    private String audioPath;
    private int duration;

    AudioInfo(String ringName, String audioPath, int duration) {
        this.ringName = ringName;
        this.audioPath = audioPath;
        this.duration = duration;
    }

    public String getRingName() {
        return ringName;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public int getDuration() {
        return duration;
    }
}
