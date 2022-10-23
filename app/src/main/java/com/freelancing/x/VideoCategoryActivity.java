package com.freelancing.x;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class VideoCategoryActivity extends AppCompatActivity {
RecyclerView recycler;
    ProgressDialog progressDialog;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category3);
        recycler=findViewById(R.id.recycler);
        path=getIntent().getStringExtra("name");
     progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        setupDatabase();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



    private void setupDatabase() {
        LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(manager);
        Query query= FirebaseDatabase.getInstance().getReference(path);
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<ListModel>().setQuery(query, snapshot -> new ListModel(snapshot.getKey(),snapshot.child("name").getValue().toString(),snapshot.child("image").getValue().toString())).build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<ListModel,ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false));
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull ListModel listModel) {
                viewHolder.details(listModel);
            }
        };
        recycler.setAdapter(adapter);
        adapter.startListening();

    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
       LinearLayout layout;
       ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.text);
            layout=itemView.findViewById(R.id.main);
            imageView=itemView.findViewById(R.id.imageView);
        }

        public void details(ListModel listModel) {
            Glide.with(getApplicationContext())
             .load(listModel.getImage())
             .placeholder(R.drawable.logo)
             .transition(withCrossFade())
             .into(imageView );
            Log.i("123321", "image: "+listModel.getImage());

name.setText(listModel.getName());
            layout.setOnClickListener(v -> {
                layout.setBackgroundColor(getResources().getColor(R.color.select2));
                Intent intent=new Intent(getApplicationContext(), VideoActivityx.class);
                intent.putExtra("name",path+"/"+listModel.getPath()+"/video");
                startActivity(intent);
            });

        }
    }

}
