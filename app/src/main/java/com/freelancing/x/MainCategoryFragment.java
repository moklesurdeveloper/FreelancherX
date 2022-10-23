package com.freelancing.x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

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

import butterknife.ButterKnife;


public class MainCategoryFragment extends Fragment {
RecyclerView recycler;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main_category, container, false);
        ButterKnife.bind(this, view);
        recycler=view.findViewById(R.id.recycler);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();
        setupDatabase();
        return view;

    }


    private void setupDatabase() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        Query query= FirebaseDatabase.getInstance().getReference("admin/main_list");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<ListModel>().setQuery(query, new SnapshotParser<ListModel>() {
            @NonNull
            @Override
            public ListModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new ListModel(snapshot.getKey(), snapshot.child("name").getValue().toString(), snapshot.child("image").getValue().toString());
            }
        }).build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<ListModel,ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list3,parent,false));
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
            name=itemView.findViewById(R.id.textView);
            layout=itemView.findViewById(R.id.main);
            imageView=itemView.findViewById(R.id.imageView);
        }

        public void details(ListModel listModel) {
            //            Glide.with(getApplicationContext())
//                    .load(listModel.getImage())
//                    .placeholder(R.drawable.img_default)
//                    .transition(withCrossFade())
//                    .into(imageView );
            Log.i("123321", "image: "+listModel.getImage());
            Picasso.get().load(listModel.getImage()).placeholder(R.drawable.logo).into(imageView);

            layout.setOnClickListener(v -> {
                layout.setBackgroundColor(getResources().getColor(R.color.select));
                Intent intent=new Intent(getActivity(),VideoCategoryActivity.class);
                intent.putExtra("name","admin/main_list/"+listModel.getPath()+"/"+"live");
                startActivity(intent);
            });

        }
    }

}
