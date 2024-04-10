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
    private static int yes;
    private static int pik;
    private static int good;
    private static int buttonRestart;

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
        pik = soundPool.load(context, R.raw.geiger1, 1);
        yes = soundPool.load(context, R.raw.yes, 1);
        good = soundPool.load(context, R.raw.good, 1);
        buttonRestart = soundPool.load(context, R.raw.button7, 1);
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

    public static void playYes() {
        play(yes);
    }

    public static void playPik() {
        play(pik);
    }

    public static void playRestartButton() {
        play(buttonRestart);
    }

    public static void playGood() {
        play(good);
    }

    public static void randomAccessOrAccepted() {
        int r = (int)(Math.random()*100);

        if (r % 3 == 0) {
            playAccepted();
        } else if (r % 3 == 1){
            playAccess();
        } else {
            playYes();
        }
    }

    private static void play(int sound) {
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }
}
