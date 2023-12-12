package com.chiibeii.ZiYanZiYu.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.chiibeii.ZiYanZiYu.R

class SettingPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        val outTypePreference: ListPreference? = findPreference("key_output_type")

//        outTypePreference?.summaryProvider =
//            Preference.SummaryProvider<ListPreference> { preference ->
//                val text = preference.entry
//                if (TextUtils.isEmpty(text)) {
//                    "选择格式"
//                } else {
//                    "将导出为$text"
//                }
//            }

    }


}