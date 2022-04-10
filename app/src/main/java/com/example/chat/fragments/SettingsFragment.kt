package com.example.chat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.chat.R
import com.example.chat.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)

        loadSettings()

        val preference = findPreference<Preference>("sign_out")
        preference?.setOnPreferenceClickListener {
            signOut()
            true
        }
    }

    private fun loadSettings() {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val theme = sharedPreference.getString("theme", "")
        changeTheme(theme ?: "")
    }

    private fun changeTheme(theme: String) {
        if(theme == "light")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(context, LoginActivity::class.java))
        activity?.finish()
    }

}