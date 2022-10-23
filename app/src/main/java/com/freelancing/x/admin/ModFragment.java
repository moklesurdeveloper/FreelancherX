package com.freelancing.x.admin;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freelancing.x.R;
import com.freelancing.x.mr.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModFragment extends Fragment {
RecyclerView recyclerView;
SearchView searchView;
String text="";
    public ModFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView=view.findViewById(R.id.recycler);
        searchView=view.findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text=newText;
                setUpRecycler(   FirebaseDatabase.getInstance().getReference("user").orderByChild("role").startAt("moderator")
);

                return false;
            }
        });
        setUpRecycler(   FirebaseDatabase.getInstance().getReference("user").orderByChild("role").equalTo("moderator"));

 return view;   }

    private void setUpRecycler(Query query) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<UserModel>().setQuery(query, snapshot -> {
            String name = snapshot.child("name").exists()?snapshot.child("name").getValue().toString():"no name";
            String profile_image = snapshot.child("profile_image").exists()?snapshot.child("profile_image").getValue().toString():"jjjj";
            String role = snapshot.child("role").exists() ? snapshot.child("role").getValue().toString() : "member";
            String u_id=snapshot.getKey();

            return new UserModel(name, profile_image, role,u_id);

        }).build();
        FirebaseRecyclerAdapter adapter=new FirebaseRecyclerAdapter<UserModel,ViewHolder>(options) {

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull UserModel userModel) {
                viewHolder.setDetails(userModel);

            }




        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class  ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView name, role;
        ImageView more;
        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;
        PowerMenu powerMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            profile = itemView.findViewById(R.id.image);
            role = itemView.findViewById(R.id.role);
            more = itemView.findViewById(R.id.more);
        }

        public void setDetails(final UserModel userModel) {
            if(Utils.role.equals("admin")){
                more.setVisibility(View.VISIBLE);
            }
            else {
                more.setVisibility(View.GONE);
            }

            name.setText(userModel.getName());
            Picasso.get().load(userModel.getPhoto()).placeholder(R.drawable.ic_user).into(profile);
            role.setText(userModel.getRole());
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    powerMenu = new PowerMenu.Builder(v.getContext())
                            // .addItemList(list) // list has "Novel", "Poerty", "Art"
                            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                            .setMenuRadius(10f) // sets the corner radius.
                            .setMenuShadow(10f) // sets the shadow.
                            .setTextColor(Color.GRAY)
                            .setTextGravity(Gravity.CENTER)
                            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                            .setSelectedTextColor(Color.WHITE)
                            .setMenuColor(Color.WHITE)
                            .setSelectedMenuColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary))
                            .setOnMenuItemClickListener(onMenuItemClickListener)
                            .build();

                       switch (userModel.getRole()){
                           case "member":
                               powerMenu.addItem(new PowerMenuItem("Promote to admin"));
                               powerMenu.addItem(new PowerMenuItem("Promote to moderator"));
                               break;
                           case "moderator":
                               powerMenu.addItem(new PowerMenuItem("Promote to admin"));
                               powerMenu.addItem(new PowerMenuItem("Demote to member"));
                               break;
                           case "admin":
                               powerMenu.addItem(new PowerMenuItem("Demote to moderator"));
                               powerMenu.addItem(new PowerMenuItem("Demote to member"));
                               break;
                       }

                    powerMenu.showAsDropDown(v);

                        }
            });

            onMenuItemClickListener = (position, items) -> {
                //        Toast.makeText(context, item.getTitle() + position, Toast.LENGTH_SHORT).show();
                powerMenu.setSelectedPosition(position); // change selected item
                switch (items.getTitle()) {
                    case "Demote to moderator":
                    case "Promote to moderator":
                        setRole("moderator",userModel);
                        break;
                    case "Promote to admin":
                        setRole("admin",userModel);
                        break;
                    case "Demote to member":
                        setRole("member",userModel);


                }
                powerMenu.dismiss();
            };
        }

        private void setRole(String mod,UserModel userModel) {
            Utils.role=mod;
            Log.i("123321", "194:"+userModel.getU_id());
            String message="You are now "+mod+" of Freelancing X.";
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("user").child(userModel.getU_id());
            databaseReference.child("role").setValue(mod).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(itemView.getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            });
            DatabaseReference databaseReference1=databaseReference.child("notification").push();
            databaseReference1.child("message").setValue(message);
            databaseReference1.child("time").setValue(System.currentTimeMillis());

            Notify2.SendIt(message,userModel.getU_id(),userModel.getU_id());
        }
    }
}
