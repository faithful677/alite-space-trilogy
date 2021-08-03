package com.capstone.challengeweek.Media;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.Build;

import com.capstone.challengeweek.R;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int laserSound, foodSound, enemyHit, loseSound, healthSound;
    private AudioAttributes audioAttributes;
    private AudioTrack audioTrack;

    public SoundPlayer(Context context){
        // SoundPool is deprecated
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
             soundPool = new SoundPool.Builder()
                     .setAudioAttributes(audioAttributes)
                     .setMaxStreams(2)
                     .build();

        }
        else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        //load sounds from raw folder
        laserSound = soundPool.load(context, R.raw.laser_sound, 1);
        loseSound = soundPool.load(context, R.raw.lose_sound,  1);
        enemyHit = soundPool.load(context, R.raw.enemy_exit, 1);
        foodSound = soundPool.load(context, R.raw.food_sound,  1);
        healthSound = soundPool.load(context, R.raw.health_sound,  1);
    }



    public void playLaserSound() {
        soundPool.play(laserSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLoseSound() {
        soundPool.play(loseSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playEnemyHitSound() {
        soundPool.play(enemyHit, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playFoodSound() {
        soundPool.play(foodSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playHealthSound() {
        soundPool.play(healthSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
