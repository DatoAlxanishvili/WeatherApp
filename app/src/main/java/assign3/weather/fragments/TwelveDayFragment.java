package assign3.weather.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import assign3.weather.R;

/**
 * Created by dalkh on 01-Dec-15.
 */
public class TwelveDayFragment extends android.support.v4.app.Fragment {
    public TwelveDayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.twleve_day_fragment, container, false);
        return rootView;
    }
}