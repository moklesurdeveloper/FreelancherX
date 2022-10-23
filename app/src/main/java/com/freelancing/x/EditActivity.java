package com.freelancing.x;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.freelancing.x.mr.MainActivity;
import com.freelancing.x.mr.Notify;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditActivity extends AppCompatActivity {

    @BindView(R.id.post_button)
    TextView postButton;
    @BindView(R.id.write)
    EditText write;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.remove)
    ImageView remove;
    @BindView(R.id.image_progress)
    ProgressBar imageProgress;
    @BindView(R.id.relativeLayout)
    ConstraintLayout relativeLayout;
    String image_link="";
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postx);

        ButterKnife.bind(this);
        postButton.setText("Update");
        image_link=getIntent().getStringExtra("image");
        if(!image_link.equals("")){
            Picasso.get().load(image_link).into(photo, new Callback() {
                @Override
                public void onSuccess() {
                    remove.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        write.setText(getIntent().getStringExtra("text"));

    }

    @OnClick(R.id.post_button)
    public void onViewClicked() {
        Log.i("123321", "351:"+image_link);
        // DO SOMETHINGS
        long time = System.currentTimeMillis();
        Long updated = 9999999999999L;
        long net=updated-time;
        ProgressDialog progressDialog = new ProgressDialog(EditActivity.this);
        progressDialog.setMessage("loading");
        progressDialog.show();

        String text = write.getText().toString().replaceAll("\n", "<br>");

        if (!text.isEmpty()||!image_link.equals("")) {

            Map<String, Object> map = new HashMap<>();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(getIntent().getStringExtra("post_id"));

            databaseReference.child("text").setValue(text);
            databaseReference.child("post_imag").setValue(image_link)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Snackbar.make(relativeLayout,"✔ Success! ", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "✔ Success! ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));




                        }
                    });



        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Write Your Message and select a category", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick({R.id.photo, R.id.remove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.photo:
            {
                image_link="";
                ImagePicker.Companion.with(EditActivity.this)
                        //Crop image(Optional), Check Customization for more option
                        .galleryOnly()
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)
                        //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
                break;
            case R.id.remove:
                photo.setImageDrawable(getResources().getDrawable(R.drawable.add));
                remove.setVisibility(View.GONE);
                image_link="";
                break;
        }
    }

    public void onActivityResult(int i, int j, Intent data) {
        this.data = data;

        if(j== Activity.RESULT_OK){
            Log.i("123321", "89:ok");
            image_link="";
            uploadToServer("http://nul.com");

            uploadToServer(ImagePicker.Companion.getFilePath(data));

            try {
                Uri selectedImg = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImg);
                imageProgress.setVisibility(View.VISIBLE);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("123321", "102:"+e.getMessage());
            }
        }
        super.onActivityResult(i, j, data);


    }

    private void uploadToServer(String url) {
        Log.i("123321", "Uploading.................................");
        MediaManager.get().upload(url)
                .unsigned("sample_preset")
                .option("resource_type", "auto")
                .option("folder","image")
                .option("public_id", "IMG_"+System.currentTimeMillis())
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.i("123321", "start:"+requestId);

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                        Log.i("123321", "total:"+totalBytes+" byte:"+bytes);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        Log.i("123321", "41:"+resultData);
                        imageProgress.setVisibility(View.GONE);
                        remove.setVisibility(View.VISIBLE);
                        image_link= Objects.requireNonNull(resultData.get("url")).toString();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Log.i("123321", "42:"+error.getDescription());
                        if(!error.getDescription().equals("File 'http://nul.com' does not exist"))
                            Snackbar.make(relativeLayout,"Something want's wrong!", Snackbar.LENGTH_INDEFINITE).setAction("Retry", v ->{uploadToServer("http://null.com");uploadToServer(ImagePicker.Companion.getFilePath(data));}).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.i("123321", "error:::"+error.getDescription());

                    }
                })
                .dispatch();
    }
}
