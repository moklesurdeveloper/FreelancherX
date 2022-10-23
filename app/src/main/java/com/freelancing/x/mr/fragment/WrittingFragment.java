package com.freelancing.x.mr.fragment;


import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.freelancing.x.R;
import com.freelancing.x.mr.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class WrittingFragment extends AppCompatActivity {


    @BindView(R.id.comment_edt)
    EditText commentEdt;
    @BindView(R.id.comment_post)
    TextView commentPost;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.orginal)
    TextView orginal;
    @BindView(R.id.scroll)
    ScrollView scroll;
    String s;
    String t;
    String source;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.fragment_writting);

        ButterKnife.bind(this);
        source=getIntent().getStringExtra("mode");
        if(source.equals("e2b")){
            t="English to Bangla";
            from.setText(t);
        }
        else {
            t="Bangla to English";
            from.setText(t);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin").child("translate").child(source);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                s=dataSnapshot.child("text").getValue().toString();
                orginal.setText(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @OnClick(R.id.comment_post)
    public void onViewClicked() {
        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading");
            progressDialog.show();
            long time = System.currentTimeMillis();
            String text = commentEdt.getText().toString();
            if (!text.isEmpty()) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                String fText=text+"\n\n"+"<p style=\"color:gray;\">"+"Translated from <b>"+t+"</b><br><br><i>"+s+"</i></p>";

                Map<String, Object> map = new HashMap<>();
                Long updated = 9999999999999L;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child((updated-time)+"");
                map.put("time", time);
                map.put("text", fText);
                map.put("post_id", databaseReference.getKey());
                map.put("updated", updated - time);
                map.put("category", "Translation");
                map.put("name", user.getDisplayName());
                map.put("user_id", user.getUid());
                map.put("image", user.getPhotoUrl() + "");
                //Log.i("123321", user.getPhotoUrl() + "");

                databaseReference.setValue(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Successfully posted", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "Please Write Your Message", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            Builder builder = new Builder(this);
            builder.setTitle("Somethings want Wrong");
            builder.setMessage(""+e.getMessage()   );
            builder.setPositiveButton("ok", null    );
            builder.show();
        }
    }
}
