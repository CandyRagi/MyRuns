package cam.candy.myruns2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup

class FragmentC : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Use your XML preferences file
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceScreen?.let { removeIconSpaceRecursively(it) }


        // User Profile → new activity
        findPreference<Preference>("user_profile")?.setOnPreferenceClickListener {
            startActivity(Intent(requireContext(), UserProfileActivity::class.java))
            true
        }

        // Webpage → open browser
        findPreference<Preference>("webpage")?.setOnPreferenceClickListener {
            val url = "https://www.sfu.ca/computing.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            true
        }
    }
    private fun removeIconSpaceRecursively(pref: Preference) {
        pref.isIconSpaceReserved = false
        if (pref is PreferenceGroup) {
            for (i in 0 until pref.preferenceCount) {
                removeIconSpaceRecursively(pref.getPreference(i))
            }
        }
    }

}
