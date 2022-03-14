package com.chiibeii.ZiYanZiYu.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import com.chiibeii.ZiYanZiYu.R

class SettingPreferenceFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference,rootKey)
    }
}