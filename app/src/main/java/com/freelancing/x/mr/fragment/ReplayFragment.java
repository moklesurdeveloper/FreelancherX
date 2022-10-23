package com.freelancing.x.mr.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.freelancing.x.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReplayFragment extends Fragment {


    @BindView(R.id.replay_recycler)
    RecyclerView replayRecycler;
    @BindView(R.id.post_name)
    TextView postName;
    @BindView(R.id.post_text)
    TextView postText;
    @BindView(R.id.post_icon)
    CircleImageView postIcon;
    @BindView(R.id.like_icon)
    ImageView likeIcon;
    @BindView(R.id.comment_icon)
    ImageView commentIcon;
    @BindView(R.id.post_comment)
    TextView postComment;
    @BindView(R.id.post_like)
    TextView postLike;

    public ReplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_replay, container, false);
        ButterKnife.bind(this, view);
        Intent intent = getActivity().getIntent();
        if (intent.getStringExtra("name") != null) {
            String name = intent.getStringExtra("name");
            Toast.makeText(getContext(), "" + name, Toast.LENGTH_SHORT).show();
        }
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        replayRecycler.setLayoutManager(manager);
        return view;
    }


}
