package com.rishabh.github.instaimagedown.floatingbutton;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.rishabh.github.instaimagedown.R;



public class FloatingViewSettingsFragment extends PreferenceFragment {

    public static FloatingViewSettingsFragment newInstance() {
        final FloatingViewSettingsFragment fragment = new FloatingViewSettingsFragment();
        return fragment;
    }

    public FloatingViewSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preference);
    }

}
