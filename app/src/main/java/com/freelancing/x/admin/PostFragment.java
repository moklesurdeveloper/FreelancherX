package com.freelancing.x.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.freelancing.x.R;
import com.freelancing.x.mr.Notify;
import com.freelancing.x.mr.model.PostModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {
    RecyclerView recycler;
    ProgressDialog progressDialog;
    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        recycler=view.findViewById(R.id.recyclerview);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.show();

        setupDatabase();
    return  view;}

    private void setupDatabase() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        Query query= FirebaseDatabase.getInstance().getReference("approve");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<PostModel>().setQuery(query, new SnapshotParser<PostModel>() {
            @NonNull
            @Override
            public PostModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new PostModel(
                        getData(snapshot.child("name").getValue().toString()),
                        getData(snapshot.child("image").getValue().toString()),
                        getData(snapshot.child("post_id").getValue().toString()),
                        getData(snapshot.child("text").getValue().toString()),
                        getData(snapshot.child("user_id").getValue().toString()),
                        getData(snapshot.child("category").getValue().toString()),
                        getData(snapshot.child("post_image").exists() ? snapshot.child("post_image").getValue().toString() : ""),
                        getData2(snapshot.child("time").getValue(Long.class)));
            }
        }).build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<PostModel, ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post22,parent,false));
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressDialog.dismiss();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull PostModel postModel) {
                viewHolder.setDetails(postModel.getName(), postModel.getImage(), postModel.getPost_id(), postModel.getText(), postModel.getTime(), postModel.getUser_id(), postModel.getCategory(),postModel);


            }


        };
        recycler.setAdapter(adapter);
        adapter.startListening();

    }

    private long getData2(Long time) {
        if (time == null)
            return 0;
        else return time;
    }

    private String getData(String name) {
        if (name == null || name.isEmpty() || name.equals(""))
            return "";
        else
            return name;
    }



    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt, time_txt, text_txt, comment_text, category_text;
        ImageView photo,post_image;
        RelativeLayout image_holder;
        Context context;
        String total_comment;
        Button approve,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            category_text = itemView.findViewById(R.id.category);

            name_txt = itemView.findViewById(R.id.post_name);
            text_txt = itemView.findViewById(R.id.post_text);
            time_txt = itemView.findViewById(R.id.post_time);
            photo = itemView.findViewById(R.id.post_icon);
            post_image=itemView.findViewById(R.id.post_image);
            image_holder=itemView.findViewById(R.id.image_holder);
            approve=itemView.findViewById(R.id.approve);
            delete=itemView.findViewById(R.id.delete);

            context=itemView.getContext();
        }


        public void setDetails(String name, String image, final String post_id, String text, long time, String user_id, String category, PostModel model) {
            post_image.setImageDrawable(itemView.getContext().getDrawable(R.drawable.empty));
            image_holder.setVisibility(View.GONE);
            Log.i("123321", "text:" + text + "  image:" + model.getPost_image());
            name_txt.setText(name);
            if (!model.getPost_image().equals("")) {
                image_holder.setVisibility(View.VISIBLE);
                Log.i("123321", "652:text:" + text + "  image:" + model.getPost_image());

                Picasso.get().load(model.getPost_image()).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(post_image);

            }
            time_txt.setText(reformat(time));
            category_text.setText(category);

            try {
                text_txt.setText(Html.fromHtml(text));
            } catch (Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("123321", dataSnapshot+"");

                            Notify2.notify(dataSnapshot.child("post_id").getValue().toString(),"post","post",dataSnapshot.child("user_id").getValue().toString(),"Your post Successfully approved by an admin");
                            DatabaseReference databaseReference0=FirebaseDatabase.getInstance().getReference("post");
                            databaseReference0.child(dataSnapshot.child("post_id").getValue().toString()).setValue(dataSnapshot.getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                                    databaseReference.removeValue();
                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("123321", dataSnapshot+"");
                            Notify2.notify(dataSnapshot.child("post_id").getValue().toString(),"post","post",dataSnapshot.child("user_id").getValue().toString(),"Sorry! Your post not follow our rules");

                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("approve").child(post_id);
                            databaseReference.removeValue();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });






            Picasso.get().load(image).placeholder(R.drawable.ic_user).into(photo);
            time_txt.setText(reformat(time));



        }
    }
    private String reformat(long time) {
        long system = System.currentTimeMillis();
        long difference = system - time;
        if (difference < 1000 * 60 * 60) {
            return difference / (1000 * 60) + " min ago";
        } else if (difference < 1000 * 60 * 60 * 24) {
            return difference / (1000 * 60 * 60) + " hour ago";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM hh:mm a");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return format.format(calendar.getTime());
        }
    }
}
