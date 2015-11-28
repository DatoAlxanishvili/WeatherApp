package assign3.weather;

import android.app.ProgressDialog;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity   {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private static String TAG = MainActivity.class.getSimpleName();

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


    @Override
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "tbilisi";
                case 1:
                    return "batumi";
                case 2:
                    return "kutaisi";
                case 3:
                    return "zugdidi";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static String CITY_NAME = "city_name";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            switch (sectionNumber) {
                case 1:
                    args.putString(CITY_NAME, "Tbilisi");
                    break;
                case 2:
                    args.putString(CITY_NAME, "Batumi");
                    break;
                case 3:
                    args.putString(CITY_NAME, "Kutaisi");
                    break;
                case 4:
                    args.putString(CITY_NAME, "Zugdidi");
                    break;
            }


            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }
        private ProgressDialog pDialog;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final SwipeRefreshLayout refreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
            refreshLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_dark);
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            final ImageView icon=(ImageView) rootView.findViewById(R.id.weather_icon);
            final TextView tempView = (TextView) rootView.findViewById(R.id.temp);
            final TextView conditionView = (TextView) rootView.findViewById(R.id.section_label);
            final TextView pressureView=(TextView) rootView.findViewById(R.id.pressure);
            final TextView humidityView=(TextView) rootView.findViewById(R.id.humidity);
            final TextView windView=(TextView) rootView.findViewById(R.id.wind_speed);
            final TextView sunriseView=(TextView) rootView.findViewById(R.id.sunrise);
            final TextView sunsetView=(TextView) rootView.findViewById(R.id.sunset);



            String city=getArguments().getString(CITY_NAME);
            final String urlJsonObj="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=2de143494c0b295cca9337e1e96b00e0&units=metric";
            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            /*
            Check cache
             */
            Cache cache= requestQueue.getCache();

            Cache.Entry entry = cache.get(urlJsonObj);

            if(isInternetAvailable()){
                entry=null;
                cache.invalidate(urlJsonObj,true);
            }

            if(entry!=null){
                try {
                    String data = new String(entry.data, "UTF-8");
                    Log.d("CACHE DATA", data);
                    JSONObject jsonObject=new JSONObject(data);
                    setData(jsonObject,tempView,icon,conditionView,pressureView,humidityView,windView,sunriseView,sunsetView);

                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else{
                // Cache data not exist.
                makeJsonObjectRequest(requestQueue, urlJsonObj, tempView, icon, conditionView,pressureView,humidityView,windView,sunriseView,sunsetView);
            }
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                public void onRefresh() {
                    System.out.println("refresh");
                    makeJsonObjectRequest(requestQueue, urlJsonObj, tempView, icon, conditionView,pressureView,humidityView,windView,sunriseView,sunsetView);
                    refreshLayout.setRefreshing(false);
                }
            });

            return rootView;
        }

        private void makeJsonObjectRequest(RequestQueue requestQueue,
                                           String urlJsonObj,
                                           final TextView tempView,
                                           final ImageView icon,
                                           final TextView conditionView,
                                           final TextView pressureView,
                                           final TextView humidityView,
                                           final TextView windView,
                                           final TextView sunriseView,
                                           final TextView sunsetView) {
            showpDialog();

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    urlJsonObj, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, response.toString());

                    setData(response,tempView,icon,conditionView,pressureView,humidityView,windView,sunriseView,sunsetView);
                    hidepDialog();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(),
                            "საჭიროა ინტენეტთან კავშირი", Toast.LENGTH_SHORT).show();
                    // hide the progress dialog
                   hidepDialog();

                }
            });

            requestQueue.add(jsonObjReq);
            // Adding request to request queue
        }
        private void showpDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hidepDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
        private void setData(JSONObject response,
                             final TextView tempView,
                             final ImageView icon,
                             final TextView conditionView,
                             final TextView pressureView,
                             final TextView humidityView,
                             final TextView windView,
                             final TextView sunriseView,
                             final TextView sunsetView){
            try {
                // Parsing json object response
                // response will be a json object
                JSONObject main = response.getJSONObject("main");
                String temp = main.getString("temp");
                String pressure = main.getString("pressure");
                String humidity=main.getString("humidity");
                JSONObject wind=response.getJSONObject("wind");
                String windSpeed=wind.getString("speed");
                JSONObject sys=response.getJSONObject("sys");
                long sunrise=sys.getLong("sunrise");
                long sunset=sys.getLong("sunset");
                JSONArray weather= response.getJSONArray("weather");
                String weatherDescription=weather.getJSONObject(0).getString("description");
                String weatherIcon="http://openweathermap.org/img/w/"+weather.getJSONObject(0).getString("icon")+".png";
                //setting views
                Picasso.with(getActivity().getApplicationContext()).load(weatherIcon).into(icon);
                tempView.setText(temp + "\u00baC");
                conditionView.setText(weatherDescription);
                pressureView.setText(pressure + " hpa");
                humidityView.setText(humidity + "%");
                windView.setText(windSpeed+" m/s");
                sunriseView.setText(convertTime(sunrise));
                sunsetView.setText(convertTime(sunset));

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
        public boolean isInternetAvailable() {
            try {
                InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

                if (ipAddr.equals("")) {
                    return false;
                } else {
                    return true;
                }

            } catch (Exception e) {
                return false;
            }

        }
        public String convertTime(long time){

            Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+4")); // give a timezone reference for formating (see comment at the bottom
            return sdf.format(date);
        }

    }


}
