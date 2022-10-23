package com.freelancing.x;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class Main2Activity_x extends AppCompatActivity {

    VideoView video;
        String video_url = "https://firebasestorage.googleapis.com/v0/b/promoterapp-f101e.appspot.com/o/VMateLite_117183824.mp4?alt=media&token=85cb94da-9e39-4d31-9378-5eb14d11224b";//this one works if i use it
//   String video_url = "https://firebasestorage.googleapis.com/v0/b/promoterapp-f101e.appspot.com/o/61177186_408444939737660_1369181066534846464_n.jpg?alt=media&token=b52c7d3c-0e1c-43f5-abc9-d4a42819af52";
    //but this link from firebase does not work, i usually fetch it from online
    ProgressDialog pd;
    Button playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2x);

        video = (VideoView) findViewById(R.id.Videoview1);
        playButton = (Button) findViewById(R.id.playButton1);

        pd = new ProgressDialog(Main2Activity_x.this);
        pd.setMessage("Buffering video please wait...");
        pd.show();

        Uri uri = Uri.parse(video_url);
        video.setVideoURI(uri);

video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i("123321", ""+"onError ——> STATE_ERROR ———— what：" + what + ", extra: " + extra);
        return false;
    }
});
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //close the progress dialog when buffering is done
                pd.dismiss();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                video.start();
            }
        });
    }
}