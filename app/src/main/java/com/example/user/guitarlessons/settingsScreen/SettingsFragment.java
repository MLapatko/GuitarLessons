package com.example.user.guitarlessons.settingsScreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;

import com.example.user.guitarlessons.R;

/**
 * Created by user on 15.03.2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static SettingsFragment newInstance() {
        
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mLanguage;
    public final static String LANGUAGE = "languageList";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case LANGUAGE:
                mLanguage = sharedPreferences.getString(getString(R.string.languageList), "");
                if (!TextUtils.isEmpty(mLanguage)) {
                    SettingsHelper.setLanguage(getContext(), mLanguage);
                    SettingsHelper.saveLanguage(getContext(), mLanguage);
                    if (getActivity() instanceof LanguageChangeListener){
                        ((LanguageChangeListener) getActivity()).onLanguageChanged();
                    }
                    getActivity().recreate();
                }
                break;
        }
    }
}
