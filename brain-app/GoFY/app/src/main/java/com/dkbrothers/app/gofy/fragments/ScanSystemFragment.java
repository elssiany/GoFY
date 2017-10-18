package com.dkbrothers.app.gofy.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.adapters.SensorAdapter;
import com.dkbrothers.app.gofy.models.Sensor;
import com.dkbrothers.app.gofy.utils.ManagerSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by kens.
 */
public class ScanSystemFragment extends Fragment implements View.OnClickListener{



    RecyclerView recyclerVData;
    SensorAdapter sensorAdapter;
    View rootView=null;
    LinearLayout containerIntro,containerFormScan,containerListSensors;
    TextView btnNext;
    ProgressDialog mProgressDialog=null;
    public static List<Sensor> sensors = new ArrayList<>();
    //ViewPager viewPager;
    int posiSensor=0;
    int totalSensors=0;
    boolean errorSensors=false;

    int numSensorDM=0,numSensorScanDM=0,numAlarmScan=0,numAlarm=0;
    //PagerSensorAdapter pagerSensorAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(rootView==null) {

            rootView = inflater.inflate(R.layout.scan_sensors_fragment, container, false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerVData = (RecyclerView) rootView.findViewById(R.id.lvSensor);
            containerFormScan=(LinearLayout) rootView.findViewById(R.id.container_form_scan);
            containerIntro=(LinearLayout) rootView.findViewById(R.id.container_intro);
            containerListSensors=(LinearLayout) rootView.findViewById(R.id.container_list_sensors);
            btnNext=(TextView) rootView.findViewById(R.id.btn_next);
            sensorAdapter = new SensorAdapter(getActivity());
            recyclerVData.setLayoutManager(layoutManager);
            recyclerVData.setAdapter(sensorAdapter);
            btnNext.setOnClickListener(this);
            rootView.findViewById(R.id.btn_scan_system).setOnClickListener(this);

            //viewPager = (ViewPager) rootView.findViewById(R.id.pager);

            //sensores con problemas
            getDatas();


        }
        return rootView;
    }


    private void getDatas(){

        FirebaseDatabase.getInstance().getReference().child("review-sensors").child(ManagerSharedPreferences
                .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ"))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Map<String,Object>> datasSensors = new ArrayList<>();
                for(DataSnapshot sensor: dataSnapshot.getChildren()) {
                    Map<String,Object> data = (Map<String,Object>) sensor.getValue();
                    assert data != null;
                    datasSensors.add(data);
                }
                if(datasSensors.size()>0)
                    errorSensors=true;

                sensorAdapter.addDatas(datasSensors);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
                .getPreferences(getActivity(),
                        "idProduct", "-Kw24TH46pgvGBWBr8lQ"))
                .child("sensors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot sensor : dataSnapshot.getChildren()) {
                            Map<String, Object> data = (Map<String, Object>) sensor.getValue();
                            assert data != null;
                            int idDrawable = R.drawable.ic_proximity_sensor_96;
                            if (data.get("type").toString().equals("DM")) {
                                numSensorDM+=1;
                                idDrawable = R.drawable.ic_proximity_sensor_96;
                            } else if (data.get("type").toString().equals("DMD")) {
                                idDrawable = R.drawable.ic_door_sensor_alarmed_96;
                                numSensorDM+=1;
                            } else if (data.get("type").toString().equals("A")) {
                                idDrawable = R.drawable.ic_home_alarm_96;
                                numAlarm+=1;
                            }
                            Sensor sensorObejct=new Sensor(sensor.getKey(), data.get("location").toString()
                                    , data.get("description").toString(),data.get("type").toString()
                                    ,idDrawable);
                            sensors.add(sensorObejct);
                            sensorFragments.add(new SensorFragment(sensorObejct));
                        }
                        totalSensors=sensors.size()-1;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //hideProgressDialog();
                    }
                });


    }

    DatabaseReference databaseReferenceAskP;
    ValueEventListener valueEventListenerAskP;
    boolean modeScan;


    @Override
    public void onClick(final View view) {
        //boolean status = Boolean.valueOf(view.getTag().toString());
        switch (view.getId()){
            case R.id.btn_scan_system:
                if(view.getTag().toString().equals("scan")) {
                    databaseReferenceAskP = FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
                            .getPreferences(getActivity(),
                                    "idProduct", "-Kw24TH46pgvGBWBr8lQ"))
                            .child("system-information").child("modeScan");
                    showProgressDialog("Espere, por favor", "Solicitando permiso...");
                    FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
                            .getPreferences(getActivity(),
                                    "idProduct", "-Kw24TH46pgvGBWBr8lQ")).child("system-information")
                            .child("askPermission").setValue("scan-sensors").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgressDialog.setMessage("Conectando con el sistema...");
                                valueEventListenerAskP = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        modeScan = (boolean) dataSnapshot.getValue();

                                        if (modeScan) {
                                            mProgressDialog.setMessage("Preparando sensores...");
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    view.setTag("s");
                                                    ((TextView) view).setText("Aceptar");
                                                    initViewsForScanSystem();
                                                    hideProgressDialog();
                                                    databaseReferenceAskP.removeEventListener(valueEventListenerAskP);
                                                    Toast.makeText(getActivity(), "GoFY esta listo para escanear cada sensor con tu ayuda"
                                                            , Toast.LENGTH_LONG).show();
                                                }
                                            }, 3000);

                                        }
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (!modeScan) {
                                                        FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
                                                                .getPreferences(getActivity(),
                                                                        "idProduct", "-Kw24TH46pgvGBWBr8lQ")).child("system-information")
                                                                .child("askPermission").setValue("not");
                                                        databaseReferenceAskP.removeEventListener(valueEventListenerAskP);
                                                        Toast.makeText(getActivity(), "No podemos hacer el escaneo del sistema en estos momentos..."
                                                                , Toast.LENGTH_LONG).show();
                                                        hideProgressDialog();
                                                    }
                                                }
                                            }, 12000);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        hideProgressDialog();
                                    }
                                };
                                databaseReferenceAskP.addValueEventListener(valueEventListenerAskP);
                            } else {
                                Toast.makeText(getActivity(), "No podemos hacer el escaneo del sistema en estos momentos..."
                                        , Toast.LENGTH_LONG).show();
                                hideProgressDialog();
                            }
                        }
                    });
                }else{

                    ((ImageView) rootView.findViewById(R.id.img_intro)).setImageDrawable(
                            ContextCompat.getDrawable(getActivity(),R.drawable.ic_iris_scan_96));
                    ((TextView) rootView
                            .findViewById(R.id.txt_msm)).setText(
                                    "Si haces un escaneo del sistema ayudas a GoFY a mejor la seguridad");
                    view.setTag("scan");
                    ((TextView) view).setText("Escanear Sistema");
                    errorSensors=false;
                    posiSensor=0;
                    containerFormScan.setVisibility(View.GONE);
                    containerListSensors.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_next:
                if(posiSensor>=totalSensors){
                    mProgressDialog.setMessage("Recopilando datos...");
                    showProgressDialog("Espere, por favor","Recopilando datos...");
                    getReportScan();
                }else {
                    Map<String, Object> sensor = new HashMap<>();
                    sensor.put("id", sensorFragments.get(posiSensor).sensor.getId());
                    sensor.put("dateTime", ServerValue.TIMESTAMP);
                    sensor.put("name", sensorFragments.get(posiSensor).sensor.getTitle());
                    sensor.put("description", "El sensor no quiere detectar objetos");
                    FirebaseDatabase.getInstance().getReference().child("review-sensors").child(ManagerSharedPreferences
                            .getPreferences(getActivity(), "idProduct", "-Kw24TH46pgvGBWBr8lQ")).child(
                            sensor.get("id").toString()
                    ).setValue(sensor);
                    posiSensor += 1;
                    showFragment(posiSensor);
                    if(sensors.get(posiSensor).getType().equals("A")) {
                        FirebaseDatabase.getInstance().getReference().child("active-systems")
                                .child(ManagerSharedPreferences
                                        .getPreferences(getActivity(), "idProduct", "-Kw24TH46pgvGBWBr8lQ"))
                                .child("system-information").child("alarm").setValue(true);
                    }
                }
                break;
        }
    }



    private void getReportScan(){


        containerFormScan.setVisibility(View.GONE);
        containerIntro.setVisibility(View.VISIBLE);

        if(errorSensors) {
            containerListSensors.setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById
                    (R.id.txt_msm)).setText("GoFY a detectado daños en algunas sensores por favor revisa el reporte y contacta a un asesor lo más pronto posible");

            ((ImageView) rootView.findViewById(R.id.img_intro)).setImageDrawable(ContextCompat.getDrawable(
                    getActivity(),R.drawable.ic_error_96));

        }else{
            sensorAdapter.removeDatas();
            ((TextView) rootView.findViewById
                    (R.id.txt_msm)).setText("Escaneo exitoso, sistema funcionando");

            ((ImageView) rootView.findViewById(R.id.img_intro)).setImageDrawable(ContextCompat.getDrawable(
                    getActivity(),R.drawable.ic_protect_96));
        }

        FirebaseDatabase.getInstance().getReference().child("report-scan-sensors").child(ManagerSharedPreferences
                .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("active-systems").child(ManagerSharedPreferences
                            .getPreferences(getActivity(),
                                    "idProduct", "-Kw24TH46pgvGBWBr8lQ")).child("system-information")
                            .child("askPermission").setValue("scan-sensors").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){

                          }
                          hideProgressDialog();
                        }
                    });
                }else{
                    hideProgressDialog();
                }
            }
        });


    }


    private List<SensorFragment> sensorFragments = new ArrayList<>();

    private void initViewsForScanSystem(){


        FirebaseDatabase.getInstance().getReference().child("report-scan-sensors").child(ManagerSharedPreferences
                .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,Object> datas = (Map<String,Object>) dataSnapshot.getValue();
                        if(datas!=null) {
                            for (Object key : datas.keySet().toArray()) {
                                String typeSensor = datas.get(key.toString()).toString();
                                switch (typeSensor) {
                                    case "DM":
                                        numSensorScanDM += 1;
                                        break;
                                    case "DMD":
                                        numSensorScanDM += 1;
                                        break;
                                    default:
                                        numAlarmScan += 1;
                                        break;
                                }
                            }
                            if (posiSensor >= totalSensors) {
                                mProgressDialog.setMessage("Recopilando datos...");
                                showProgressDialog("Espere, por favor", "Recopilando datos...");
                                getReportScan();
                            } else {
                                posiSensor += 1;
                                showFragment(posiSensor);
                                if(sensors.get(posiSensor).getType().equals("A")){
                                    FirebaseDatabase.getInstance().getReference().child("active-systems")
                                            .child(ManagerSharedPreferences
                                                    .getPreferences(getActivity(),"idProduct","-Kw24TH46pgvGBWBr8lQ"))
                                            .child("system-information").child("alarm").setValue(true);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        hideProgressDialog();
                    }
                });

        showFragment(0);

        containerIntro.setVisibility(View.GONE);
        containerFormScan.setVisibility(View.VISIBLE);
        containerListSensors.setVisibility(View.GONE);

    }


    public void showFragment(int idFragment){

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainers,
                sensorFragments.get(idFragment)).commit();
    }


    public void showProgressDialog(String title,String message) {

        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getActivity(),title,
                    message,false,false);
            //mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();

    }



    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }








}