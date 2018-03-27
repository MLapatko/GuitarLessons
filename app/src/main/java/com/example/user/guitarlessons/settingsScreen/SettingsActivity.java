package com.example.user.guitarlessons.settingsScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.example.user.guitarlessons.BaseActivity;
import com.example.user.guitarlessons.R;

/**
 * Created by user on 26.03.2018.
 */

public class SettingsActivity extends BaseActivity implements LanguageChangeListener {

    public static final String LANGUAGE_STATUS="language status";
    private boolean isLanguageChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null){
            isLanguageChange=savedInstanceState.getBoolean(LANGUAGE_STATUS,false);
        }

        setBackButtonStatus(true);
        setToolbarTitle(getString(R.string.settings));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.settings_screen, SettingsFragment.newInstance());
        ft.commit();
    }

    @Override
    protected int getToolBarId() {
        return R.id.tool_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.settings_activity;
    }

    @Override
    public void onLanguageChanged() {
        isLanguageChange = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                returnResult();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        returnResult();
        super.onBackPressed();
    }

    private void returnResult(){
        Intent i=new Intent();
        i.putExtra(LANGUAGE_STATUS,isLanguageChange);
        setResult(RESULT_OK,i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(LANGUAGE_STATUS,isLanguageChange);
    }
}
