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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostCategoryActivity extends AppCompatActivity {
    RecyclerView recycler;
    ProgressDialog progressDialog;
    String path;
    @BindView(R.id.textView2)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category2);
        ButterKnife.bind(this);
        recycler = findViewById(R.id.recycler);
        path = getIntent().getStringExtra("name");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        setupDatabase();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/notification2");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              textView2.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setupDatabase() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(manager);
        Query query = FirebaseDatabase.getInstance().getReference(path);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ListModel>().setQuery(query, snapshot -> new ListModel(snapshot.getKey(), snapshot.child("name").getValue().toString(), snapshot.child("image").getValue().toString())).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ListModel, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false));
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout layout;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView);
            layout = itemView.findViewById(R.id.main);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void details(ListModel listModel) {
//            Glide.with(getApplicationContext())
//                    .load(listModel.getImage())
//                    .placeholder(R.drawable.img_default)
//                    .transition(withCrossFade())
//                    .into(imageView );
            Log.i("123321", "image: " + listModel.getImage());
            Picasso.get().load(listModel.getImage()).placeholder(R.drawable.logo).into(imageView);
            name.setText(listModel.getName());
            layout.setOnClickListener(v -> {
                layout.setBackgroundColor(getResources().getColor(R.color.select));
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                intent.putExtra("title", listModel.getName());
                intent.putExtra("name", path + "/" + listModel.getPath() );
                startActivity(intent);
            });

        }
    }

}
