package com.freelancing.x.mr.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.freelancing.x.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContestFragment extends Fragment {

int i;
    public ContestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contest, container, false);
        ButterKnife.bind(this, view);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("comment_point");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    i = dataSnapshot.getValue(Integer.class);
                } else i = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        return view;


    }

    @OnClick({R.id.reading, R.id.listen,R.id.leader,R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leader:



           getActivity().startActivity(new Intent(getActivity(),LeaderFragment.class));
                break;
            case R.id.reading:
                getActivity().startActivity(new Intent(getActivity(),QuizContestFragment.class));
                break;
            case R.id.listen:
                getActivity().startActivity(new Intent(getActivity(),SpellingContestFragment.class));
                break;
            case R.id.share:
           getActivity().startActivity(new Intent(getActivity(),QuickQuizFragment.class));
        }
    }
}
