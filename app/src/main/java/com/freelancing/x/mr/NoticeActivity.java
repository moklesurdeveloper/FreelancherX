package com.freelancing.x.mr;

import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freelancing.x.R;
import com.freelancing.x.mr.model.Notice;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.notification)
    RecyclerView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        notification.setLayoutManager(manager);
populateRecycler();
    }

    private void populateRecycler() {
        Query query= FirebaseDatabase.getInstance().getReference("admin/notice");
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Notice>().setQuery(query, snapshot ->
                new Notice(snapshot.child("title").getValue().toString(),snapshot.child("time").getValue().toString(),
                        snapshot.child("text").getValue().toString())).build();
        FirebaseRecyclerAdapter adapter =new FirebaseRecyclerAdapter<Notice,ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder(LayoutInflater.from(
                        parent.getContext()
                ).inflate(R.layout.notice,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Notice notice) {

                viewHolder.setDetails(notice.getTitle(),notice.getTime(),notice.getText());
            }

        };
        notification.setAdapter(adapter);
        adapter.startListening();

    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,time,text;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            title=itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.date);
            text=itemView.findViewById(R.id.details);
            layout=itemView.findViewById(R.id.layout);

        }

        public void setDetails(String stitle, String stime, String stext) {
            title.setText(stitle);
            time.setText(stime);
            text.setText(stext);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Builder builder = new Builder(NoticeActivity.this);
                    builder.setTitle(stitle);
                    builder.setMessage(stext);
                    builder.setPositiveButton("ok",null );
                    builder.show();
                }
            });
        }
    }
}
