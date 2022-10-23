package com.freelancing.x.mr.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.freelancing.x.FullscreenActivity;
import com.freelancing.x.MainCategoryFragment;
import com.freelancing.x.ModeCategoryActivity;
import com.freelancing.x.NewActivity;
import com.freelancing.x.R;
import com.freelancing.x.mr.model.Model;
import com.freelancing.x.mr.model.newModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {
    private InterstitialAd mInterstitialAd;
RecyclerView recycler;
ProgressDialog progressDialog;
String text,question;

    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this, view);
        mInterstitialAd = new InterstitialAd(getActivity());
      mInterstitialAd.setAdUnitId("ca-app-pub-2574328611948823/6263965069");
        //   mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Model.setI(0);

        //  timerProgramCountdown.startCountDown(99999999);



        recycler=view.findViewById(R.id.recycler);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();
        setupDatabase();
       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("admin/new_list");
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                //  Intent intent=new Intent(getApplicationContext(), Main3Activity_x.class);
                Intent intent=new Intent(getActivity(), NewActivity.class);
                intent.putExtra("text",text);
                intent.putExtra("question",question);
                startActivity(intent);
            }
        });
        return view;

    }


    private void setupDatabase() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        Query query= FirebaseDatabase.getInstance().getReference("admin/new_list");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<newModel>().setQuery(query, new SnapshotParser<newModel>() {
            @NonNull
            @Override
            public newModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.i("123321", ""+snapshot);
                return new newModel( snapshot.child("title").getValue().toString(), snapshot.child("text").getValue().toString(),snapshot.child("question").getValue().toString());
            }
        }).build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<newModel, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false));
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull newModel newModel) {
                viewHolder.details(newModel);
            }
        };
        recycler.setAdapter(adapter);
        adapter.startListening();

    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RelativeLayout layout;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           title=itemView.findViewById(R.id.text);
            layout=itemView.findViewById(R.id.main);
        }

        public void details(newModel newModel) {
            //            Glide.with(getApplicationContext())
//                    .load(newModel.getImage())
//                    .placeholder(R.drawable.img_default)
//                    .transition(withCrossFade())
//                    .into(imageView );
            text=newModel.getText();
            question=newModel.getQuestion();
            title.setText(newModel.getText());

            layout.setOnClickListener(v -> {
                layout.setBackgroundColor(getResources().getColor(R.color.select));
                if(mInterstitialAd.isLoaded())
                    mInterstitialAd.show();

         else {
                    Intent intent = new Intent(getActivity(), NewActivity.class);
                    intent.putExtra("text", newModel.getText());
                    intent.putExtra("question", newModel.getQuestion());
                    startActivity(intent);
                }
            });

        }
    }

}
