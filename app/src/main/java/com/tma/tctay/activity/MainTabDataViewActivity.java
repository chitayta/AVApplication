package com.tma.tctay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tma.tctay.android.UserSessionManager;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.tabfragment.CommandPlaceholderFragment;
import com.tma.tctay.tabfragment.DashboardPlaceholderFragment;
import com.tma.tctay.tabfragment.DataPlaceholderFragment;
import com.tma.tctay.tabfragment.SystemPlaceholderFragment;

public class MainTabDataViewActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String systemUid;
    public AccessToken accessToken;

    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab_data_view);

        session = new UserSessionManager(getApplicationContext());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        Intent callerIntent = getIntent();
        //có intent rồi thì lấy Bundle dựa vào MyPackage
        systemUid = callerIntent.getStringExtra("systemUid");
        String access_token = callerIntent.getStringExtra("access_token");
        String token_type = callerIntent.getStringExtra("token_type");
        String refresh_token = callerIntent.getStringExtra("refresh_token");
        Integer expires_in = callerIntent.getIntExtra("expires_in",0);
        accessToken = new AccessToken(access_token, token_type, refresh_token, expires_in);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), accessToken, systemUid);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_viewpaper);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //tabLayout.addTab(R.id.tabItem);

//        tabLayout.getTabAt(0).setIcon(R.drawable.system_tab_icon);
//        tabLayout.getTabAt(1).setIcon(R.drawable.data_tab_icon);
//        tabLayout.getTabAt(2).setIcon(R.drawable.command_tab_icon);
//        tabLayout.getTabAt(3).setIcon(R.drawable.dashboard_tab_icon);


        //TextView textView = (TextView) findViewById(R.id.textViewData);
        //String access_token = accessTokenBundle.getString("access_token");
        //textView.setText("Access Token:" + access_token);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //dataWaitingProgressBar = (ProgressBar) findViewById(R.id.dataWaitingProgressBar);
        //tabLayoutView = (View) findViewById(R.id.main_content);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab_data_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id)
        {
            case R.id.action_refreshToken:
                Toast.makeText(getApplicationContext(), "Selected Refresh Token menu item!",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_signOut:
                session.logoutUser();
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private AccessToken accessToken;
        private String systemUid;

        public SectionsPagerAdapter(FragmentManager fm, AccessToken accessToken, String systemUid) {
            super(fm);
            this.accessToken = accessToken;
            this.systemUid = systemUid;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            //return DataPlaceholderFragment.newInstance(position + 1, accessToken);
            switch (position) {
                case 0:
                    return SystemPlaceholderFragment.newInstance(position + 1, accessToken, systemUid);
                case 1:
                    return DataPlaceholderFragment.newInstance(position + 1, accessToken, systemUid);
                case 2:
                    return CommandPlaceholderFragment.newInstance(position + 1, accessToken, systemUid);
                case 3:
                    return DashboardPlaceholderFragment.newInstance(position + 1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SYSTEM";
//                case 2:
//                    return "COMMAND";
//                case 3:
//                    return "DASHBOARD";
//                case 1:
//                    return "DATA";
//            };
//            return null;
//        }
    }
}
