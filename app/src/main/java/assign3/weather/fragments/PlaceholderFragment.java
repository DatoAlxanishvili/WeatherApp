package assign3.weather.fragments;

/**
 * Created by dalkh on 01-Dec-15.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import assign3.weather.MainActivity;
import assign3.weather.R;
import assign3.weather.customViews.DejaVUSansTextView;
import assign3.weather.customViews.RobotoTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static String TAG = MainActivity.class.getSimpleName();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    /*    public static PlaceholderFragment newInstance(int sectionNumber) {

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
        }*/
    public PlaceholderFragment() {
    }

    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        refreshLayout.setColorSchemeColors(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);
        pDialog = new ProgressDialog(getContext(),R.style.ProgressDialogTheme);
        pDialog.setMessage("გთხოვთ დაელოდოთ...");
        pDialog.setCancelable(false);
        DejaVUSansTextView cityNameView= (DejaVUSansTextView) rootView.findViewById(R.id.city);
        final ImageView icon = (ImageView) rootView.findViewById(R.id.weather_icon);
        final RobotoTextView tempView = (RobotoTextView) rootView.findViewById(R.id.temp);
        final DejaVUSansTextView conditionView =(DejaVUSansTextView) rootView.findViewById(R.id.temp_description);
        final LinearLayout weekWeatherView = (LinearLayout) rootView.findViewById(R.id.calendar);
        cityNameView.setText(getArguments().getString("cityName"));
        String city = getArguments().getString("name");
        System.out.println(city);

        final String urlJsonObj = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=2de143494c0b295cca9337e1e96b00e0&units=metric";
        final String urlJsonWeekForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&appid=2de143494c0b295cca9337e1e96b00e0&units=metric&cnt=7";
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

            /*
            Check cache
             */
        Cache cache = requestQueue.getCache();

        Cache.Entry entry = cache.get(urlJsonObj);
        Cache.Entry entryWeekForecast = cache.get(urlJsonWeekForecast);

        if (isInternetAvailable()) {
            entry = null;
            entryWeekForecast=null;
            cache.invalidate(urlJsonObj, true);
            cache.invalidate(urlJsonWeekForecast, true);
        }

        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                String weekData=new String(entryWeekForecast.data, "UTF-8");
                Log.d("CACHE DATA", data);
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonObject1 =new JSONObject(weekData);
                setData(jsonObject, tempView, icon, conditionView);
                setWeeklyForecastData(jsonObject1,weekWeatherView);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // Cache data not exist.
            makeJsonObjectRequest(requestQueue, urlJsonObj, tempView, icon, conditionView);
            getWeekForecastJson(requestQueue, urlJsonWeekForecast, weekWeatherView);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                System.out.println("refresh");
                makeJsonObjectRequest(requestQueue, urlJsonObj, tempView, icon, conditionView);
                if(isInternetAvailable()){
                weekWeatherView.removeAllViews();
                    getWeekForecastJson(requestQueue, urlJsonWeekForecast, weekWeatherView);
                }

                refreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    private void makeJsonObjectRequest(RequestQueue requestQueue,
                                       String urlJsonObj,
                                       final TextView tempView,
                                       final ImageView icon,
                                       final TextView conditionView
                                       ) {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                setData(response, tempView, icon, conditionView);
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
                         final TextView conditionView
                         ) {
        try {
            // Parsing json object response
            // response will be a json object
            JSONObject main = response.getJSONObject("main");
            String temp = main.getString("temp");
            long date= response.getLong("dt");
            /*String pressure = main.getString("pressure");
            String humidity = main.getString("humidity");
            JSONObject wind = response.getJSONObject("wind");
            String windSpeed = wind.getString("speed");
            JSONObject sys = response.getJSONObject("sys");
            long sunrise = sys.getLong("sunrise");
            long sunset = sys.getLong("sunset");*/
            JSONArray weather = response.getJSONArray("weather");
            String weatherDescription = weather.getJSONObject(0).getString("description");
            String weatherIcon = weather.getJSONObject(0).getString("icon");
            icon.setImageResource(getResources().getIdentifier("w"+weatherIcon, "drawable", getActivity().getApplicationContext().getPackageName()));
            //setting views
            tempView.setText(temp + "\u00ba");
            conditionView.setText(convertTime(date)+", "+weatherDescription);
            /*pressureView.setText(pressure + " hpa");
            humidityView.setText(humidity + "%");
            windView.setText(windSpeed + " m/s");
            sunriseView.setText(convertTime(sunrise));
            sunsetView.setText(convertTime(sunset));*/

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }


    public boolean isInternetAvailable() {

        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            if(reachable){
                System.out.println("Internet access");
                return reachable;
            }
            else{
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }

    public String convertTime(long time) {

        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM, "+convertWeekDay(time)+" ,HH:mm"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4")); // give a timezone reference for formating (see comment at the bottom
        return sdf.format(date);
    }


    private void drawWeekWeather(LinearLayout root,
                                 ArrayList<Integer> tempArray,
                                 ArrayList<String> weekDayArray,
                                 ArrayList<String> iconArray,
                                 ArrayList<Integer> tempMinArray) {


        for (int i = 0; i < 7; i++) {
            LinearLayout element = new LinearLayout(getActivity().getApplicationContext());
            element.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            element.setLayoutParams(param);
            TextView weekDay = new TextView(getActivity().getApplicationContext());
            ImageView weatherState = new ImageView(getActivity().getApplicationContext());
            RobotoTextView temperature = new RobotoTextView(getActivity().getApplicationContext(),null);
            RobotoTextView temperatureMin = new RobotoTextView(getActivity().getApplicationContext(),null);
            weekDay.setGravity(Gravity.CENTER_HORIZONTAL);
            temperature.setGravity(Gravity.CENTER_HORIZONTAL);
            temperatureMin.setGravity(Gravity.CENTER_HORIZONTAL);
            temperature.setTextSize(15);
            temperatureMin.setTextSize(15);
            temperature.setTextColor(getResources().getColor(R.color.white));
            temperatureMin.setTextColor(getResources().getColor(R.color.white));
            weekDay.setTextColor(getResources().getColor(R.color.white));
            weatherState.setPadding(0, 22, 0, 0);
            temperature.setPadding(0, 22, 0, 0);
            weatherState.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
            weekDay.setText(weekDayArray.get(i));
            temperature.setText(tempArray.get(i) + "º");
            temperatureMin.setText(tempMinArray.get(i) + "º");
            temperatureMin.setPadding(0,0,0,30);
            weatherState.setImageResource(getResources().getIdentifier("w"+iconArray.get(i), "drawable", getActivity().getApplicationContext().getPackageName()));
            TextView separator= new TextView(getActivity().getApplicationContext());
            separator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            separator.setBackgroundColor(getResources().getColor(R.color.white));
            separator.setGravity(Gravity.CENTER_HORIZONTAL);
            element.addView(weekDay, 0);
            element.addView(weatherState, 1);
            element.addView(temperature, 2);
            element.addView(separator,3);
            element.addView(temperatureMin,4);
            root.addView(element, i);
        }

    }

    private void getWeekForecastJson(RequestQueue requestQueue, String url, final LinearLayout root) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {




            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
               setWeeklyForecastData(response,root);

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
     private void setWeeklyForecastData(JSONObject response,LinearLayout root){
         ArrayList<Integer> tempArray = new ArrayList<>();
         ArrayList<String> weekDayArray = new ArrayList<>();
         ArrayList<String> iconArray = new ArrayList<>();
         ArrayList<Integer> tempMinArray = new ArrayList<>();
         try {
             JSONArray list = response.getJSONArray("list");
             for (int i = 0; i < list.length(); i++) {
                 JSONObject item = list.getJSONObject(i);
                 long dt=item.getLong("dt");
                 weekDayArray.add(convertWeekDay(dt));
                 JSONObject temp = item.getJSONObject("temp");
                 Double temp_max = temp.getDouble("max");
                 tempArray.add(Math.round(temp_max.floatValue()));
                 Double temp_min= temp.getDouble("min");
                 tempMinArray.add(Math.round(temp_min.floatValue()));
                 iconArray.add(item.getJSONArray("weather").getJSONObject(0).getString("icon"));
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }

         drawWeekWeather(root, tempArray, weekDayArray, iconArray, tempMinArray);
     }
    public String convertWeekDay(long time) {

        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4")); // give a timezone reference for formating
        String englishWeekDay = sdf.format(date);
        switch (englishWeekDay) {
            case "Mon":
                return "ორშ.";
            case "Tue":
                return "სამშ.";
            case "Wed":
                return "ოთხ.";
            case "Thu":
                return "ხუთ.";
            case "Fri":
                return "პარ.";
            case "Sat":
                return "შაბ.";
            case "Sun":
                return "კვ.";


        }
        return null;
    }
}