package com.example.user.guitarlessons;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

/**
 * Created by user on 06.03.2018.
 */

public class FilterLayout extends FrameLayout implements RadioGroup.OnCheckedChangeListener {

    private FilterListener listener;
    private RadioGroup radioGroup;

    public void setListener(FilterListener listener) {
        this.listener = listener;
    }

    public FilterLayout(@NonNull Context context) {
        super(context);
    }

    public FilterLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FilterLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.filter_layout, this, true);
        radioGroup = view.findViewById(R.id.filters);
        radioGroup.setOnCheckedChangeListener(this);
    }

    public void refreshData() {
        if (listener != null) {
            onCheckedChanged(radioGroup, radioGroup.getCheckedRadioButtonId());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (listener!=null) {
            switch (i) {
                case R.id.radio_all:
                    listener.onAllClicked();
                    break;
                case R.id.radio_chords:
                    listener.onChordsClicked();
                    break;
                case R.id.radio_tabs:
                    listener.onTabsClicked();
                    break;
            }
        }
    }


    public interface FilterListener {
        void onAllClicked();

        void onChordsClicked();

        void onTabsClicked();

    }
}
