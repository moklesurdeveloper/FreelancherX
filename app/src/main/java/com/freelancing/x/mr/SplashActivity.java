package com.freelancing.x.mr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.freelancing.x.R;
import com.freelancing.x.mr.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        final Handler handler = new Handler();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("admin/isUpdating");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("123321", "33:"+dataSnapshot);
                if(dataSnapshot.getValue(Boolean.class)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("Maintenance Break");
                    builder.setMessage("Please try agian later");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                    builder.show();
                }
                else {



                    handler.postDelayed(() -> {
                        //Do something after 100ms
                        if (FirebaseAuth.getInstance().getCurrentUser()==null) {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));

                        }
                        else
                     {//
                     DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("role");
                     databaseReference1.addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             if(dataSnapshot.exists()){
                                 Utils.role=dataSnapshot.getValue().toString();
                                 startActivity(new Intent(getApplicationContext(), MainActivity.class));
                             }
                             else {
                                 startActivity(new Intent(getApplicationContext(), MainActivity.class));

                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });
                     }

                    }, 2000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        }

}
