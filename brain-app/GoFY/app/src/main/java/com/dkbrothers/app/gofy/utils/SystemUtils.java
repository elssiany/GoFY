package com.dkbrothers.app.gofy.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kevin on 14/12/2016.
 */

public class SystemUtils {


    public static int numRandom(int desde,int hasta){
        return (int) Math.floor(Math.random()*(hasta-desde+1)+desde);
    }


    /**
     *
     * @return yyyy-MM-dd HH:mm:ss formate date as string
     */
    public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateFormat.format(new Date()); // Find todays date
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static long addDaysInDateGetTimeInMillis(int days){

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date currentDate = new Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();

        c.setTime(currentDate);
        // manipulate date
        //c.add(Calendar.YEAR, 1);
        //c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, 1);
        c.add(Calendar.DAY_OF_MONTH,days+1);
        //c.add(Calendar.HOUR, 1);
        //c.add(Calendar.MINUTE, 1);
        //c.add(Calendar.SECOND, 1);
        // convert calendar to date
        //Date currentDatePlusOne = c.getTime();
        return c.getTimeInMillis();
    }


    public static  long addDaysInDateGetTimeInMillis(Date currentDate,int hour){

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        if(currentDate==null)
            currentDate = new Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();

        c.setTime(currentDate);
        // manipulate date
        //c.add(Calendar.YEAR, 1);
        //c.add(Calendar.MONTH, 1);
        //c.add(Calendar.DATE, 1);
        //c.add(Calendar.DAY_OF_MONTH,days+1);
        c.add(Calendar.HOUR, hour);
        //c.add(Calendar.MINUTE, 4);
        //c.add(Calendar.SECOND, 1);
        // convert calendar to date
        //Date currentDatePlusOne = c.getTime();
        return c.getTimeInMillis();
    }



    public static Date addDaysInDateGetDate(int days){

        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        Date currentDate = new Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();

        c.setTime(currentDate);
        // manipulate date
        //c.add(Calendar.YEAR, 1);
        //c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, 1);
        c.add(Calendar.DAY_OF_MONTH,days);
        //c.add(Calendar.HOUR, 1);
        //c.add(Calendar.MINUTE, 1);
        //c.add(Calendar.SECOND, 1);
        // convert calendar to date
        //Date currentDatePlusOne = c.getTime();
        return c.getTime();
    }




    public static Date getDate(long dateInMillis){
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return  new Date(dateInMillis);
    }

    public static boolean versionAtLeast(int version) {
        return Build.VERSION.SDK_INT >= version;
    }


    //verificar si hay acceso a internet
    public static boolean haveNetwork(Context context) {
        boolean enabled = true;
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if((info == null || !info.isConnected() || !info.isAvailable())) {
            enabled = false;
        }
        return enabled;
    }


    /** Open another app.
     * @param context current Context, like Activity, App, or Service
     * @param packageName the full package name of the app to open
     * @return true if likely successful, false if unsuccessful
     */
    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
            //throw new PackageManager.NameNotFoundException();
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }



    /*
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


*/


}
