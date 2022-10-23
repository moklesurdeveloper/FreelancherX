package com.freelancing.x.mr;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fcm.androidtoandroid.FirebasePush;
import fcm.androidtoandroid.connection.PushNotificationTask;
import fcm.androidtoandroid.model.Notification;

public class Notify {
    public  static List<String>key_list=new ArrayList<>();
    public  static List<String>data_list=new ArrayList<>();
    public static DatabaseReference path= FirebaseDatabase.getInstance().getReference("post");
    public  static String u_id= FirebaseAuth.getInstance().getUid();


    public static   void notify(String post_id,String link,String total,String type,String kind,String user,String user_id,String key){

        Log.i("123321", "user_id: "+user_id+" orginal id: "+u_id);
        Query query=path.child(link+"/"+type);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("123321", "41:link:"+
                        link+"/"+type);
                Log.i("123321", "43:"+dataSnapshot);
                String lastname = "";
                int count;
                String count_message;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (type.equals("like") || type.equals("like2")) {
                        String key = ds.getKey();
                        String data = ds.getValue().toString();
                        key_list.add(key);
                        data_list.add(data);
                    } else {
                        String key = ds.child("id").getValue().toString();
                        String data = ds.child("name").getValue().toString();
                        key_list.add(key);
                        data_list.add(data);
                    }


                }
                // lastname  =(key_list.get(key_list.size()-1).equals(u_id))?"You ":data_list.get(data_list.size()-1);
                lastname = data_list.get(data_list.size() - 1);
                Set<String> counter = new HashSet<>(data_list);
                Set<String> sKey = new HashSet<>(key_list);
                List<String> fskey = new ArrayList<>(sKey);

                count = counter.size() - 1;
                count_message = count >= 1 ? " and " + count + " other also" : "";
                String username = user_id.equals(u_id)? " Your " : user + "'s";
                username=type.equals("like")||type.equals("like2")?"Your":username;
                String type2 = type.equals("like2") ? "react" : type;
                String message = "<b>" + lastname + "</b>" + count_message + " " + type2 + " " + username + " " + kind;

                if (type.equals("like") || type.equals("like2") ) {
                    if(!user_id.equals(u_id)){
                    Log.i("123321", u_id + "notifying " + user_id);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user_id).child("notification").push();
                    databaseReference.child("message").setValue(message);
                    databaseReference.child("kind").setValue(kind);
                    databaseReference.child("link").setValue(link);
                    databaseReference.child("time").setValue(System.currentTimeMillis());
                    databaseReference.child("type").setValue(type);

                    Log.i("123321", "81: " + user_id);
                    Log.i("123321", "80:SendIt");
                    Log.i("123321", "79:" + type);
                    if (kind.equals("post"))
                        SendIt(message, post_id + "owner", user_id);
                    else
                        SendIt(message, key + "owner", user_id);

                }
            }
                      else if(type.equals("comment")||type.equals("replay")){
                          for(int i=0;i<fskey.size();i++){
                              if(!fskey.get(i).equals(u_id)&&!fskey.get(i).equals(user_id))
                              {  Log.i("123321", "84:notifying "+user_id);
                              String fType=type.equals("comment")?"commented":"replied";
                                username=key_list.get(i).equals(user_id)?"Your":user+"'s";
                                message="<b>"+lastname +"</b>"+count_message+" "+fType+" "+username+" "+kind;
                                  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(fskey.get(i)).child("notification").push();
                                  databaseReference.child("message").setValue(message);
                                  databaseReference.child("kind").setValue(kind);
                                  databaseReference.child("link").setValue(link);
                                  databaseReference.child("time").setValue(System.currentTimeMillis());
                                  databaseReference.child("type").setValue(type);
                                  DatabaseReference databaseReference0 = FirebaseDatabase.getInstance().getReference("user").child(user_id).child("count").push();
                                  databaseReference0.setValue(System.currentTimeMillis());
                                  Log.i("123321", "104: "+fskey.get(i));

                              }
                              else {
                       //           Log.i("123321", "84: "+type);
                              }

                          }
                          Log.i("123321", "103:SendIt");
                          SendIt(message,post_id+"viewer",user_id);
                          if(kind.equals("post")||kind.equals("comment")){

                              if(!user_id.equals(u_id)){
                              Log.i("123321", "90:notifying "+user_id);

                          username="your";
                              String fType=type.equals("comment")?"also commented":"also replied";
                          message="<b>"+lastname +"</b>"+count_message+" "+fType+" "+username+" "+kind;
                          DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user_id).child("notification").push();
                          databaseReference.child("message").setValue(message);
                          databaseReference.child("kind").setValue(kind);
                          databaseReference.child("key").setValue(key);
                          databaseReference.child("link").setValue(link);
                          databaseReference.child("time").setValue(System.currentTimeMillis());
                          databaseReference.child("type").setValue(type);
                                  DatabaseReference databaseReference0 = FirebaseDatabase.getInstance().getReference("user").child(user_id).child("count").push();
                                  databaseReference0.setValue(System.currentTimeMillis());
                                  Log.i("123321", "119:SendIt");
                                  SendIt(message,post_id+"owner",user_id);
                          }

                }

                      }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public static void subscribe(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        Log.i("123321", "135: suscribe +:"+topic);
    }
    public static void SendIt(String message, String topic,String id){
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
