package com.freelancing.x.admin;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;

public class Notify2 {
    public  static List<String>key_list=new ArrayList<>();
    public  static List<String>data_list=new ArrayList<>();
    public static DatabaseReference path= FirebaseDatabase.getInstance().getReference("post");
    public static  String u_id="";
    public static String message;


    public static   void notify(final String post_id, final String type, final String kind,  final String user_id,final  String message0) {
        message=message0;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user_id).child("notification").push();
                    databaseReference.child("message").setValue(message);
                    databaseReference.child("kind").setValue(kind);
                    databaseReference.child("link").setValue(post_id);
                    databaseReference.child("time").setValue(System.currentTimeMillis());
                    databaseReference.child("type").setValue(type);

                    Log.i("123321", "81: " + user_id);
                    Log.i("123321", "80:SendIt");
                    Log.i("123321", "79:" + type);
                    if (kind.equals("post"))
    SendIt(message, post_id + "owner", user_id);
                    else
    SendIt(message, post_id + "owner", user_id);}
    public static void subscribe(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        Log.i("123321", "135: suscribe +:"+topic);
    }
    public static void SendIt(String message, final String topic, String id){
        message=message.replace("<b>","");
        message=message.replace("</b>","");
        Log.i("123321", "139:"+id+" Topic:"+topic);

        FirebasePush firebasePush = new FirebasePush( "AAAAHEEQ5TY:APA91bHs8UVSB1ynEZrS3VOATP49ixtItLALzyQ6zrRxjirivxUPSbvCcfTlq92ShUMVQeNM4TxGR238yrIVIF9Su0foAG1XzXc4qpXdI2F--DDoFoDzdquI-IQPpRlfTGRtsu-fdIqq");
        firebasePush.setAsyncResponse(new PushNotificationTask.AsyncResponse() {
            @Override
            public void onFinishPush(@NotNull String ouput) {
                Log.e("OUTPUT", ouput + " To:" + topic);
            }
        });
        firebasePush.setNotification(new Notification("Freelancing X",message));




// Send to topic
        firebasePush.sendToTopic(topic);
    }
}
