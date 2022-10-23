package com.freelancing.x.mr;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.freelancing.x.BuildConfig;
import com.freelancing.x.MainCategoryFragment;
import com.freelancing.x.R;
import com.freelancing.x.admin.AdminActivity;
import com.freelancing.x.mr.fragment.HomeFragment;
import com.freelancing.x.mr.fragment.LiveFragment;
import com.freelancing.x.mr.fragment.NotificationFragment;
import com.freelancing.x.mr.fragment.ProfileFragment;
import com.freelancing.x.mr.model.Model;
import com.freelancing.x.mr.utils.Utils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends AppCompatActivity {
    String currentFragment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer)
    DrawerLayout drawer;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    final Fragment home = new HomeFragment();
    final Fragment live = new LiveFragment();
    final Fragment test = new MainCategoryFragment();
    final Fragment notification = new NotificationFragment();
    final Fragment profile = new ProfileFragment();
    FragmentManager fm = getSupportFragmentManager();
    Fragment active = home;
    FirebaseUser user;
    TextView headerName, headerEmail;
    @BindView(R.id.main_container)
    FrameLayout mainContainer;
    @BindView(R.id.drawer_nav)
    NavigationView drawerNav;
    ImageView imageView;
    boolean comment = true;
    List list = new ArrayList();
    @BindView(R.id.admin)
    Button admin;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String version;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        ButterKnife.bind(this);
        if (!Utils.role.equals("member")) {
            admin.setVisibility(View.VISIBLE);
            admin.setText(Utils.role);
        }
        else {
            admin.setVisibility(View.GONE);
        }
        user=FirebaseAuth.getInstance().getCurrentUser();
    if(user!=null) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
    databaseReference1.child("email").setValue(user.getEmail());
    //databaseReference1.child("role").setValue("admin");
    }
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        DatabaseReference databaseReference0 = FirebaseDatabase.getInstance().getReference("admin").child("app_version");

        databaseReference0.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue().equals(version)) {
                    Builder builder = new Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Update required");
                    builder.setMessage("A new Version is available on Play store.\nPlease update ");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String appPackageName = getPackageName();

// getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                finishAffinity();
                                System.exit(0);
                            } catch (ActivityNotFoundException anfe) {
                                startActivity(new Intent(ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                finishAffinity();
                                System.exit(0);
                            }
                        }
                    });

                    builder.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if (user == null) {
            Menu nav_Menu = drawerNav.getMenu();
            nav_Menu.findItem(R.id.logout).setVisible(false);
        } else {
            Menu nav_Menu = drawerNav.getMenu();
            nav_Menu.findItem(R.id.signin).setVisible(false);
        }
        drawerSetup();
        bottomSetup();
        navHeaderSetup();
        profileSetup();
        ImageView imageView = toolbar.findViewById(R.id.drawerIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        fm.addOnBackStackChangedListener(() -> {
            List<Fragment> f = fm.getFragments();
            Fragment frag = f.get(0);
            currentFragment = frag.getClass().getSimpleName();
        });

    }

    private void profileSetup() {


    }


    private void navHeaderSetup() {
        headerName = drawerNav.getHeaderView(0).findViewById(R.id.name);
        headerEmail = drawerNav.getHeaderView(0).findViewById(R.id.email);
        imageView = drawerNav.getHeaderView(0).findViewById(R.id.imageView);
        if (user != null) {
            headerName.setText(user.getDisplayName());
            headerEmail.setText(user.getEmail());
            Picasso.get().load(user.getPhotoUrl()).into(imageView);

        }
    }

    private void drawerSetup() {
        //    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        //    drawer.addDrawerListener(toggle);

        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setHomeButtonEnabled(true);
        //    toggle.syncState();


        drawerNav.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {

                case R.id.logout:

                    drawer.closeDrawers();
                    Builder builder2 = new Builder(this);
                    builder2.setTitle("Are You Sure?");
                    builder2.setMessage("Are You Want To Sign Out?");
                    builder2.setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    });
                    builder2.setNegativeButton("cancel", null);
                    builder2.show();
                    break;
                case R.id.notice:
                    startActivity(new Intent(getApplicationContext(), NoticeActivity.class));
                    drawer.closeDrawers();
                    break;

                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileFragment.class));
                    drawer.closeDrawers();
                    break;
                case R.id.policy:
                    String url = "https://www.websitepolicies.com/policies/view/ZEQdS8FI";
                    Intent i = new Intent(ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    drawer.closeDrawers();
                    break;
                case R.id.signin:
                    startActivity(new Intent(this, SignInActivity.class));
                    break;
                case R.id.about:
                    Builder builder = new Builder(this);
                    builder.setTitle("About Us");
                    builder.setMessage("In the last decade, we, the team of some furious people, dreamed to change the traditional thinking as well as making a new gateway to earning money. \n" +
                            "\n" +
                            "But we all know our ecosystem. However, we were curious enough and never ever think about our past. \n" +
                            "\n" +
                            "After that painful journey, we found some sweets on freelancing. By the game of time, we now an established team generating 6 figures every month. \n" +
                            "\n" +
                            "From that view, We designed FreelancingX to provide all kinds of premium freelancing course totally free. \n" +
                            "\n" +
                            "Our goal is to remove unemployment and help people leading a happy life. We hope you will be a major part of our glorious success! Happy Journey on Freelancing! \n" +
                            "\n" +
                            "Thanks- \n" +
                            "FreelancingX Team");
                    builder.setPositiveButton("ok", null);
                    builder.show();
                    drawer.closeDrawers();
                    break;
                case R.id.share:
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                    drawer.closeDrawers();
                    break;
                case R.id.contact:
                    Builder builder1 = new Builder(this);
                    builder1.setTitle("Contact Us");
                    builder1.setMessage("If you have any suggestion or complain, feel free to contact with us via \ndeveloper.mrenglish@gmail.com");
                    builder1.setPositiveButton("ok", null);
                    builder1.show();
                    drawer.closeDrawers();

            }
            return false;
        });

    }

    private void bottomSetup() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //fm.beginTransaction().add(R.id.main_container, grammer, "grammer").hide(grammer).commit();
        //   fm.beginTransaction().add(R.id.main_container, profile, "profile").hide(profile).commit();
        fm.beginTransaction().add(R.id.main_container, home, "home").commit();

        fm.beginTransaction().add(R.id.main_container, test, "test").hide(test).commit();
        fm.beginTransaction().add(R.id.main_container, profile, "profile").hide(profile).commit();
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.homescreen_count, bottomNavigationMenuView, false);
        TextView tv = badge.findViewById(R.id.notification_badge);
        tv.setVisibility(View.GONE);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).child("count");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        tv.setVisibility(View.VISIBLE);
                        String s = dataSnapshot.getChildrenCount() + "";
                        tv.setText(s);
                        try {
                            itemView.addView(badge);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        tv.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        if (getIntent().getStringExtra("name") != null) {
            Log.i("123321", "296");
            DatabaseReference databaseReference0 = FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).child("count");
            databaseReference0.removeValue();
            if (list.contains(notification)) {
                comment = true;
                fm.beginTransaction().hide(active).show(notification).commit();
                active = notification;

            } else {
                list.add(notification);
                fm.beginTransaction().add(R.id.main_container, notification, "profile").hide(notification).commit();
                comment = true;
                fm.beginTransaction().hide(active).show(notification).commit();
                active = notification;

            }
        } else {
            Log.i("123321", "315");
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {


        switch (item.getItemId()) {
            case R.id.feed:

                fm.beginTransaction().hide(active).show(home).commit();
                active = home;
                return true;

            case R.id.live:
                if (user != null) {
                    if (!list.contains(live)) {
                        list.add(live);
                        fm.beginTransaction().add(R.id.main_container, live, "live").hide(live).commit();
                    }
                    {
                        fm.beginTransaction().hide(active).show(live).commit();
                        active = live;
                        comment = true;
                    }
                } else {
                    Builder builder = new Builder(MainActivity.this);
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }
                return true;

            case R.id.test:
                if (user != null) {


                    fm.beginTransaction().hide(active).show(test).commit();
                    active = test;
                } else {
                    Builder builder = new Builder(MainActivity.this);
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }
                return true;

            case R.id.profile:
                if (user != null) {
                    fm.beginTransaction().hide(active).show(profile).commit();
                    active = profile;
                    comment = true;
                } else {
                    Builder builder = new Builder(this);
                    builder.setTitle("Sign in requied");
                    builder.setMessage("you must sign in to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));

                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                }
                return true;

            case R.id.notification:
                if (user != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).child("count");
                    databaseReference.removeValue();
                    if (list.contains(notification)) {
                        comment = true;
                        fm.beginTransaction().hide(active).show(notification).commit();
                        active = notification;
                        return true;
                    } else {
                        list.add(notification);
                        fm.beginTransaction().add(R.id.main_container, notification, "profile").hide(notification).commit();
                        comment = true;
                        fm.beginTransaction().hide(active).show(notification).commit();
                        active = notification;
                        return true;
                    }
                } else {
                    Builder builder = new Builder(this);
                    builder.setTitle("Sign in requied");
                    builder.setMessage("you must sign in to use the feature");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));

                        }
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                }
        }

        return false;
    };


    public void setActive(Fragment fragment) {
        active = fragment;
    }

    public Fragment getActive() {
        return active;
    }

    @Override
    public void onBackPressed() {


        if (active == home) {
            Builder builder = new Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you Sure ?" +
                    "");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                    System.exit(0);

                }
            });
            builder.setNegativeButton("cancel", null);
            builder.show();
        }

        // Toast.makeText(this, ""+Model.getI(), Toast.LENGTH_SHORT).show();
        if (Model.getI() == 1) {
            // super.onBackPressed();
        } else {
            navigation.setVisibility(View.VISIBLE);
        }

    }

    public void setComment(Boolean b) {
        // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        comment = b;
    }

    @OnClick(R.id.admin)
    public void onViewClicked() {
        startActivity(new Intent(this, AdminActivity.class));
        
    }
}