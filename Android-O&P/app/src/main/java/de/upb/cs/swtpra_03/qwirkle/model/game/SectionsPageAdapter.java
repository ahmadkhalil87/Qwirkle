package de.upb.cs.swtpra_03.qwirkle.model.game;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.upb.cs.swtpra_03.qwirkle.R;
import de.upb.cs.swtpra_03.qwirkle.view.ChatFragment;
import de.upb.cs.swtpra_03.qwirkle.view.HandsFragment;

/**
 * Handles the tabs UI Elements that switch between Chat and Hands Fragments
 */
public class SectionsPageAdapter extends FragmentPagerAdapter {

    Fragment[] fragments = new Fragment[2];
    CharSequence[] titles = new CharSequence[2];

    /**
     * Constructor
     *
     * @param fm        FragmentManager
     * @param activity  parent activity
     */
    public SectionsPageAdapter(FragmentManager fm, Activity activity) {
        super(fm);

        fragments[0] = new ChatFragment();
        fragments[1] = new HandsFragment();

        titles[0] = activity.getResources().getString(R.string.chat);
        titles[1] = activity.getResources().getString(R.string.hands);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
