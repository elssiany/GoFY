package com.dkbrothers.app.gofy.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.utils.GoogleSignIn;
import com.dkbrothers.app.gofy.utils.ManagerSharedPreferences;
import com.dkbrothers.app.gofy.utils.SystemUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private GoogleSignIn googleSignIn;
    private static final int RC_SIGN_IN = 9491;
    private final String TAG="MainActivity";

    public LinearLayout containerFormRegisterUser=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        containerFormRegisterUser=(LinearLayout) findViewById(R.id.containerForm);
        if(ManagerSharedPreferences.getPreferences(this,
                "showIntroGoFY",true)) {
            googleSignIn = new GoogleSignIn(this, MainActivity.this);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(SystemUtils.haveNetwork(getApplicationContext())) {
                        googleSignIn.signIn();
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.message_error_4,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }, 2000);
        }

    }



    private void registerUser(String code){


        final Map<String,Object> user = new HashMap<>();


        @SuppressLint("HardwareIds") final String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);


        Query query = FirebaseDatabase.getInstance().getReference()
                .child("password-access-users-registation").orderByChild("password").equalTo(code);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                        if (data == null) {
                            Toast.makeText(getApplicationContext(), "Esta contraseña de acceso es invalida, por favor verificar bien"
                                    , Toast.LENGTH_LONG).show();
                        } else {

                            user.put("name", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            user.put("photoUrl", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            user.put("idDevice", androidId);
                            user.put("deviceManufacturer", Build.MANUFACTURER);
                            user.put("deviceModel", Build.MODEL);
                            user.put("status", 0);
                            user.put("isAdmin",false);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("active-systems").child(data.get("id").toString()).child("detaills").child("lock-system")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            boolean lockSystem=(boolean) dataSnapshot.getValue();

                                            if(lockSystem) {
                                                FirebaseDatabase.getInstance().getReference().child("active-systems").child(data.get("id").toString())
                                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (!task.isSuccessful()) {
                                                            ManagerSharedPreferences.editPreferences(MainActivity.this,"showIntroGoFY",false);
                                                            ManagerSharedPreferences.editPreferences(MainActivity.this,
                                                                    "idProduct", data.get("id").toString());
                                                            ManagerSharedPreferences.editPreferences(MainActivity.this,
                                                                    "isAdmin", false);
                                                            finish();
                                                            startActivity(new Intent(MainActivity.this,
                                                                    HomeActivity.class));
                                                        }else {
                                                            Toast.makeText(getApplicationContext(),"Error, intente más tarde por favor..."
                                                                    ,Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }else {
                                                Toast.makeText(getApplicationContext(), "Lo siento :(, en estos momentos el sistema se encuentra bloqueado por seguridad"
                                                        , Toast.LENGTH_LONG).show();
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                            FirebaseCrash.logcat(Log.WARN, TAG, databaseError.getDetails());
                                            FirebaseCrash.report(databaseError.toException());
                                        }
                                    });

                        }
                    }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            FirebaseCrash.logcat(Log.WARN, TAG, databaseError.getDetails());
                            FirebaseCrash.report(databaseError.toException());
                        }
                    });


    }



    private void registerSystem(final String idProduct,final String password,final String name){

        FirebaseDatabase.getInstance().getReference()
                .child("list-product-ids").child(idProduct)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()==null){
                            Toast.makeText(getApplicationContext(),"Este ID es invalido, no existe en nuestros registros"
                            ,Toast.LENGTH_LONG).show();
                        }else{

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("idLicense")
                                    .setValue(dataSnapshot.getValue().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()) {
                                        Map<String,Object> dataExtra = new HashMap<>();
                                        dataExtra.put("id",idProduct);
                                        dataExtra.put("password",password);
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("password-access-users-registation").child(idProduct)
                                                .updateChildren(dataExtra).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(!task.isSuccessful()) {
                                                    Map<String,Object> dataExtra = new HashMap<>();
                                                    dataExtra.put("name",name);
                                                    dataExtra.put("password",password);
                                                    dataExtra.put("lock-system",false);
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("active-systems").child(idProduct).child("detaills")
                                                            .updateChildren(dataExtra).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(!task.isSuccessful()) {
                                                                ManagerSharedPreferences.editPreferences(MainActivity.this,"showIntroGoFY",false);
                                                                ManagerSharedPreferences.editPreferences(MainActivity.this,
                                                                        "idProduct", idProduct);
                                                                finish();
                                                                startActivity(new Intent(MainActivity.this,
                                                                        HomeActivity.class));
                                                            }else {
                                                                Toast.makeText(getApplicationContext(),"Error, intente más tarde por favor..."
                                                                        ,Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                                }else {
                                                    Toast.makeText(getApplicationContext(),"Error, intente más tarde por favor..."
                                                            ,Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                        finish();
                                        startActivity(new Intent(MainActivity.this,
                                                HomeActivity.class));
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Error, intente más tarde por favor..."
                                                ,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        FirebaseCrash.logcat(Log.WARN,TAG,databaseError.getDetails());
                        FirebaseCrash.report(databaseError.toException());
                    }
                });



    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                //para registrar un nuevo usuario premium
                googleSignIn.firebaseAuthWithGoogle(account,true);
            } else {
                //Google Sign In failed, update UI appropriately
                if(googleSignIn.mProgressDialog!=null)
                    googleSignIn.mProgressDialog.cancel();

                FirebaseCrash.logcat(Log.ERROR,TAG,
                        "Google Sign In failed-statusCode:"+result.getStatus().getStatusCode());
                Toast.makeText(MainActivity.this,
                        R.string.message_error_3,Toast.LENGTH_LONG).show();
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]


}
