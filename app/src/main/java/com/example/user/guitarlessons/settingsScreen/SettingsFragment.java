package com.example.user.guitarlessons.settingsScreen;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.user.guitarlessons.R;

/**
 * Created by user on 15.03.2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_fragment,rootKey);
    }

}
