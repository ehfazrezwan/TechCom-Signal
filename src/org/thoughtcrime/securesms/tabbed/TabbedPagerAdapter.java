package org.thoughtcrime.securesms.tabbed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.thoughtcrime.securesms.TabbedChatFragment;
import org.thoughtcrime.securesms.TabbedFragmentContacts;

public class TabbedPagerAdapter extends FragmentStatePagerAdapter {

    public TabbedPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                return new TabbedChatFragment();
            case 1:
                return new TabbedFragmentContacts();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int i) {
        switch(i) {
            case 0:
                return "Chats";
            case 1:
                return "Contacts";
            default:
                return null;
        }
    }
}
