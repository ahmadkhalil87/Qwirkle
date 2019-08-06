package de.upb.cs.swtpra_03.qwirkle.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

import de.upb.cs.swtpra_03.qwirkle.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        //J.Required empty public constructor
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
            String rootKey) {
        //J. Set preferences.xml as the Fragment resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
