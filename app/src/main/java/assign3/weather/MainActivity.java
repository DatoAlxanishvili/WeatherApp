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


import com.readystatesoftware.systembartint.SystemBarTintManager;

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
    private String[] cityTitles={"თბილისი","ბათუმი","ქუთაისი",
            "თელავი","ზუგდიდი","გორი","მცხეთა","ახალციხე","ბორჯომი","ბაკურიანი",
    "კასპი","ხაშური","მარნეული","ფოთი","სენაკი","ზესტაფონი","ქობულეთი","რუსთავი","გურჯაანი","საგარეჯო","ყვარელი","ლაგოდეხი",
            "წინანდალი","ახმეტა","თიანეთი","ჟინვალი","ფასანაური","ახალგორი","ცხინვალი","მანგლისი","წალკა","ბოლნისი","გარდაბანი",
    "სურამი","წაღვერი","ასპინძა","ნინოწმინდა","ახალქალაქი","აბასთუმანი","ადიგენი","ხარაგაული","ტყიბული","ონი","ამბროლაური","წყალტუბო",
    "ხონი","ვანი","სამტრედია","აბაშა","ხობი","წალენჯიხა","ლენტეხი","მესტია","ურეკი","მახინჯაური","გალი","ოჩამჩირე","ტყვარჩელი","სოხუმი",
    "ბიჭვინთა"};
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
               mDrawerLayout.closeDrawers();
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
                   case 5:
                       bundle.putString("name", "gori");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 6:
                       bundle.putString("name", "mtskheta");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 7:
                       bundle.putString("name", "akhaltsikhe");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 8:
                       bundle.putString("name", "borjomi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 9:
                       bundle.putString("name", "bakuriani");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 10:
                       bundle.putString("name", "kaspi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 11:
                       bundle.putString("name", "khashuri");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 12:
                       bundle.putString("name", "marneuli");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 13:
                       bundle.putString("name", "poti");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 14:
                       bundle.putString("name", "senaki");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 15:
                       bundle.putString("name", "zestaponi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 16:
                       bundle.putString("name", "kobuleti");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 17:
                       bundle.putString("name", "rustavi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 18:
                       bundle.putString("name", "gurjaani");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 19:
                       bundle.putString("name", "sagarejo");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 20:
                       bundle.putString("name", "qvareli");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 21:
                       bundle.putString("name", "lagodekhi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 22:
                       bundle.putString("name", "tsinandali");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 23:
                       bundle.putString("name", "akhmeta");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 24:
                       bundle.putString("name", "tianeti");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 25:
                       bundle.putString("name", "zhinvali");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 26:
                       bundle.putString("name", "pasanauri");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 27:
                       bundle.putString("name", "akhalgori");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 28:
                       bundle.putString("name", "tskhinvali");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 29:
                       bundle.putString("name", "manglisi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 30:
                       bundle.putString("name", "tsalka");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 31:
                       bundle.putString("name", "bolnisi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 32:
                       bundle.putString("name", "gardabani");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 33:
                       bundle.putString("name", "surami");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 34:
                       bundle.putString("name", "tsaghveri");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 35:
                       bundle.putString("name", "aspindza");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 36:
                       bundle.putString("name", "ninotsminda");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 37:
                       bundle.putString("name", "akhalkalaki");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 38:
                       bundle.putString("name", "abastumani");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 39:
                       bundle.putString("name", "adigeni");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 40:
                       bundle.putString("name", "kharagauli");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 41:
                       bundle.putString("name", "tqibuli");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 42:
                       bundle.putString("name", "oni");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 43:
                       bundle.putString("name", "ambrolauri");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 44:
                       bundle.putString("name", "tsqaltubo");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 45:
                       bundle.putString("name", "khoni");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 46:
                       bundle.putString("name", "vani");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 47:
                       bundle.putString("name", "samtredia");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 48:
                       bundle.putString("name", "abasha");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 49:
                       bundle.putString("name", "khobi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 50:
                       bundle.putString("name", "tsalenjikha");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 51:
                       bundle.putString("name", "lentekhi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 52:
                       bundle.putString("name", "mestia");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 53:
                       bundle.putString("name", "ureki");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 54:
                       bundle.putString("name", "makhinjauri");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 55:
                       bundle.putString("name", "gali");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 56:
                       bundle.putString("name", "ochamchire");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 57:
                       bundle.putString("name", "tqvarcheli");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 58:
                       bundle.putString("name", "sokhumi");
                       bundle.putString("cityName",cityTitles[position]);
                       break;
                   case 59:
                       bundle.putString("name", "bichvinta");
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



}
