package com.abg.wordfinder;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundManager {
    private static SoundPool soundPool;
    private static int buttonSettings;
    private static int buttonSwitch;
    private static int buttonBell;
    private static int buttonCock;
    private static int buttonReset;
    private static int accepted;
    private static int access;

    public void setUpSounds(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(attributes).build();
        buttonSettings = soundPool.load(context, R.raw.button3, 1);
        buttonSwitch = soundPool.load(context, R.raw.spark3, 1);
        buttonBell = soundPool.load(context, R.raw.bell1, 1);
        buttonCock = soundPool.load(context, R.raw.cock1, 1);
        buttonReset = soundPool.load(context, R.raw.tu_ping, 1);
        accepted = soundPool.load(context, R.raw.accepted, 1);
        access = soundPool.load(context, R.raw.access, 1);
    }

    public static void playSettings() {
        play(buttonSettings);
    }

    public static void playSwitch() {
        play(buttonSwitch);
    }

    public static void playBell() {
        play(buttonBell);
    }

    public static void playCock() {
        play(buttonCock);
    }

    public static void playReset() {
        play(buttonReset);
    }

    public static void playAccepted() {
        play(accepted);
    }

    public static void playAccess() {
        play(access);
    }

    public static void randomAccessOrAccepted() {
        if ((int)(Math.random()*100) % 2 == 0) {
            playAccepted();
        } else {
            playAccess();
        }
    }

    private static void play(int sound) {
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }
}
