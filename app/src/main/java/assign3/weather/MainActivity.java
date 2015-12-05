package assign3.weather;


import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.sql.SQLOutput;

import assign3.weather.customViews.DejaVUSansTextView;
import assign3.weather.fragments.PlaceholderFragment;
import assign3.weather.fragments.TwelveDayFragment;


public class MainActivity extends AppCompatActivity   {

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
    private String[] cityTitles={"თბილისი","ბათუმი","ქუთაისი","თელავი","ზუგდიდი"};
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

        System.out.println("########### "+context.getResources().getIdentifier("w01d","drawable",context.getPackageName()));
        System.out.println("###########* "+R.drawable.w01d);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        /**
         * create drawer button
         */
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        mDrawerToggle.syncState();
        /**end */
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, cityTitles));
        final Bundle bundle=new Bundle();
        //default city
        bundle.putString("name", "tbilisi");
        bundle.putString("cityName", "თბილისი");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),bundle);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener(){
           @Override
           public void onItemClick(AdapterView parent, View view, int position, long id) {
               mDrawerList.setItemChecked(position, true);
               mDrawerLayout.closeDrawer(mDrawerList);
               switch (position){
                   case 0:
                       bundle.putString("name", "tbilisi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 1:
                       bundle.putString("name", "batumi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 2:
                       bundle.putString("name", "kutaisi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 3:
                       bundle.putString("name", "telavi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 4:
                       bundle.putString("name", "zugdidi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
               }
               /*Thread thread = new Thread(new Runnable(){
                   @Override
                   public void run() {
                       MainActivity.this.runOnUiThread(new Runnable() {

                           @Override
                           public void run() {
                               mSectionsPagerAdapter.notifyDataSetChanged();
                           }
                       });
                   }
               });

               thread.start();*/
               mSectionsPagerAdapter.notifyDataSetChanged();
               //set Fragmentclass Arguments

           }
       });



    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

*/
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final Bundle fragmentBundle;

        public SectionsPagerAdapter(FragmentManager fm,Bundle data) {
            super(fm);
            fragmentBundle=data;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    PlaceholderFragment mainFragment=new PlaceholderFragment();

                    mainFragment.setArguments(this.fragmentBundle);
                    return mainFragment;
                case 1:
                    TwelveDayFragment twelveDayFragment=new TwelveDayFragment();
                    twelveDayFragment.setArguments(this.fragmentBundle);
                    return twelveDayFragment;
            }
           return null;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "7 დღის";
                case 1:
                    return "14 დღის";

            }
            return null;
        }
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public static Context getContext()
    {
        return context;
    }


}
