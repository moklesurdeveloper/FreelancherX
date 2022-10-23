package com.freelancing.x;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freelancing.x.mr.MainActivity;
import com.freelancing.x.mr.Notify;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.edittext)
    EditText edittext;
String source,question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ButterKnife.bind(this);
        source=getIntent().getStringExtra("text");
        question=getIntent().getStringExtra("question");
        text.setText(question);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String finalText=edittext.getText().toString().replace("\n","<br>");
        String fText=source+"<br><br>"+question+"<br><br>"+finalText;
        Map<String, Object> map = new HashMap<>();
        long time = System.currentTimeMillis();
        Long updated = 9999999999999L;
        long net=updated-time;
        ProgressDialog progressDialog = new ProgressDialog(NewActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("approve").child((net) + "");
        map.put("time", time);
        map.put("text", fText);
        map.put("post_id", databaseReference.getKey());
        map.put("updated", updated - time);
        map.put("category", "Q&A");
        map.put("name", user.getDisplayName());
        map.put("user_id", user.getUid());
        map.put("image", user.getPhotoUrl() + "");
        map.put("post_image","");
       
        //    Log.i("123321", user.getPhotoUrl() + "");
        Notify.subscribe((updated - time) + "owner");
        databaseReference.setValue(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Successfully submitted for review", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        
                    }
                });
    }
}
