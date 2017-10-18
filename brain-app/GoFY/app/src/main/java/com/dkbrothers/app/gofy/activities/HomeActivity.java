package com.dkbrothers.app.gofy.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.adapters.PagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    FloatingActionButton fab;

    //private int idFragment=0;

    /*
    Fragment[] fragments = new Fragment[]{
            //new RouletteFragment()
    };
    */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       initViews();

    }


    private void initViews(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Home Sistema"));
        tabLayout.addTab(tabLayout.newTab().setText("Actividad En La Casa"));
        tabLayout.addTab(tabLayout.newTab().setText("Registros De Entrada"));
        tabLayout.addTab(tabLayout.newTab().setText("Revisar Sistema"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),AboutActivity.class));
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }

    /*
    public void showFragment(int idFragment){
        this.idFragment = idFragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainers,
                fragments[idFragment]).commit();
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_system) {
            showDialogAddSystem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showDialogAddSystem(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.view_dialog_add_system, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();


        final TextView btnAdd=(TextView) dialogView.findViewById(R.id.btn_add_system);

        final Button btnCall = (Button) dialogView.findViewById(R.id.btn_call);

        final EditText inputPassword = (EditText) dialogView.findViewById(R.id.input_code_accesss);
        final EditText inputName = (EditText) dialogView.findViewById(R.id.input_name_system);
        final EditText inputIdProduct = (EditText) dialogView.findViewById(R.id.input_id_product);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdd.setText("Procesando...");
                HashMap<String,Object> newSystem = new HashMap<>();
                newSystem.put("password",inputPassword.getText().toString());
                newSystem.put("name",inputName.getText().toString());
                newSystem.put("idProduct",inputIdProduct.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("datas-jackygifts")
                        .child("revisar-recompensar").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .updateChildren(newSystem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()) {

                        } else {

                        }
                        alertDialog.cancel();
                    }
                });
                /*
                if(SystemUtils.haveNetwork(getActivity())) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }, 6000);
                }
                */

            }
        });

        alertDialog.show();
    }



}
