package com.freelancing.x.mr.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freelancing.x.R;
import com.freelancing.x.mr.model.LeaderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderFragment extends AppCompatActivity {



    TextView textView7;
    @BindView(R.id.word)
    RecyclerView word;
    @BindView(R.id.spell)
    RecyclerView spell;
    @BindView(R.id.quick)
    RecyclerView quick;

    int i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_leader);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setReverseLayout(true);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setReverseLayout(true);
        LinearLayoutManager manager3 = new LinearLayoutManager(this);
        manager3.setReverseLayout(true);
        ButterKnife.bind(this);
        word.setLayoutManager(manager1);
        spell.setLayoutManager(manager2);
        quick.setLayoutManager(manager3);
       // populateRecycler("quiz_point",leader);
        i=0;
        populateRecycler("word_point",word);
        i=0;
        populateRecycler("spell_point",spell);
        i=0;
        populateRecycler("quick_point",quick);


    }

    private void populateRecycler(String key, RecyclerView leader) {
       i=0;
        Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild(key).limitToFirst(5);
        FirebaseRecyclerOptions options = null;
        try {
            options = new FirebaseRecyclerOptions.Builder<LeaderModel>().setQuery(
                    query, (DataSnapshot snapshot) -> {
                        if (snapshot.child(key).exists()&&snapshot.child("profile_image").exists()&&snapshot.child("name").exists()) {
                            return new LeaderModel(snapshot.child("name").getValue().toString(), snapshot.child("profile_image").getValue().toString(), snapshot.child(key).getValue(Integer.class));

                        } else if(snapshot.child("profile_image").exists()&&snapshot.child("name").exists()) {
                            return new LeaderModel(snapshot.child("name").getValue().toString(), snapshot.child("profile_image").getValue().toString(), 0);
                        }
                        else
                            return new LeaderModel("No Name", "http://com.com", 0);


                    }


            ).build();
        } catch (Exception e) {
            options = new FirebaseRecyclerOptions.Builder<LeaderModel>().setQuery(
                    query, snapshot -> new LeaderModel(snapshot.child("name").getValue().toString(), snapshot.child("profile_image").getValue().toString(), 0)


            ).build();

        }
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<LeaderModel, ViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull LeaderModel leaderModel) {
                viewHolder.setDetails(leaderModel.getName(), leaderModel.getProfile_image(), leaderModel.getQuiz_point());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_recycler, parent, false));
            }


        };
        leader.setAdapter(adapter);
        adapter.startListening();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, balance, serial;
        CircleImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            balance = itemView.findViewById(R.id.balance);
            icon = itemView.findViewById(R.id.icon);
            serial = itemView.findViewById(R.id.serial);
        }

        public void setDetails(String sname, String profile_image, int quiz_point) {
            i++;
            serial.setText(".");
            name.setText(sname);
            balance.setText(quiz_point + "");
            Picasso.get().load(profile_image).placeholder(R.drawable.ic_user).into(icon);
        }
    }
}
