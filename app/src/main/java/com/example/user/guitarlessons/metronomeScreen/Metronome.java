package com.example.user.guitarlessons.metronomeScreen;

/**
 * Created by user on 22.03.2018.
 */

public class Metronome {

    private double bpm;
    private int beat;
    private int silenceLength;
    private double beatSound;
    private double sound;
    private static int tickLength = 1000;
    private static int SAMPLE_RATE = 8000;
    private boolean play = true;
    private AudioGenerator audioGenerator;

    public Metronome() {
        audioGenerator = new AudioGenerator(SAMPLE_RATE);
    }

    private void calcSilence() {
        this.silenceLength = (int) (((60 / bpm) * SAMPLE_RATE) - tickLength);
    }

    public void play() {
        play = true;
        audioGenerator.createPlayer();

        new Thread(new Runnable() {

            @Override
            public void run() {

                double[] mTickSound =
                        audioGenerator.getSineWave(tickLength, SAMPLE_RATE, beatSound);
                double[] mTockSound =
                        audioGenerator.getSineWave(tickLength, SAMPLE_RATE, sound);
                double[] sound = new double[SAMPLE_RATE];
                int t = 0, s = 0, b = 0;
                do {
                    for (int i = 0; i < sound.length && play; i++) {
                        if (t < tickLength) {
                            if (b == 0)
                                sound[i] = mTockSound[t];
                            else
                                sound[i] = mTickSound[t];
                            t++;
                        } else {
                            sound[i] = 0;
                            s++;
                            if (s >= silenceLength) {
                                t = 0;
                                s = 0;
                                b++;
                                if (b > (beat - 1))
                                    b = 0;
                            }
                        }
                    }
                    audioGenerator.writeSound(sound);
                } while (play);
            }
        }).start();
    }

    public void stop() {
        play = false;
        audioGenerator.destroyAudioTrack();
    }

    public void setBeat(int beat) {
        this.beat = beat;
    }

    public void setBeatSound(double beatSound) {
        this.beatSound = beatSound;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
        calcSilence();
    }


    public void setSound(double sound) {
        this.sound = sound;
    }
}
