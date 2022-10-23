package com.freelancing.x;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity_x extends AppCompatActivity {

    @BindView(R.id.notification)
    TextView notification;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainx);


        ButterKnife.bind(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        start.setOnClickListener(v -> startActivity(new Intent(this, MainCategoryFragment.class))
        );

        DatabaseReference databaseReference0=FirebaseDatabase.getInstance().getReference("admin/isUpdating");
        databaseReference0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_x.this);
                    builder.setTitle("Maintenance Break");
                    builder.setMessage("We'll be back soon...");
                    builder.setCancelable(false);
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/notification");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView2.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        navView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.contact:
                    Builder builder1 = new Builder(this);
                    builder1.setTitle("Contact Us");
                    builder1.setMessage("If you have any suggestion or complain, feel free to contact with us via \ndeveloper.mrenglish@gmail.com");
                    builder1.setPositiveButton("ok", null);
                    builder1.show();
                    drawer.closeDrawers();
break;
                case R.id.nav_share:
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage= "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch(Exception e) {
                        Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    drawer.closeDrawers();
                    break;
                case R.id.nav_rateus:

                    final String appPackageName = getPackageName();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                //    MDToast.makeText(getApplicationContext(), "Please wait......", MDToast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    break;
                    case R.id.nav_moreapps:

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fsit.englishpractice")));
                //    MDToast.makeText(getApplicationContext(), "Please wait......", MDToast.LENGTH_SHORT).show();
                    drawer.closeDrawers();
                    break;
                    case  R.id.policy:
                    String url ="https://www.websitepolicies.com/policies/view/F0Ql2oBP";
                    Intent i = new Intent(ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    drawer.closeDrawers();
                    break;
                case R.id.nav_abaout:
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
                    builder.setPositiveButton("ok",null );
                    builder.show();
                    drawer.closeDrawers();
                    break;
            }
       return false; });
    }

    @Override
    public void onBackPressed() {
        {
            Builder builder = new Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you Sure ?");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                    System.exit(0);

                }
            });
            builder.setNegativeButton("cancel",null );
            builder.show();
        }
    }
}
