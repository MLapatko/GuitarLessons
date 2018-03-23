package com.example.user.guitarlessons.metronomeScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;

/**
 * Created by user on 22.02.2018.
 */

public class MetronomeFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    public static MetronomeFragment newInstance() {

        Bundle args = new Bundle();

        MetronomeFragment fragment = new MetronomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SeekBar mSeekBar;
    private ImageButton mPlayButton;
    private ImageButton mPlusButton;
    private ImageButton mMinusButton;
    private TextView mBpmTextView;
    private static final int SEEKBAR_MIN = 20;
    private int mCurrentProgress = SEEKBAR_MIN;
    private double pause = 0;
    private Metronome mMetronome;
    private boolean isPlaying = false;
    private Spinner mBeatSpinner;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.metronome_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSeekBar = view.findViewById(R.id.seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);

        mPlayButton = view.findViewById(R.id.play_button);
        mPlayButton.setImageResource(R.drawable.ic_music_player_play);
        mPlayButton.setOnClickListener(this);

        mPlusButton=view.findViewById(R.id.plus_button);
        mPlusButton.setOnClickListener(this);

        mMinusButton=view.findViewById(R.id.minus_button);
        mMinusButton.setOnClickListener(this);

        mBpmTextView = view.findViewById(R.id.bpm);

        mBeatSpinner=view.findViewById(R.id.spinner);

        mMetronome = new Metronome();
        mMetronome.setBpm(mCurrentProgress);
        mMetronome.setBeat(Integer.valueOf(mBeatSpinner.getSelectedItem().toString()));
        mMetronome.setBeatSound(2440);
        mMetronome.setSound(6440);

        mBeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mMetronome.setBeat(Integer.valueOf(adapterView.getItemAtPosition(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        mCurrentProgress = i + SEEKBAR_MIN;
        updateBpmTextView();
        //pause = (60000 - 16.8 * mCurrentProgress) / mCurrentProgress;
        mMetronome.setBpm(mCurrentProgress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isPlaying) {
            mMetronome.stop();
        }
    }

    @Override
    public void onClick(View view) {
        mPlayButton.setEnabled(false);
        switch (view.getId()) {
            case R.id.play_button:
                if (isPlaying) {
                    mMetronome.stop();
                    isPlaying = false;
                    mPlayButton.setImageResource(R.drawable.ic_music_player_play);

                } else {
                    isPlaying = true;
                    mPlayButton.setImageResource(R.drawable.ic_pause_button);
                   mMetronome.play();
                }
                break;
            case R.id.plus_button:
                mCurrentProgress+=1;
                mSeekBar.setProgress(mCurrentProgress-SEEKBAR_MIN);
                updateBpmTextView();
                break;
            case R.id.minus_button:
                mCurrentProgress-=1;
                mSeekBar.setProgress(mCurrentProgress-SEEKBAR_MIN);
                updateBpmTextView();
                break;
        }
        mPlayButton.setEnabled(true);
    }
    private void updateBpmTextView(){
        mBpmTextView.setText(String.valueOf(mCurrentProgress));
    }

}
