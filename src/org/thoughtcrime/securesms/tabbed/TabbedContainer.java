package org.thoughtcrime.securesms.tabbed;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.thoughtcrime.securesms.PassphraseRequiredActionBarActivity;
import org.thoughtcrime.securesms.R;

public class TabbedContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_container);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabbedPagerAdapter tabbedPagerAdapter = new TabbedPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabbedPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
