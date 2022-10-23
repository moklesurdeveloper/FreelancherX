package com.freelancing.x.mr.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freelancing.x.R;
import com.freelancing.x.mr.ReplayActivity;
import com.freelancing.x.mr.model.NotificationModel;
import com.freelancing.x.mr.utils.Utils;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.android.exoplayer2.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    String u_name;
    String u_id;
           // "xcoPG6KMytNRulGVJAnYB0inxas2"
            ;
    RecyclerView recyclerView;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.recycler);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
          u_id=  FirebaseAuth.getInstance().getCurrentUser().getUid();
            u_name= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        setupRecycler();
        return view;
    }
    public  void  setupRecycler(){
       Query mainReference= FirebaseDatabase.getInstance().getReference("user").child(u_id).child("notification");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<NotificationModel>().setQuery(mainReference, snapshot -> {

            return new NotificationModel(

                   snapshot.child("kind").exists()?snapshot.child("kind").getValue().toString():"no kind",

                    snapshot.child("link").exists()?snapshot.child("link").getValue().toString():"no link",
                    snapshot.child("message").exists()?snapshot.child("message").getValue().toString():"no message",
                    snapshot.child("status").exists() ? snapshot.child("status").getValue().toString() : "",
                    snapshot.child("time").exists()?snapshot.child("time").getValue(Long.class):Long.parseLong("0"),
                    snapshot.child("type").exists()?snapshot.child("type").getValue().toString():"no type",snapshot.getKey()
            );
        }).build();

        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<NotificationModel,ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull NotificationModel notificationModel) {
                viewHolder.setUp(notificationModel);

            }
        };
recyclerView.setAdapter(adapter);
adapter.startListening();

    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView message,time;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message);
            time=itemView.findViewById(R.id.time);
            icon=itemView.findViewById(R.id.icon);
            layout=itemView.findViewById(R.id.main_layout);
        }

        public void setUp(NotificationModel notificationModel) {
            //icon set
            switch (notificationModel.getType()){
                case "like":
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                    break;
                case "comment":
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_comment_icon));
                    break;
                default:
                    icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_reply_black_24dp));
                    break;
            }Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();

            String text = TimeAgo.using(notificationModel.getTime(), messages);
            time.setText(text);
            layout.setBackgroundColor(notificationModel.getStatus().equals("")? Color.parseColor("#ffffff"):Color.parseColor("#ECEFF5"));
            message.setText(Html.fromHtml(notificationModel.getMessage()));
            layout.setOnClickListener(v -> {
                Log.i("123321", "click:"+notificationModel.getKind());

            if(notificationModel.getKind().equals("post")){
                notifyPost(notificationModel);
            }
            if(notificationModel.getType().equals("replay")){
                startActivity(new Intent(getActivity(), ReplayActivity.class).putExtra("key",notificationModel.getLink()));

                
            }
            });
        }

        private void notifyPost(NotificationModel notificationModel) {
           DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("post").child(notificationModel.getLink());
           databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                   if(dataSnapshot.exists()){
                       Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                       intent.putExtra("text", dataSnapshot.child("text").getValue().toString());
                       intent.putExtra("name", dataSnapshot.child("name").getValue().toString());
                       intent.putExtra("image", dataSnapshot.child("image").getValue().toString());
                       intent.putExtra(Utils.post_image, dataSnapshot.child("post_image").getValue().toString());
                       intent.putExtra("post_id", dataSnapshot.child("post_id").getValue().toString());
                       intent.putExtra("total_comment", "");
                       intent.putExtra("total_like", "");
                       intent.putExtra("time", reformat(dataSnapshot.child("time").getValue(Long.class)));
                       intent.putExtra("user_id",dataSnapshot.child("user_id").getValue().toString());
                       DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notification").child(notificationModel.getKey()).child("status");
                       databaseReference1.setValue(System.currentTimeMillis());
                       getActivity().startActivity(intent);}
                   else {
                       Log.i("123321", "167:"+notificationModel.getLink());
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
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
