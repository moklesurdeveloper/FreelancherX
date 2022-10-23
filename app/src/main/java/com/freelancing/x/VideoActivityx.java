package com.freelancing.x;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class VideoActivityx extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    ProgressDialog progressDialog;
    String path;
       int     link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2574328611948823/6263965069");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
     path= getIntent().getStringExtra("name");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        setupDatabase();
        Log.i("123321", "my "+path);
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
              //  Intent intent=new Intent(getApplicationContext(), Main3Activity_x.class);
                Intent intent=new Intent(getApplicationContext(), FullscreenActivity.class);
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                intent.putExtra("link",link);
                startActivity(intent);
            }
        });
    }

    private void setupDatabase() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);
        Query query = FirebaseDatabase.getInstance().getReference(path);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<VideoModel>().setQuery(query, snapshot -> {
            try {
                return new VideoModel(snapshot.child("image").getValue().toString(), snapshot.child("name").getValue().toString(), snapshot.child("video").getValue(Integer.class));

            } catch (Exception e) {
                return new VideoModel(snapshot.child("image").getValue().toString(), snapshot.child("name").getValue().toString(),0);

            }
        }).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<VideoModel, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layoutx, parent, false));
            }

            @Override
            public void onDataChanged() {
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull VideoModel VideoModel) {
                viewHolder.details(VideoModel);
            }
        };

        recycler.setAdapter(adapter);
        adapter.startListening();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
ImageView cover;ImageView play;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           cover = itemView.findViewById(R.id.nice_video_player);
            play=itemView.findViewById(R.id.imageButton); }

        public void details(VideoModel videoModel) {
            Log.i("123321", "82"+videoModel.getImage()+"\n"+videoModel.getName()+"\n"+videoModel.getVideo());

          // or NiceVideoPlayer.TYPE_NATIVE


            //controller.setImage(getResources().getDrawable(R.drawable.ic_launcher_background));
            Log.i("123321", ""+videoModel.getImage());
            Glide.with(getApplicationContext())
                    .load(videoModel.getImage())
                    .placeholder(R.drawable.logo)
                    .transition(withCrossFade())
                    .into(cover);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     //   Intent intent=new Intent(getApplicationContext(), Main3Activity_x.class);
                        Intent intent=new Intent(getApplicationContext(), FullscreenActivity.class);
                        link=videoModel.getVideo();
                        intent.putExtra("link",videoModel.getVideo());
                        if(mInterstitialAd.isLoaded())mInterstitialAd.show();
                        else
                        startActivity(intent);

                    }
            });

            }

    }
    @Override
    protected void onStop() {
        super.onStop();
        // 在onStop时释放掉播放器

    }
    @Override
    public void onBackPressed() {
finish();
        super.onBackPressed();
    }}

