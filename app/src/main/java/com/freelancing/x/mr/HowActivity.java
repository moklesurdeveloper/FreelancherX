package com.freelancing.x.mr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freelancing.x.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HowActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.dis)
    TextView dis;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.progressBar4)
    ProgressBar progressBar4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howto);
        ButterKnife.bind(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/howto");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                title.setText(dataSnapshot.child("title").getValue().toString());
                web.loadUrl(dataSnapshot.child("des").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        web.
                setWebViewClient(new WebViewClient() {
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    }


                    @Override
                    public void onPageFinished(WebView view, String url) {
                        progressBar4.setVisibility(View.GONE);



                    }



    });
}
}