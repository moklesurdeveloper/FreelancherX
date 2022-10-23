package com.freelancing.x.mr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freelancing.x.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlyerActivity extends AppCompatActivity {

    @BindView(R.id.frag_video)
    YouTubePlayerView youtubePlayerView;
    String videoId = "";
    YouTubePlayer youTubePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plyer);
        videoId=getIntent().getStringExtra("id");
        ButterKnife.bind(this
        );

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer0) {
youTubePlayer=youTubePlayer0;
                youTubePlayer0.loadVideo(videoId, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            youTubePlayer.pause();
            super.onBackPressed();

        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }
}
