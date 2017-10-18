package com.dkbrothers.app.gofy.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.adapters.SensorAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by kens.
 */
@SuppressLint("ValidFragment")
public class ListDatasFragment extends Fragment {


    RecyclerView recyclerVData;
    SensorAdapter sensorAdapter;
    View rootView=null;
    private String idNodeHumedades="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.scan_sensors_fragment, container, false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerVData = (RecyclerView) rootView.findViewById(R.id.lvDatas);
            sensorAdapter = new SensorAdapter(getActivity());
            recyclerVData.setLayoutManager(layoutManager);
            recyclerVData.setAdapter(sensorAdapter);

            getDatas();

        }

        return rootView;

    }


    public void getDatas(){


        FirebaseDatabase.getInstance().getReference().child("historial")
                .child(idNodeHumedades)
        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<Map<String,Object>> datas = new ArrayList<>();
                        for(DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                            Map<String,Object> data = (Map<String,Object>) productSnapshot.getValue();
                            assert data != null;
                            if(data.get("fecha")==null){
                                //data.put("fecha", ServerValue.TIMESTAMP);
                                FirebaseDatabase.getInstance().getReference().child("historial")
                                        .child(idNodeHumedades)
                                        .child(productSnapshot.getKey())
                                        .child("fecha").setValue(ServerValue.TIMESTAMP);
                            }
                            datas.add(data);
                        }
                        sensorAdapter.addDatas(datas);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                    }
                });


    }


    public ListDatasFragment(String idNodeHumedades) {
        this.idNodeHumedades=idNodeHumedades;
    }


}