package rbhasin.reasforecast;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


/**
 * The main  weather activity that hosts the view pager and navigation drawer.
 */
public class WeatherActivity extends ActionBarActivity implements ActionBar.TabListener {


    SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<String> fragments = new ArrayList<>();
    Context context;
    ViewPager mViewPager;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    Menu optionsMenu;
    EditText zipcodeField;
    String zipcode = null;
    AlertDialog.Builder builder;
    AlertDialog zipcodeAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_weather);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //If this activity was started from the settings activity, retrieve the new zipcode.
        Intent intent = getIntent();
        zipcode = intent.getStringExtra(SettingsActivity.EXTRA_MESSAGE);

        //If we don't currently have a zipcode, prompt the user to provide one.
        if (zipcode == null || zipcode == "") {
            LayoutInflater li = LayoutInflater.from(context);
            View zipcodePrompt = li.inflate(R.layout.zipcode_prompt, null);

            builder = new AlertDialog.Builder(context);
            builder.setView(zipcodePrompt);

            zipcodeField = (EditText) zipcodePrompt.findViewById(R.id.zipcode);

            builder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    zipcode = zipcodeField.getText().toString();
                                    dialog.dismiss();

                                    //Refresh fragments to populate data
                                    for(int i =0 ; i < mSectionsPagerAdapter.getCount(); i ++) {
                                        Fragment f = mSectionsPagerAdapter.getItem(i);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .detach(f)
                                                .attach(f)
                                                .commit();
                                    }
                                }
                            });

            zipcodeAlert = builder.create();
            zipcodeAlert.show();
        }

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Set up nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, new String[] {getString(R.string.settings)}));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle (
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        fragments.add(CurrentWeatherFragment.class.getName());
        fragments.add(ThreeDayWeatherFragment.class.getName());
        fragments.add(HourlyWeatherFragment.class.getName());

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected (int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return  super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_refresh:
                for(int i =0 ; i < mSectionsPagerAdapter.getCount(); i ++) {
                    Fragment f = mSectionsPagerAdapter.getItem(i);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .detach(f)
                            .attach(f)
                            .commit();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        switch (position){
            case 0: {

                //Starting a new activity causes the app to crash because the state of the fragment cannot be saved
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
        }
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i =0; i<mSectionsPagerAdapter.getCount(); i++) {
            Fragment f = mSectionsPagerAdapter.getItem(i);
            if (f.isAdded()) {
                getSupportFragmentManager().putFragment(outState, mSectionsPagerAdapter.getPageTitle(i).toString(), f);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> frags = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
            frags = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(context, frags.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
