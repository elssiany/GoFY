package com.dkbrothers.app.gofy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.activities.HomeActivity;
import com.dkbrothers.app.gofy.activities.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by kevin on 19/06/2017.
 */

public class GoogleSignIn implements GoogleApiClient.OnConnectionFailedListener {


    private static final int RC_SIGN_IN = 9491;
    private final String TAG = "GoogleSignIn";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private GoogleApiClient mGoogleApiClient;

    private Activity activity;
    private Context context;


    public GoogleSignIn(Context context, Activity activity){
        this.activity=activity;
        this.context=context;
        initAuth();
    }



    private void initAuth(){

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

     private boolean registerProduct=false;
    String idProduct="";

    // [START auth_with_google]
    public void firebaseAuthWithGoogle(final GoogleSignInAccount googleSignInAccount,
                                       final boolean registerUser) {


        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        final AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final Map<String,Object> newUser = new HashMap<>();


                            FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("idLicense")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.getValue()==null){
                                                registerProduct=true;
                                            }else {
                                                idProduct=dataSnapshot.getValue().toString();
                                            }
                                            newUser.put("name",user.getDisplayName());
                                            newUser.put("email",user.getEmail());

                                            //newUser.put("coins","10");
                                            newUser.put("idToken",FirebaseInstanceId.getInstance().getToken());
                                            if(user.getPhotoUrl()!=null)
                                                newUser.put("profilePhoto",user.getPhotoUrl().toString());
                                            else
                                                newUser.put("profilePhoto","not data");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("users").child(user.getUid())
                                                    .updateChildren(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(!task.isSuccessful()) {
                                                        FirebaseCrash.logcat(Log.ERROR,TAG,
                                                                "Se pudo guardar los datos del usuario en firebase");
                                                        FirebaseCrash.report(task.getException());
                                                        Toast.makeText(context, R.string.message_error_1,
                                                                Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        if(registerUser){
                                            /*
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("datas-jackygifts")
                                                    .child("users").child(user.getUid())
                                                    .child("list-purchases").keepSynced(true);
                                            */
                                                                if(!idProduct.isEmpty()) {

                                                                    Toast.makeText(context, R.string.message_1, Toast.LENGTH_LONG).show();
                                                                    ManagerSharedPreferences.editPreferences(context, "isSingIn", true);
                                                                    //JackyGiftsApplication.shareConceal.edit().putBoolean("isSingIn",true).apply();
                                                                    ManagerSharedPreferences.editPreferences(context,
                                                                            "showIntroGoFY", false);
                                                                    ManagerSharedPreferences.editPreferences(context,
                                                                            "idProduct", idProduct);
                                                                    //JackyGiftsApplication.shareConceal.edit().putBoolean("showIntroJackyApp",false).apply();
                                                                    activity.finish();
                                                                    activity.startActivity(new Intent(activity,
                                                                            HomeActivity.class));

                                                                }else{

                                                                    MainActivity mainActivity=(MainActivity) activity;
                                                                    mainActivity.containerFormRegisterUser.setVisibility(View.VISIBLE);
                                                                    Toast.makeText(context, "Ya casi terminamos :)", Toast.LENGTH_LONG).show();

                                                                }



                                                        }
                                                    }
                                                    // [START_EXCLUDE]
                                                    hideProgressDialog();
                                                    // [END_EXCLUDE]
                                                }
                                            });
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            // Getting Post failed, log a message
                                            FirebaseCrash.logcat(Log.WARN,TAG,databaseError.getDetails());
                                            FirebaseCrash.report(databaseError.toException());
                                        }
                                    });

                            ManagerSharedPreferences.editPreferences(context,
                                    "idUser",user.getUid());
                            //JackyGiftsApplication.shareConceal.edit().putString("idUser", user.getUid()).apply();


                        } else {

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]

                            // If sign in fails, display a message to the user.
                            FirebaseCrash.logcat(Log.ERROR,TAG,
                                    "Google Sign In failed firebase");
                            FirebaseCrash.report(task.getException());
                            Toast.makeText(context, R.string.message_error_1,
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }
    // [END auth_with_google]



    // [START signin]
    public void signIn() {
        showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]




    public ProgressDialog mProgressDialog;


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        FirebaseCrash.logcat(Log.ERROR,TAG,
                "Google Sign failed -onConnectionFailed:"
                        +connectionResult.getErrorMessage()+"errorCode:"+
        connectionResult.getErrorCode());
    }




}
