package com.freelancing.x;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity {
    FirebaseRecyclerAdapter adapter;
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.layout)
    RelativeLayout layout;
    boolean full = false, nvision = false;
    int size = 18;
    String stitle, path;

    TextToSpeech t1;
int counter=0;

    int i = 1;
    int w = 0, r = 0;
    int postion;
    long total_question = 0;

    int current = 0;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    String child;
    DatabaseReference databaseReference;
    int rightServer, wrongSrver, totalServer;


    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postx);
        ButterKnife.bind(this);
        stitle = getIntent().getStringExtra("title");
        path = getIntent().getStringExtra("name");
        title = toolbar.findViewById(R.id.title);

        child = path + "/" + "question";
        title.setText(stitle);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);
        setupRecycler();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(path).child("text");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRecycler() {
        Query query=FirebaseDatabase.getInstance().getReference(path).child("question");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<QuestionModel>().setQuery(query,
                snapshot -> new QuestionModel(snapshot.getKey())).build();

        adapter = new FirebaseRecyclerAdapter<QuestionModel,ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull QuestionModel questionModel) {
                viewHolder.setup(questionModel.getChild());
            }
        };
        recycler.setAdapter(adapter);
        adapter.startListening();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {


        switch (item.getItemId()) {
//            case R.id.full:
//                if (full == false) {
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    toolbar.setVisibility(View.GONE);
//                    navigation.setVisibility(View.GONE);
//                    Toast.makeText(this, "Press Back to Exit FullScreen", Toast.LENGTH_SHORT).show();
//                    full = true;
//                }
//                break;
//            case R.id.zoomIn:
//                size++;
//                text.setTextSize(size);
//                break;
//            case R.id.zoomOut:
//                size--;
//                text.setTextSize(size);
//                break;


        }


        return false;
    };

    @Override
    public void onBackPressed() {
        if (full == true) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            toolbar.setVisibility(View.VISIBLE);
            navigation.setVisibility(View.VISIBLE);
            full = false;

        } else {
            super.onBackPressed();
        }
    }











    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.oa)
        Button oa;
        @BindView(R.id.oc)
        Button oc;
        @BindView(R.id.ob)
        Button ob;
        @BindView(R.id.od)
        Button od;
        @BindView(R.id.position_question)
        TextView position_question;
        @BindView(R.id.question)
        TextView question;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public  void setup(String s){
            getQuestion(Integer.parseInt(s));

        }
        private void getQuestion(int i) {

            if (i <= total_question) {

            }
            ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
            progressDialog.setMessage("loading");
            progressDialog.show();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(child).child(i + "");


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        position_question.setText(" Question: " + i + "");
                        ResetColor();
                        question.setText((dataSnapshot.child("question").getValue().toString()));
                        String s = dataSnapshot.child("answer").getValue().toString();
                        String oat = dataSnapshot.child("option1").getValue().toString();
                        oa.setText(oat);
                        String obt = dataSnapshot.child("option2").getValue().toString();
                        ob.setText(obt);
                        String oct = dataSnapshot.child("option3").getValue().toString();
                        oc.setText(oct);
                        String odt = dataSnapshot.child("option4").getValue().toString();
                        od.setText(odt);

                        if (s.equals(oat))
                            postion = 1;
                        else if (s.equals(obt))
                            postion = 2;
                        else if (s.equals(oct))
                            postion = 3;
                        else if (s.equals(odt))
                            postion = 4;

                        buttonEnable(true);
                        progressDialog.dismiss();

                    } else {

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostActivity.this);
                        // ...Irrelevant code for customizing the buttons and title
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.complete2, null);
                        dialogBuilder.setView(dialogView);

                        TextView totalL = dialogView.findViewById(R.id.totalL);
                        TextView rightL = dialogView.findViewById(R.id.rightL);
                        TextView wrongL = dialogView.findViewById(R.id.wrongL);
                        Button home = dialogView.findViewById(R.id.home);
                        TextView txt = dialogView.findViewById(R.id.textView11);
                        txt.setText("You Have Successfully Completed ");
                        totalL.setText("Total Quiz:" + total_question);
                        rightL.setText("Right:" + r);
                        wrongL.setText("Wrong:" + w);


                        home.setOnClickListener(v -> {
                            startActivity(new Intent(getApplicationContext(), MainActivity_x.class));
                        });

                        AlertDialog alertDialog = dialogBuilder.create();
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
        private void ResetColor() {
            buttonEnable(false);
            oa.setBackgroundResource(R.drawable.rounded_shape);
            ob.setBackgroundResource(R.drawable.rounded_shape);
            oc.setBackgroundResource(R.drawable.rounded_shape);
            od.setBackgroundResource(R.drawable.rounded_shape);
        }
        private void changeColor(int option, int ans) {
            oa.setBackgroundResource(R.drawable.rounded_shape);
            ob.setBackgroundResource(R.drawable.rounded_shape);
            oc.setBackgroundResource(R.drawable.rounded_shape);
            od.setBackgroundResource(R.drawable.rounded_shape);
            // option.setBackgroundColor(Color.YELLOW);

            //Do something after 100ms
            switch (option) {
                case 1:
                    oa.setBackgroundResource(R.drawable.rounded_shape_w);
                    break;
                case 2:
                    ob.setBackgroundResource(R.drawable.rounded_shape_w);
                    ;
                    break;
                case 3:
                    oc.setBackgroundResource(R.drawable.rounded_shape_w);
                    ;
                    break;
                case 4:
                    od.setBackgroundResource(R.drawable.rounded_shape_w);
                    ;
                    break;
            }
            if (postion == ans) {


                switch (postion) {
                    case 1:
                        oa.setBackgroundResource(R.drawable.rounded_shape_r);
                        ;
                        break;
                    case 2:
                        ob.setBackgroundResource(R.drawable.rounded_shape_r);
                        ;
                        break;
                    case 3:
                        oc.setBackgroundResource(R.drawable.rounded_shape_r);
                        ;
                        break;
                    case 4:
                        od.setBackgroundResource(R.drawable.rounded_shape_r);
                        ;
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
                        r += 1;
                      counter++;
                      if(counter>=adapter.getItemCount())getQuestion(999);


                    } else {
                        i++;
                        w += 1;
                        counter++;
                        if(counter>=adapter.getItemCount())getQuestion(999);



                    }
                }
            }, 1000);


        }
        private void buttonEnable(boolean b) {
            oa.setClickable(b);
            ob.setClickable(b);
            oc.setClickable(b);
            od.setClickable(b);
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
    }
}
