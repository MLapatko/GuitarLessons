package com.example.user.guitarlessons.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.TextUtils;

import com.example.user.guitarlessons.R;

import java.util.Locale;

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
        ListPreference langList = (ListPreference) findPreference(getString(R.string.languageList));
        langList.setValue(Locale.getDefault().getLanguage());
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
                mLanguage = sharedPreferences.getString(getString(R.string.languageList), "ru");
                if (!TextUtils.isEmpty(mLanguage)) {
                    SettingsHelper.setLanguage(getContext(), mLanguage);
                    SettingsHelper.saveLanguage(getContext(), mLanguage);
                    if (getActivity() instanceof LanguageChangeListener) {
                        ((LanguageChangeListener) getActivity()).onLanguageChanged();
                    }
                    if (getActivity() != null) {
                        getActivity().recreate();
                    }
                }
                break;
        }
    }

    public interface LanguageChangeListener {
        void onLanguageChanged();
    }
}
