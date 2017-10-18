package com.dkbrothers.app.gofy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.activities.HomeActivity;
import com.dkbrothers.app.gofy.adapters.SensorAdapter;
import com.dkbrothers.app.gofy.utils.ManagerSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by kens.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{


    RecyclerView recyclerVData;
    SensorAdapter sensorAdapter;
    View rootView = null;
    private Switch aSwitchAlarm,aSwitchApagar;
    private TextView txtCPU,txtCPUTemp,txtRAM,txtWifi,txtStatusSystem;
    private LinearLayout containerActuadores;
    private HomeActivity homeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




            rootView = inflater.inflate(R.layout.system_home_fragment, container, false);
        homeActivity=(HomeActivity) getActivity();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerVData = (RecyclerView) rootView.findViewById(R.id.lvSensor);
            sensorAdapter = new SensorAdapter(getActivity());
            recyclerVData.setLayoutManager(layoutManager);
            recyclerVData.setAdapter(sensorAdapter);
            txtCPU=(TextView) rootView.findViewById(R.id.txt_cpu);
            //containerActuadores=(LinearLayout)rootView.findViewById(R.id.contenedor_actuadores);
            aSwitchAlarm=(Switch) rootView.findViewById(R.id.switch_alarm);
            //aSwitchApagar=(Switch) rootView.findViewById(R.id.switch_mode_general);
            txtCPUTemp=(TextView) rootView.findViewById(R.id.txt_cpu_temp);
            txtRAM=(TextView) rootView.findViewById(R.id.txt_ram);
            txtWifi=(TextView) rootView.findViewById(R.id.txt_wifi);
            txtStatusSystem=(TextView) rootView.findViewById(R.id.txt_status_system);
            //rootView.findViewById(R.id.txt_motobomba).setOnClickListener(this);
            //rootView.findViewById(R.id.txt_estado_techo).setOnClickListener(this);
            //rootView.findViewById(R.id.txt_iluminacion_2).setOnClickListener(this);
            txtWifi.setOnClickListener(this);
            txtCPU.setOnClickListener(this);
            txtCPUTemp.setOnClickListener(this);
            txtRAM.setOnClickListener(this);

            getData();


        return rootView;
    }


    private void getData(){

        FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
                .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ"))
                .child("system-information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> infoSistema = (Map<String,Object>) dataSnapshot.getValue();
                assert infoSistema != null;
                txtCPU.setText(infoSistema.get("valueCPU").toString());
                txtWifi.setText(infoSistema.get("valueCPU").toString());
                txtCPUTemp.setText(infoSistema.get("valueCPUTemp").toString()+" C°");
                txtRAM.setText(infoSistema.get("valueRAM").toString()+" MB");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
        .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ")).child("system-information").child("alarm")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                aSwitchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            aSwitchAlarm.setText("Alarm On");
                        }else {
                            aSwitchAlarm.setText("Alarm Off");
                        }
                        FirebaseDatabase.getInstance().getReference().child("active-systems")
                                .child(ManagerSharedPreferences
                                .getPreferences(homeActivity,"idProduct","-Kw24TH46pgvGBWBr8lQ"))
                                .child("system-information").child("alarm").setValue(b);
                    }
                });
                aSwitchAlarm.setChecked((boolean) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("review-sensors").child(ManagerSharedPreferences
                .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<Map<String,Object>> datasSensors = new ArrayList<>();
                        if(dataSnapshot.getValue()!=null) {
                            for (DataSnapshot sensor : dataSnapshot.getChildren()) {
                                Map<String, Object> data = (Map<String, Object>) sensor.getValue();
                                assert data != null;
                                datasSensors.add(data);
                            }
                            if(txtStatusSystem!=null) {
                                txtStatusSystem.setCompoundDrawablesWithIntrinsicBounds(null,
                                        ContextCompat.getDrawable(homeActivity, R.drawable.ic_error_96), null, null);
                                txtStatusSystem.setText("Hay Sensores Dañados");
                            }
                            if(sensorAdapter!=null){
                                sensorAdapter.addDatas(datasSensors);
                            }
                        }else {
                            txtStatusSystem.setCompoundDrawablesWithIntrinsicBounds(null,
                                    ContextCompat.getDrawable(homeActivity,R.drawable.ic_protect_96),null,null);
                            txtStatusSystem.setText("Sistema Funcionando");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }



    @Override
    public void onClick(View view) {
        if(view.getTag()!=null){
            Toast.makeText(getActivity(),getTag(),Toast.LENGTH_LONG).show();
        }
    }




}