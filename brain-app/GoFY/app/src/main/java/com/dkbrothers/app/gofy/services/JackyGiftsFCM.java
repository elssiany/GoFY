/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dkbrothers.app.gofy.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.dkbrothers.app.gofy.R;
import com.dkbrothers.app.gofy.activities.MainActivity;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class JackyGiftsFCM extends FirebaseMessagingService {

    private static final String TAG = "JackyGiftsFCM";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        //Hay dos tipos de mensajes: mensajes de datos y mensajes de notificación. Los mensajes de datos se manejan
        // aquí en onMessageReceived si la aplicación está en primer plano o en segundo plano. Los mensajes de datos son del tipo
        // tradicionalmente utilizado con GCM. Los mensajes de notificación solo se reciben aquí en onMessageReceived cuando la aplicación
        //está en primer plano. Cuando la aplicación está en segundo plano se muestra una notificación generada automáticamente.
        //Cuando el usuario teclea la notificación, se devuelve a la aplicación. Mensajes que contienen tanto la notificación
        //y las cargas útiles de datos se tratan como mensajes de notificación. La consola Firebase siempre envía una notificación
        //mensajes. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // ¿No recibes mensajes aquí? Vea por qué esto puede ser: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Compruebe si el mensaje contiene una carga de datos.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if(/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if(remoteMessage.getData() != null) {
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getData());
            //String typeNotification=remoteMessage.getData().get("typeNotification");

                    sendAlertNotification(remoteMessage);

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {

        //[START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(JackyGiftsJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        //[END dispatch_job]

    }


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     *
     */
    private void sendAlertNotification(RemoteMessage remoteMessage) {

        //UUID.randomUUID().hashCode() para generar Ids
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Map<String,String> data = remoteMessage.getData();
        String title,body,bigText,summaryText;
            title=data.get("title");
            body=data.get("body");
            bigText=data.get("bigContent");
            summaryText=data.get("summaryText");
        intent.putExtra("nameNodeMSM",data.get("nameNodeMSM"));
            //intent.putExtra("showFragmentRewards",true);
            //intent.putExtra("urlPage",data.get("urlPage"));
            intent.putExtra("dataExtra1",data.get("dataExtra1"));
            intent.putExtra("dataExtra2",data.get("dataExtra2"));
            intent.putExtra("dataExtra3",data.get("dataExtra3"));
        intent.putExtra("typeNotification",data.get("typeNotification"));

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        // Big Text Style
        final NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(notificationBuilder);
        style.bigText(bigText)
                .setBigContentTitle(title).setSummaryText(summaryText);
        BitmapDrawable contactPicDrawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),
                R.mipmap.ic_launcher);
        Bitmap iconLarge = contactPicDrawable.getBitmap();

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                764/* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);//
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                 notificationBuilder.setContentTitle(title)
                .setContentText(bigText)
                .setTicker(body)
                         .setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
                .setAutoCancel(true)
                .setLargeIcon(iconLarge).setStyle(style)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     *
     */
    private void sendOffersNotification(RemoteMessage remoteMessage) {

        //UUID.randomUUID().hashCode() para generar Ids
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Map<String,String> data = remoteMessage.getData();
        String title="",body="",bigText="",summaryText="";
        if (data != null) {
            title=data.get("title");
            body=data.get("body");
            bigText=data.get("bigContent");
            summaryText=data.get("summaryText");
            intent.putExtra("nameNodeMSM",data.get("nameNodeMSM"));
            //intent.putExtra("showFragmentRewards",true);
            //intent.putExtra("urlPage",data.get("urlPage"));
            //intent.putExtra("image_alert",data.get("image_alert"));
            //intent.putExtra("bigContent",data.get("bigContent"));
            //intent.putExtra("title",title);
            //intent.putExtra("body",body);
            intent.putExtra("dataExtra1",data.get("dataExtra1"));
            intent.putExtra("dataExtra2",data.get("dataExtra2"));
            intent.putExtra("dataExtra3",data.get("dataExtra3"));
            intent.putExtra("typeNotification",data.get("typeNotification"));
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        // Big Text Style
        final NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(notificationBuilder);
        style.bigText(bigText)
                .setBigContentTitle(title).setSummaryText(summaryText);
        BitmapDrawable contactPicDrawable = (BitmapDrawable) ContextCompat.getDrawable(getApplicationContext(),
                R.mipmap.ic_launcher);
        Bitmap iconLarge = contactPicDrawable.getBitmap();

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                764/* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);//
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setContentTitle(title)
                .setContentText(bigText)
                .setTicker(body)
                .setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
                .setAutoCancel(true)
                .setLargeIcon(iconLarge).setStyle(style)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }








}
