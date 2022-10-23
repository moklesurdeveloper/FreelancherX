package com.freelancing.x.mr.fragment;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.freelancing.x.R;
import com.freelancing.x.mr.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpellingContestFragment extends AppCompatActivity {


    @BindView(R.id.question)
    TextView question;
    int i = 1;
    int postion;
    @BindView(R.id.time_view)
    TextView timeView;
    CountDownTimer countDownTimer;
    int r = 0, w = 0;
    @BindView(R.id.passage2)
    TextView passage2;
    @BindView(R.id.timer)
    CardView timer;
    TextView position_question;

    ProgressDialog progressDialog;
    long total_question = 0;
    @BindView(R.id.oa)
    Button oa;
    @BindView(R.id.oc)
    Button oc;
    @BindView(R.id.ob)
    Button ob;
    @BindView(R.id.od)
    Button od;

    public SpellingContestFragment() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_contest_layout);
        ButterKnife.bind(this);

        Toolbar toolbar=findViewById(R.id.toolbar);
        position_question=toolbar.findViewById(R.id.position_question);
        buttonEnable(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        get_total();
        passage2.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(1000 * 60 * 30, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                timeView.setText(format.format(new Date(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                buttonEnable(false);
                getQuestion(1000000000);

            }
        }.start();


    }

    private void get_total() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin").child("contest").child("spelling");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                total_question = dataSnapshot.getChildrenCount();
                //Toast.makeText(, "total "+total_question , Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                getQuestion(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void buttonEnable(boolean b) {
        oa.setClickable(b);
        ob.setClickable(b);
        oc.setClickable(b);
        od.setClickable(b);
    }

    private void getQuestion(int i) {
        if (i <= total_question) {
            position_question.setText("Question: "+i + ":" + total_question);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin").child("contest").child("spelling").child(i + "");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ResetColor();
                    //   question.setText(Html.fromHtml(dataSnapshot.child("title").getValue().toString()));
                    question.setText("Choose The Correct Spelling Below");
                    postion = dataSnapshot.child("answer").getValue(Integer.class);
                      oa.setText(dataSnapshot.child("optionA").getValue().toString());
                      ob.setText(dataSnapshot.child("optionB").getValue().toString());
                      oc.setText(dataSnapshot.child("optionC").getValue().toString());
                      od.setText(dataSnapshot.child("optionD").getValue().toString());
                    buttonEnable(true);


                } else {
                    Builder dialogBuilder = new Builder(SpellingContestFragment.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.complete, null);
                    dialogBuilder.setView(dialogView);
                    TextView totalL = dialogView.findViewById(R.id.totalL);
                    TextView rightL=dialogView.findViewById(R.id.rightL);
                    TextView wrongL=dialogView.findViewById(R.id.wrongL);
                    Button home = dialogView.findViewById(R.id.home);
                    totalL.setText("Total Quiz:" + total_question);
                    rightL.setText("Right:" + r);
                    wrongL.setText("Wrong:" + w);
                    TextView txt = dialogView.findViewById(R.id.textView11);
                    txt.setText("You Have Successfully Completed Your Spelling Master Contest");
                    home.setOnClickListener(v -> {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("quiz_point").exists()) {
                                    int i = dataSnapshot.child("quiz_point").getValue(Integer.class);
                                    reference.child("quiz_point").setValue(i + r);

                                } else {
                                    reference.child("quiz_point").setValue(r);
                                }


                                if (dataSnapshot.child("spelling_point").exists()) {
                                    int i = dataSnapshot.child("spelling_point").getValue(Integer.class);
                                    reference.child("spelling_point").setValue(i + r);
                                } else {
                                    reference.child("spelling_point").setValue(r);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    });

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    try {
                        alertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @OnClick({R.id.oa, R.id.ob, R.id.oc, R.id.od})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oa:
                ResetColor();
                changeColor(1, 1);
                break;
            case R.id.ob:
                ResetColor();
                changeColor(2, 2);
                break;
            case R.id.oc:
                ResetColor();
                changeColor(3, 3);
                break;
            case R.id.od:
                ResetColor();
                changeColor(4, 4);
                break;
        }
    }

    private void changeColor(int option, int ans) {
        // option.setBackgroundColor(Color.YELLOW);
        //Do something after 100ms
        switch (option) {
            case 1:
                oa.setBackgroundResource(R.drawable.rounded_shape_w);;
                break;
            case 2:
                ob.setBackgroundResource(R.drawable.rounded_shape_w);;
                break;
            case 3:
                oc.setBackgroundResource(R.drawable.rounded_shape_w);;
                break;
            case 4:
                od.setBackgroundResource(R.drawable.rounded_shape_w);;
                break;
        }

        if (postion == ans) {


            switch (postion) {
                case 1:
                    oa.setBackgroundResource(R.drawable.rounded_shape_r);;
                    break;
                case 2:
                    ob.setBackgroundResource(R.drawable.rounded_shape_r);;
                    break;
                case 3:
                    oc.setBackgroundResource(R.drawable.rounded_shape_r);;
                    break;
                case 4:
                    od.setBackgroundResource(R.drawable.rounded_shape_r);;
                    break;
            }
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (ans == postion) {
                    i++;
                    getQuestion(i);
                    r += 1;
                } else {
                    i++;
                    getQuestion(i);
                    w += 1;
                }
            }
        }, 3000);


    }

    private void ResetColor() {
        buttonEnable(false);
        oa.setBackgroundResource(R.drawable.rounded_shape);
        ob.setBackgroundResource(R.drawable.rounded_shape);
        oc.setBackgroundResource(R.drawable.rounded_shape);
        od.setBackgroundResource(R.drawable.rounded_shape);
    }


}
