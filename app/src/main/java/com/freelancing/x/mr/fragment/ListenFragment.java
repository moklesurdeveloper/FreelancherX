package com.freelancing.x.mr.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.freelancing.x.R;
import com.freelancing.x.mr.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListenFragment extends AppCompatActivity {


    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.passage)
    TextView passage;
    @BindView(R.id.abc)
    RelativeLayout abc;
    @BindView(R.id.oa)
    Button oa;
    @BindView(R.id.oc)
    Button oc;
    @BindView(R.id.ob)
    Button ob;
    @BindView(R.id.od)
    Button od;
    @BindView(R.id.question)
    TextView question2;

    private Button btn;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private boolean initialStage = true;
    int i = 1, w = 0, r = 0;
    int postion;
    String link;

    long total_question = 0;
    TextView position_question;
    int current = 0;
    ProgressDialog progressDialog;


    public ListenFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_listen);

        Toolbar toolbar=findViewById(R.id.toolbar);
        position_question=toolbar.findViewById(R.id.position_question);
        ButterKnife.bind(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressBar.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        get_total();
        buttonEnable(false);

    }

    private void get_total() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin").child("listen");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                total_question = dataSnapshot.getChildrenCount();
                progressDialog.dismiss();
                getQuestion(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.play, R.id.oa, R.id.ob, R.id.oc, R.id.od})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play:
                playAudio(link);

                break;
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

    private void playAudio(String link) {
       // Log.i("123321", "152: "+link);
        {
            if (!playPause) {
                // btn.setText("Pause Streaming");
                play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));

                if (initialStage) {
                    new Player().execute(link);

                    int audioSessionId = mediaPlayer.getAudioSessionId();
                    if (audioSessionId != -1)
                        if (audioSessionId != AudioManager.ERROR) {
                            //          wave2.setAudioSessionId(audioSessionId);

                        }


                } else {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();

                }

                playPause = true;

            } else {
                // btn.setText("Launch Streaming");
                //  Toast.makeText(getActivity(), "launch", Toast.LENGTH_SHORT).show();
                play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp));

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }

                playPause = false;
            }
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    initialStage = true;
                    playPause = false;
                    //   btn.setText("Launch Streaming");
                    // Toast.makeText(getActivity(), "launch 2", Toast.LENGTH_SHORT).show();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp));


                    mediaPlayer.stop();
                    mediaPlayer.reset();
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.INVISIBLE);
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));


            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp));

            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    private void buttonEnable(boolean b) {
        oa.setClickable(b);
        ob.setClickable(b);
        oc.setClickable(b);
        od.setClickable(b);
    }

    private void getQuestion(int i) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp));

            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        if (i <= total_question) {
            position_question.setText("Question: "+i + ":" + total_question);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin").child("listen").child(i + "");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ResetColor();
                    link = dataSnapshot.child("link").getValue().toString();
                    postion = dataSnapshot.child("answer").getValue(Integer.class);
                    oa.setText(dataSnapshot.child("optionA").getValue().toString());
                    question2.setText(dataSnapshot.child("question").getValue().toString());

                    ob.setText(dataSnapshot.child("optionB").getValue().toString());
                    oc.setText(dataSnapshot.child("optionC").getValue().toString());
                    od.setText(dataSnapshot.child("optionD").getValue().toString());
                    passage.setText(dataSnapshot.child("passage").getValue().toString());
                    buttonEnable(true);
                    //    playAudio(link);

                } else {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListenFragment.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.complete2, null);
                    dialogBuilder.setView(dialogView);
                    TextView totalL = dialogView.findViewById(R.id.totalL);
                    TextView rightL=dialogView.findViewById(R.id.rightL);
                    TextView wrongL=dialogView.findViewById(R.id.wrongL);
                    Button home = dialogView.findViewById(R.id.home);
                   TextView txt = dialogView.findViewById(R.id.textView11);
                    txt.setText("You Have Successfully Completed Your Listening Practice");
                    totalL.setText("Total Quiz:" + total_question);
                    rightL.setText("Right:" + r);
                    wrongL.setText("Wrong:" + w);
                    home.setOnClickListener(v -> {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    });

                    try {
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setCancelable(false);
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


    private void changeColor(int option, int ans) {
        oa.setBackgroundResource(R.drawable.rounded_shape);
        ob.setBackgroundResource(R.drawable.rounded_shape);
        oc.setBackgroundResource(R.drawable.rounded_shape);
        od.setBackgroundResource(R.drawable.rounded_shape);
        // option.setBackgroundColor(Color.YELLOW);
        final Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            //Do something after 100ms
            switch (option) {
                case 1:
                    oa.setBackgroundResource(R.drawable.rounded_shape_w);
                    break;
                case 2:
                    ob.setBackgroundResource(R.drawable.rounded_shape_w);
                    break;
                case 3:
                    oc.setBackgroundResource(R.drawable.rounded_shape_w);
                    break;
                case 4:
                    od.setBackgroundResource(R.drawable.rounded_shape_w);
                    break;
            }

            if (postion == ans) {


                switch (postion) {
                    case 1:
                        oa.setBackgroundResource(R.drawable.rounded_shape_r);
                        break;
                    case 2:
                        ob.setBackgroundResource(R.drawable.rounded_shape_r);
                        break;
                    case 3:
                        oc.setBackgroundResource(R.drawable.rounded_shape_r);
                        break;
                    case 4:
                        od.setBackgroundResource(R.drawable.rounded_shape_r);
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

        }, 800);

    }

    private void ResetColor() {
        buttonEnable(false);
        oa.setBackgroundResource(R.drawable.rounded_shape);
        ob.setBackgroundResource(R.drawable.rounded_shape);
        oc.setBackgroundResource(R.drawable.rounded_shape);
        od.setBackgroundResource(R.drawable.rounded_shape);
    }

    @Override
    protected void onDestroy() {

        try {
            mediaPlayer.release();
            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
            super.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            mediaPlayer.release();
       super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }
}




