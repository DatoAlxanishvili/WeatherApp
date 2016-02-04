package assign3.weather.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import assign3.weather.MainActivity;
import assign3.weather.R;
import assign3.weather.adapters.ForecastRecyclerViewAdapter;
import assign3.weather.objects.ListItemObject;

/**
 * Created by dalkh on 01-Dec-15.
 */
public class TwelveDayFragment extends android.support.v4.app.Fragment {

    private static String TAG = MainActivity.class.getSimpleName();
    public TwelveDayFragment() {
    }
    RecyclerView listView;
    RelativeLayout backgroundView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.twleve_day_fragment, container, false);
        backgroundView= (RelativeLayout) rootView.findViewById(R.id.background_tab_two);
        final SwipeRefreshLayout refreshLayout= (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        listView = (RecyclerView) rootView.findViewById(R.id.forecast_list);
        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        listView.setLayoutManager(linearLayoutManager);
        String city = getArguments().getString("name");
        final String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&appid=81a98ab23d462997c65be8c8074ccc44&units=metric&cnt=14";
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        final ArrayList<ListItemObject> listItemObjectArrayList=new ArrayList<>();
        final ForecastRecyclerViewAdapter adapter=new ForecastRecyclerViewAdapter(listItemObjectArrayList,getActivity().getApplicationContext());
        listView.setAdapter(adapter);


        //Caching
        Cache cache = requestQueue.getCache();
        Cache.Entry entry = cache.get(url);

        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                Log.d("CACHE DATA", data);

                JSONObject jsonObject = new JSONObject(data);

                setData(jsonObject,listItemObjectArrayList, adapter, refreshLayout);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // Cache data not exist.
           /* refreshLayout.post(new Runnable() {
                @Override public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });*/
            fetchJsonData(requestQueue,url,listItemObjectArrayList,adapter,refreshLayout);
            //refreshLayout.setRefreshing(false);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                fetchJsonData(requestQueue,url,listItemObjectArrayList,adapter,refreshLayout);

            }
        });

        return rootView;
    }

    private void fetchJsonData(RequestQueue requestQueue, String url,
                               final ArrayList<ListItemObject> recyclerViewItemObjectList,
                               final ForecastRecyclerViewAdapter adapter,
                               final SwipeRefreshLayout refreshLayout) {
        //showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                setData(response,recyclerViewItemObjectList, adapter, refreshLayout);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        "საჭიროა ინტერნეტთან კავშირი", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
               // hidepDialog();
                refreshLayout.setRefreshing(false);
            }
        });

        requestQueue.add(jsonObjReq);

        // Adding request to request queue
    }
    private void setData(JSONObject response,
                         final ArrayList<ListItemObject> recyclerViewItemObjectList,
                          final ForecastRecyclerViewAdapter adapter,
                          final SwipeRefreshLayout refreshLayout){
        recyclerViewItemObjectList.clear();
        try {
            JSONArray list = response.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                long dt=item.getLong("dt");
                String date=convertDate(dt);
                String weekDay=convertWeekDay(dt);
                JSONObject temp = item.getJSONObject("temp");
                Double temp_max = temp.getDouble("max");
                Integer temp_maximum = Math.round(temp_max.floatValue());
                Double temp_min= temp.getDouble("min");
                Integer temp_minimum = Math.round(temp_min.floatValue());
                String icon=(item.getJSONArray("weather").getJSONObject(0).getString("icon"));
                ListItemObject listItem= new ListItemObject(date,weekDay,icon,temp_maximum.toString(),temp_minimum.toString());
                recyclerViewItemObjectList.add(listItem);
            }
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
    public String convertWeekDay(long time) {

        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEE"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4")); // give a timezone reference for formating
        String englishWeekDay = sdf.format(date);
        switch (englishWeekDay) {
            case "Mon":
                return "ორშაბათი";
            case "Tue":
                return "სამშაბათი";
            case "Wed":
                return "ოთხშაბათი";
            case "Thu":
                return "ხუთშაბათი";
            case "Fri":
                return "პარასკევი";
            case "Sat":
                return "შაბათი";
            case "Sun":
                return "კვირა";
        }
        return null;
    }
    public String convertDate(long time) {

        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+4")); // give a timezone reference for formating (see comment at the bottom
        return sdf.format(date);
    }

   /* private class DownloadFilesTask extends AsyncTask<String, Integer, ArrayList<ListItemObject>> {
        protected ArrayList<ListItemObject> doInBackground(String... urls) {
            int count = urls.length;
            final ArrayList<ListItemObject> recyclerViewItemObjectList = new ArrayList<>();
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        urls[0], null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray list = response.getJSONArray("list");
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject item = list.getJSONObject(i);
                                long dt=item.getLong("dt");
                                String date=convertDate(dt);
                                String weekDay=convertWeekDay(dt);
                                JSONObject temp = item.getJSONObject("temp");
                                Double temp_max = temp.getDouble("max");
                                Integer temp_maximum = Math.round(temp_max.floatValue());
                                Double temp_min= temp.getDouble("min");
                                Integer temp_minimum = Math.round(temp_min.floatValue());
                                String icon=(item.getJSONArray("weather").getJSONObject(0).getString("icon"));
                                ListItemObject listItem= new ListItemObject(date,weekDay,icon,temp_maximum.toString(),temp_minimum.toString());
                                recyclerViewItemObjectList.add(listItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                        // hidepDialog();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(),
                                "საჭიროა ინტერნეტთან კავშირი", Toast.LENGTH_SHORT).show();
                        // hide the progress dialog
                        // hidepDialog();

                    }
                });

                requestQueue.add(jsonObjReq);


            if(jsonObjReq!=null){
            return recyclerViewItemObjectList;}
            return null;
        }



        protected void onPostExecute(ArrayList<ListItemObject> result) {
            super.onPostExecute(result);
            Context context=getActivity();
            listView.setAdapter(new ForecastRecyclerViewAdapter(result,context));
        }
    }*/
}