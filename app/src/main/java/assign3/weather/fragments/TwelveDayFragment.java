package assign3.weather.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.twleve_day_fragment, container, false);
        listView = (RecyclerView) rootView.findViewById(R.id.forecast_list);
        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity().getApplicationContext());
        listView.setLayoutManager(linearLayoutManager);
        String city = getArguments().getString("name");
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + "&appid=2de143494c0b295cca9337e1e96b00e0&units=metric&cnt=14";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ArrayList<ListItemObject> listItemObjectArrayList=new ArrayList<>();
        ForecastRecyclerViewAdapter adapter=new ForecastRecyclerViewAdapter(listItemObjectArrayList,getActivity().getApplicationContext());
        listView.setAdapter(adapter);
        fetchJsonData(requestQueue,url,listItemObjectArrayList,adapter);


        return rootView;
    }

    private void fetchJsonData(RequestQueue requestQueue, String url, final ArrayList<ListItemObject> recyclerViewItemObjectList, final ForecastRecyclerViewAdapter adapter) {
        //showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

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
                    adapter.notifyDataSetChanged();
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

        // Adding request to request queue
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