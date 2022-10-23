package com.freelancing.x.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.freelancing.x.R;
import com.freelancing.x.mr.MainActivity;
import com.freelancing.x.mr.SignInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity {
    final Fragment post=new PostFragment();
    final Fragment user=new UserFragment();
    final Fragment admin=new AdminFragment();
    final Fragment mod=new ModFragment();
    Fragment active = post;
    FragmentManager fm = getSupportFragmentManager();
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        navSetup();
    }

    private void navSetup() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //fm.beginTransaction().add(R.id.main_container, grammer, "grammer").hide(grammer).commit();
        //   fm.beginTransaction().add(R.id.main_container, profile, "profile").hide(profile).commit();
        fm.beginTransaction().add(R.id.main_container, post, "post").commit();

        fm.beginTransaction().add(R.id.main_container, user, "user").hide(user).commit();
        fm.beginTransaction().add(R.id.main_container, admin, "admin").hide(admin).commit();
        fm.beginTransaction().add(R.id.main_container, mod, "mod").hide(mod).commit();
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {



        switch (item.getItemId()) {
            case R.id.post:

                fm.beginTransaction().hide(active).show(post).commit();
                active = post;
                return true;
                case R.id.admin:

                fm.beginTransaction().hide(active).show(admin).commit();
                active = admin;
                return true;
             case R.id.mod:

                fm.beginTransaction().hide(active).show(mod).commit();
                active = mod;
                return true;



            case R.id.user:


                    fm.beginTransaction().hide(active).show(user).commit();
                    active = user;


                return true;

    }


        return false;
    };
}
