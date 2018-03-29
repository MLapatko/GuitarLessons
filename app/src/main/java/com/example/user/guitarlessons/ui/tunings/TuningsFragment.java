package com.example.user.guitarlessons.ui.tunings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.user.guitarlessons.base.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.TuningsManager;
import com.example.user.guitarlessons.model.Tuning;

import java.util.List;

/**
 * Created by user on 27.03.2018.
 */

public class TuningsFragment extends BaseFragment implements NumberPicker.OnValueChangeListener {

    public static TuningsFragment newInstance() {

        Bundle args = new Bundle();

        TuningsFragment fragment = new TuningsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private NumberPicker mPicker;
    private TextView firstStr;
    private TextView secondStr;
    private TextView thirdStr;
    private TextView fourthStr;
    private TextView firthStr;
    private TextView sixthStr;
    private List<Tuning> mTunings;
    private ViewSwitcher mViewSwitcher;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.tunings_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewSwitcher=view.findViewById(R.id.tunings_view_switcher);
        mViewSwitcher.setDisplayedChild(0);

        mPicker = view.findViewById(R.id.tunings_picker);
        mPicker.setOnValueChangedListener(this);
        mPicker.setMinValue(0);

        firstStr = view.findViewById(R.id.first_string);
        secondStr = view.findViewById(R.id.second_string);
        thirdStr = view.findViewById(R.id.third_string);
        fourthStr = view.findViewById(R.id.fourth_string);
        firthStr = view.findViewById(R.id.fifth_string);
        sixthStr = view.findViewById(R.id.sixth_string);

        getTunings(getContext(), R.raw.turnings);

    }

    public void getTunings(Context context, int docId) {
        TuningsManager.getInstance().getTunings(context, docId, new TuningsManager.TuningsListener() {

            @Override
            public void onSuccess(List<Tuning> response) {
                mTunings = response;
                String[] tuningsNames = new String[response.size()];
                for (int i = 0; i < response.size(); i++) {
                    Tuning tuning = response.get(i);
                    tuningsNames[i] = tuning.getName();
                }
                mPicker.setMaxValue(response.size() - 1);
                mPicker.setDisplayedValues(tuningsNames);
                if (mTunings != null) {
                    setTextViewText(mTunings.get(0));
                }
            }

            @Override
            public void onError(Throwable e) {
                mViewSwitcher.setDisplayedChild(1);
            }
        });
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        if (mTunings != null) {
            setTextViewText(mTunings.get(i1));
        }
    }

    private void setTextViewText(Tuning tuning) {
        firstStr.setText(tuning.getSt1());
        secondStr.setText(tuning.getSt2());
        thirdStr.setText(tuning.getSt3());
        fourthStr.setText(tuning.getSt4());
        firthStr.setText(tuning.getSt5());
        sixthStr.setText(tuning.getSt6());
    }

}
