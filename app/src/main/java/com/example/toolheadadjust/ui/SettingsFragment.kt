package com.example.toolheadadjust.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.toolheadadjust.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}