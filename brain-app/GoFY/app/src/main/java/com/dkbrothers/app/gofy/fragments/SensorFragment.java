package com.dkbrothers.app.gofy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.models.Sensor;

/**
 * Created by kens on 12/10/17.
 */
@SuppressLint("ValidFragment")
public class SensorFragment extends Fragment {

    View rootView=null;
    public Sensor sensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView==null) {

            rootView = inflater.inflate(R.layout.item_test_sensor_fragment, container, false);

            ((TextView) rootView.findViewById(R.id.txt_title)).setText(sensor.getTitle());

            ((ImageView) rootView.findViewById(R.id.img_sensor)).setImageDrawable(ContextCompat.getDrawable(
                    getActivity(),sensor.getIdDrawable()));

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),sensor.getDescription(),Toast.LENGTH_LONG).show();
                }
            });

        }

        return rootView;

    }

    public SensorFragment(Sensor sensor) {
        this.sensor=sensor;
    }


}