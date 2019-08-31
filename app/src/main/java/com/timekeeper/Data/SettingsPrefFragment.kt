package com.timekeeper.Data

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.timekeeper.R

class SettingsPrefFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}