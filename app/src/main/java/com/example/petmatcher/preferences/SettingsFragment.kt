package com.example.petmatcher.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.petmatcher.R

/**
 * Preferences screen that includes user preferences for app behavior and adoptable pets.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
