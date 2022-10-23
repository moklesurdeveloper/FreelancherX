package com.freelancing.x;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModeCategoryActivity extends AppCompatActivity {
String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_category);
        path=getIntent().getStringExtra("name");
        ButterKnife.bind(this);
    }

    @OnClick({R.id.live, R.id.skill})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.live:
                Intent intent=new Intent(getApplicationContext(),VideoCategoryActivity.class);
                intent.putExtra("name",path+"live");
                startActivity(intent);
                break;
            case R.id.skill:
                Intent intent2=new Intent(getApplicationContext(),PostCategoryActivity.class);
                intent2.putExtra("name","/"+path+"/skill");
                startActivity(intent2);
                break;
        }
    }
}
