package com.example.churm.lectorlibro.fragments;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.example.churm.lectorlibro.R;
public class PreferenciasFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
