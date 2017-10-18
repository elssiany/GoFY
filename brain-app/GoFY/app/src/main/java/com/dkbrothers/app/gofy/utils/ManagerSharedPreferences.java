package com.dkbrothers.app.gofy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by dilan on 15/12/16.
 */

public class ManagerSharedPreferences extends Activity {


    public static final String MY_PREFERENCE = "data_gofy";


    public static void editPreferences(final Context activity, final String idPreference, final int dato) {

        //JackyGiftsApplication.shareConceal.edit().putInt(idPreference,dato).apply();

        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
        editor.putInt(idPreference,dato);
        editor.apply();

        //JackyGiftsApplication.shareConceal.edit().putInt(idPreference,dato).apply();
        //SharedPreferences preferencs = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        //saveTheme=preferencs.getInt("theme",0);

    }

    public static void editPreferences(Context activity, String idPreference, String dato) {
        //JackyGiftsApplication.shareConceal.edit().putString(idPreference,dato).apply();

        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
        editor.putString(idPreference, dato);
        editor.apply();
        //JackyGiftsApplication.shareConceal.edit().putString(idPreference,dato).apply();
        //SharedPreferences preferencs = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        //saveTheme=preferencs.getInt("theme",0);

    }

    public static void editPreferences(Context activity, String idPreference, boolean dato) {
        //JackyGiftsApplication.shareConceal.edit().putBoolean(idPreference,dato).apply();

        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
        editor.putBoolean(idPreference,dato);
        editor.apply();
        //JackyGiftsApplication.shareConceal.edit().putBoolean(idPreference,dato).apply();
        //SharedPreferences preferencs = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        //saveTheme=preferencs.getInt("theme",0);

    }

    public static void editPreferences(final Context activity, final String idPreference, final float dato) {
        //JackyGiftsApplication.shareConceal.edit().putFloat(idPreference,dato).apply();

        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE).edit();
        editor.putFloat(idPreference,dato);
        editor.apply();
        //SharedPreferences preferencs = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        //saveTheme=preferencs.getInt("theme",0);

    }



    public static int getPreferences(final Context activity,final String  idPreference,final int dato) {
        //return JackyGiftsApplication.shareConceal.getInt(idPreference,dato);
        return activity.getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE).getInt( idPreference,dato);
        //la variable <dato> es para asignarle al sharedPreference un dato en caso de que ese
        // sharedPreference se llame por promera vez;
    }

    public static float getPreferences(final Context activity,final String  idPreference,final float dato) {
        //return JackyGiftsApplication.shareConceal.getFloat(idPreference,dato);
        return activity.getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE).getFloat(idPreference,dato);
        //la variable <dato> es para asignarle al sharedPreference un dato en caso de que ese
        // sharedPreference se llame por promera vez;
    }

    public static boolean getPreferences(final Context activity,String  idPreference,boolean dato) {
        //return JackyGiftsApplication.shareConceal.getBoolean(idPreference,dato);

        SharedPreferences preferences = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        //la variable <dato> es para asignarle al sharedPreference un dato en caso de que ese
        // sharedPreference se llame por promera vez
        return preferences.getBoolean(idPreference, dato);

    }

    public static String getPreferences(final Context activity, String  idPreference, String dato) {
        //return JackyGiftsApplication.shareConceal.getString(idPreference,dato);

        SharedPreferences preferences = activity.getSharedPreferences(MY_PREFERENCE,MODE_PRIVATE);
        //la variable <dato> es para asignarle al sharedPreference un dato en caso de que ese
        // sharedPreference se llame por promera vez
        return preferences.getString(idPreference, dato);

    }


}
