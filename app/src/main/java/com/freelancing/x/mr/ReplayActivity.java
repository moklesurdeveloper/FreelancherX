package com.freelancing.x.mr;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.freelancing.x.R;
import com.freelancing.x.mr.fragment.UserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.squareup.picasso.Picasso;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReplayActivity extends AppCompatActivity {
    String image;

    FirebaseRecyclerAdapter replayAdapter;
String link;
    String text;
    String text5;
    @BindView(R.id.replay_recycler)
    RecyclerView replay_recycler;
    String post_id, key, user_id;
    PowerMenu powerMenu;
    FirebaseUser user;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.replay_edt_box)
    EditText replay_edt;
    @BindView(R.id.rep)
    TextView rep;
    @BindView(R.id.post_name)
    TextView postName;
    @BindView(R.id.post_text)
    TextView postText;
    @BindView(R.id.post_icon)
    CircleImageView postIcon;
    @BindView(R.id.report)
    ImageView report;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.edit)
    ImageView edit;
    private ContextMenuDialogFragment mMenuDialogFragment;
    OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        getExtra();
        user = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        replay_recycler.setLayoutManager(manager);
        setupReplayAdapter(key);
        if ((FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() + "").equals(image)) {
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            report.setVisibility(View.VISIBLE);
        }


        edit.setOnClickListener(v -> {


            if (user != null) {

                final AlertDialog dialogBuilder = new Builder(ReplayActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.post_dialog2, null);

                final EditText editText = dialogView.findViewById(R.id.write);
                final Spinner spinner = dialogView.findViewById(R.id.spinner);
                spinner.setVisibility(View.INVISIBLE);
                TextView button1 = dialogView.findViewById(R.id.post_button);
                ImageView button2 = dialogView.findViewById(R.id.close);
                String text00 = postText.getText().toString().replaceAll("<br />","\n");

                editText.setText(text00);

                button2.setOnClickListener(view -> dialogBuilder.dismiss());
                button1.setOnClickListener(view -> {

                    // DO SOMETHINGS
                    ProgressDialog progressDialog = new ProgressDialog(ReplayActivity.this);
                    progressDialog.setMessage("loading");
                    progressDialog.show();

                    String text1 = editText.getText().toString();
                    if (!text1.isEmpty()) {

                        Map<String, Object> map = new HashMap<>();
                        Long updated = 9999999999999L;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(post_id).child("comment").child(key);

                        String text0 = text1.replaceAll("\\n", "<br />");
                        databaseReference.child("text").setValue(text0);
                        postText.setText(Html.fromHtml(text0));

                        dialogBuilder.dismiss();
                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please Write Your Message and select a category", Toast.LENGTH_SHORT).show();
                    }

                });


                dialogBuilder.setView(dialogView);
                dialogBuilder.show();
            }

        });

        delete.setOnClickListener(v -> {
            Builder builder = new Builder(ReplayActivity.this);
            builder.setTitle("Delete Post");
            builder.setMessage("Are you sure !");
            builder.setPositiveButton("ok", (dialog, which) -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment").child(key);
                databaseReference.removeValue();
                Toast.makeText(getApplicationContext(), " Deleted", Toast.LENGTH_SHORT).show();
                finish();
             //   Intent intent = new Intent(getApplicationContext(), ReplayActivity.class);
//                intent.putExtra("key", key);
//                intent.putExtra("name", name);
//                intent.putExtra("user_id", id);
//                intent.putExtra("text", text);
//                intent.putExtra("image", profile);
//                intent.putExtra("post_id", post_id);
                //   startActivity(intent);
            });
            builder.setNegativeButton("cancel", null);
            builder.show();


        });

        report.setOnClickListener(v -> {
            if (user != null) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(key);
                databaseReference.child("user_id").setValue(user.getUid());
                databaseReference.child("post_id").setValue(post_id);
                databaseReference.child("comment_id").setValue(key);
              //  databaseReference.child("replay_id").setValue(key_rep);
                Toast.makeText(getApplicationContext(), "Report Success", Toast.LENGTH_SHORT).show();

            } else {
                Builder builder = new Builder(ReplayActivity.this);
                builder.setTitle("Sign In Required");
                builder.setMessage("You need to Sign In to use the feature");
                builder.setPositiveButton("ok", (dialog, which) -> getApplicationContext().startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
                builder.setNegativeButton("cancel", null);
                builder.show();

            }
        });

        more.setOnClickListener(v ->
        {
            Context context = ReplayActivity.this;

            powerMenu = new PowerMenu.Builder(context)
                    // .addItemList(list) // list has "Novel", "Poerty", "Art"
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextColor(Color.GRAY)
                    .setTextGravity(Gravity.CENTER)
                    .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                    .setSelectedTextColor(Color.WHITE)
                    .setMenuColor(Color.WHITE)
                    .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setOnMenuItemClickListener(onMenuItemClickListener)
                    .build();
            //    powerMenu.addItem(new PowerMenuItem());
            if ((user.getPhotoUrl() + "").equals(image)) {
                powerMenu.addItem(new PowerMenuItem("Edit"));
                powerMenu.addItem(new PowerMenuItem("Delete"));
            } else powerMenu.addItem(new PowerMenuItem("Report"));
            powerMenu.showAsDropDown(v);
        });

        onMenuItemClickListener = (position, item) -> {
            /* Toast.makeText(getApplicationContext(), item.getTitle() +position, Toast.LENGTH_SHORT).show(); */
            powerMenu.setSelectedPosition(position); // change selected item
            switch (item.getTitle()) {
                case "Report": {
                    if (user != null) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(key);
                        databaseReference.child("user_id").setValue(user.getUid());
                        databaseReference.child("post_id").setValue(post_id);
                        Toast.makeText(getApplicationContext(), "Report Success", Toast.LENGTH_SHORT).show();

                    } else {
                        Builder builder = new Builder(ReplayActivity.this);
                        builder.setTitle("Sign In Required");
                        builder.setMessage("You need to Sign In to use the feature");
                        builder.setPositiveButton("ok", (dialog, which) -> startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
                        builder.setNegativeButton("cancel", null);
                        builder.show();

                    }
                }
                break;
                case "Delete":
                    Builder builder = new Builder(ReplayActivity.this);
                    builder.setTitle("Delete Post");
                    builder.setMessage("Are you sure ?");
                    builder.setPositiveButton("ok", (dialog, which) -> {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(link);
                        databaseReference.removeValue();
                        Toast.makeText(getApplicationContext(), "Post Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                        //startActivity(new Intent(this, MainActivity.class));

                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                    break;
                case "Edit": {
                    if (user != null) {
                        Log.i("123321", "273");

                        final AlertDialog dialogBuilder = new Builder(ReplayActivity.this).create();
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.post_dialog2, null);

                        final EditText editText = dialogView.findViewById(R.id.write);
                        final Spinner spinner = dialogView.findViewById(R.id.spinner);
                        spinner.setVisibility(View.INVISIBLE);
                        TextView button1 = dialogView.findViewById(R.id.post_button);
                        ImageView button2 = dialogView.findViewById(R.id.close);
                        editText.setText(text5);

                        button2.setOnClickListener(view -> dialogBuilder.dismiss());
                        button1.setOnClickListener(view -> {

                            // DO SOMETHINGS
                            ProgressDialog progressDialog = new ProgressDialog(ReplayActivity.this);
                            progressDialog.setMessage("loading");
                            progressDialog.show();

                            String text1 = editText.getText().toString();
                            if (!text.isEmpty()) {

                                Map<String, Object> map = new HashMap<>();
                                Long updated = 9999999999999L;
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(link);

                                databaseReference.child("text").setValue(text1);
                                postText.setText(Html.fromHtml(text1));

                                dialogBuilder.dismiss();
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Please Write Your Message and select a category", Toast.LENGTH_SHORT).show();
                            }

                        });


                        dialogBuilder.setView(dialogView);
                        dialogBuilder.show();
                    }

                }

                break;
            }
            powerMenu.dismiss();
        };


    }

    private void setupReplayAdapter(final String key) {
        Query query = FirebaseDatabase.getInstance().getReference("post").child(link).child("replay");
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CommentModel>().setQuery(
                query, (DataSnapshot snapshot) -> {

                    try {
                        return new CommentModel
                                (snapshot.child("name").getValue().toString(), snapshot.child("profile").getValue().toString(), snapshot.child("text").getValue().toString(), snapshot.getKey(), snapshot.child("id").getValue().toString());
                    } catch (Exception e) {
                        return new CommentModel
                                ("no ", "", "no", snapshot.getKey(), "");

                    }
                }
        ).build();
        replayAdapter = new FirebaseRecyclerAdapter<CommentModel, ReplayViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReplayViewHolder replayViewHolder, int i, @NonNull CommentModel commentModel) {
                replayViewHolder.replay(commentModel.getName(), commentModel.getProfile(), commentModel.getText(), key, commentModel.getKey(), commentModel.getId());
            }


            @NonNull
            @Override
            public ReplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ReplayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_replay, parent, false));
            }
        };
        replay_recycler.setAdapter(replayAdapter);
        replayAdapter.startListening();


    }

    private void getExtra() {
        Intent intent = getIntent();

         link = intent.getStringExtra("key");
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("post").child(link);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i("123321", "371:"+dataSnapshot);
                    Log.i("123321", "372"+dataSnapshot.getKey());
                    key=dataSnapshot.getKey();
                    String name=dataSnapshot.child("name").getValue().toString();
                    String text5=dataSnapshot.child("text").getValue().toString();
                    user_id=dataSnapshot.child("id").getValue().toString();
                    postName.setText(name);
                    postText.setText(Html.fromHtml(text5));
                    Picasso.get().load(image).placeholder(R.drawable.ic_user).into(postIcon);
                    postIcon.setOnClickListener(v -> {
                        Intent intent1 = new Intent(getApplicationContext(), UserActivity.class);
                        intent1.putExtra("user_id", user_id);
                        intent1.putExtra("user_name", name);
                        intent1.putExtra("user_profile", image);
                        startActivity(intent1);
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






    }

    @OnClick({R.id.more, R.id.rep})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.rep:


                if ((user != null)) {

                    if (!replay_edt.getText().toString().isEmpty()) {
                        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        String txt = replay_edt.getText().toString();
                        replay_edt.setText("");
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(link).child("replay").push();
                        databaseReference.child("name").setValue(user.getDisplayName());
                        databaseReference.child("id").setValue(user.getUid());
                        databaseReference.child("profile").setValue("" + user.getPhotoUrl());


                        Notify.subscribe(databaseReference.getKey()+"owner");
                        Notify.subscribe(key+"viewer");
                        databaseReference.child("text").setValue(txt).addOnCompleteListener(task ->
                                Notify.notify(key,link,"","replay","comment",postText.getText().toString(),user_id,""));

//
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Write something", Toast.LENGTH_SHORT).show();
                    }
//
                } else {
                    Builder builder = new Builder(ReplayActivity.this);
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", (dialog, which) -> startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
                    builder.setNegativeButton("cancel", null);
                    builder.show();
//
                }
//


                break;
        }
    }


    public class ReplayViewHolder extends RecyclerView.ViewHolder {
        TextView replay_text, like_text_rep;
        ImageView post_icon, like_icon_rep, more;
        TextView post_name;
        EditText replay_edt;
        boolean liked2 = false;
        TextView replay_post;
        ImageView edit, report, delete;

        public ReplayViewHolder(@NonNull View itemView) {
            super(itemView);
            replay_text = itemView.findViewById(R.id.post_text);
            like_text_rep = itemView.findViewById(R.id.post_like_rep);
            like_icon_rep = itemView.findViewById(R.id.like_icon_rep);
            post_icon = itemView.findViewById(R.id.post_icon);
            post_name = itemView.findViewById(R.id.post_name);
            more = itemView.findViewById(R.id.more);
            edit = itemView.findViewById(R.id.edit);
            report = itemView.findViewById(R.id.report);
            delete = itemView.findViewById(R.id.delete);


        }

        public void replay(String name, String profile, String comment_text, String key, String key_rep, String id) {
            post_icon.setOnClickListener(v -> {
                Intent intent1 = new Intent(getApplicationContext(), UserActivity.class);
                intent1.putExtra("user_id", id);
                intent1.putExtra("user_name", name);
                intent1.putExtra("user_profile", profile);
                startActivity(intent1);
            });

            if ((FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() + "").equals(profile)) {
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                report.setVisibility(View.GONE);
            } else {
                edit.setVisibility(View.GONE);
                report.setVisibility(View.GONE);
                report.setVisibility(View.VISIBLE);
            }


            edit.setOnClickListener(v -> {


                if (user != null) {

                    final AlertDialog dialogBuilder = new Builder(ReplayActivity.this).create();
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.post_dialog2, null);

                    final EditText editText = dialogView.findViewById(R.id.write);
                    final Spinner spinner = dialogView.findViewById(R.id.spinner);
                    spinner.setVisibility(View.INVISIBLE);
                    TextView button1 = dialogView.findViewById(R.id.post_button);
                    ImageView button2 = dialogView.findViewById(R.id.close);
                    String text00 = comment_text.replaceAll("<br />","\n");

                    editText.setText(text00);

                    button2.setOnClickListener(view -> dialogBuilder.dismiss());
                    button1.setOnClickListener(view -> {

                        // DO SOMETHINGS
                        ProgressDialog progressDialog = new ProgressDialog(ReplayActivity.this);
                        progressDialog.setMessage("loading");
                        progressDialog.show();

                        String text1 = editText.getText().toString();
                        if (!text1.isEmpty()) {

                            Map<String, Object> map = new HashMap<>();
                            Long updated = 9999999999999L;
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(link).child("replay").child(key_rep);

                            databaseReference.child("text").setValue(text1);

                            dialogBuilder.dismiss();
                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please Write Your Message and select a category", Toast.LENGTH_SHORT).show();
                        }

                    });


                    dialogBuilder.setView(dialogView);
                    dialogBuilder.show();
                }

            });

            delete.setOnClickListener(v -> {
                Builder builder = new Builder(ReplayActivity.this);
                builder.setTitle("Delete Post");
                builder.setMessage("Are you sure ?");
                builder.setPositiveButton("ok", (dialog, which) -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(link).child("replay").child(key_rep);
                    databaseReference.removeValue();
                    Toast.makeText(getApplicationContext(), " Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ReplayActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("name", name);
                    intent.putExtra("user_id", id);
                    intent.putExtra("text", text);
                    intent.putExtra("image", profile);
                    intent.putExtra("post_id", post_id);
                    //   startActivity(intent);
                });
                builder.setNegativeButton("cancel", null);
                builder.show();


            });

            report.setOnClickListener(v -> {
                if (user != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(link);
                    databaseReference.child("user_id").setValue(user.getUid());
                    databaseReference.child("post_id").setValue(post_id);
                    databaseReference.child("comment_id").setValue(key);
                    databaseReference.child("replay_id").setValue(key_rep);
                    Toast.makeText(getApplicationContext(), "Report Success", Toast.LENGTH_SHORT).show();

                } else {
                    Builder builder = new Builder(ReplayActivity.this);
                    builder.setTitle("Sign In Required");
                    builder.setMessage("You need to Sign In to use the feature");
                    builder.setPositiveButton("ok", (dialog, which) -> getApplicationContext().startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
                    builder.setNegativeButton("cancel", null);
                    builder.show();

                }
            });
            more.setOnClickListener(v -> {


                String s = comment_text;

                powerMenu = new PowerMenu.Builder(ReplayActivity.this)
                        // .addItemList(list) // list has "Novel", "Poerty", "Art"
                        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                        .setMenuRadius(10f) // sets the corner radius.
                        .setMenuShadow(10f) // sets the shadow.
                        .setTextColor(Color.GRAY)
                        .setTextGravity(Gravity.CENTER)
                        .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                        .setSelectedTextColor(Color.WHITE)
                        .setMenuColor(Color.WHITE)
                        .setSelectedMenuColor(ContextCompat.getColor(ReplayActivity.this, R.color.colorPrimary))
                        .setOnMenuItemClickListener(onMenuItemClickListener)
                        .build();
                //    powerMenu.addItem(new PowerMenuItem());
                if ((user.getPhotoUrl() + "").equals(profile)) {

                    powerMenu.addItem(new PowerMenuItem("Delete"));
                    powerMenu.addItem(new PowerMenuItem("Edit"));
                } else powerMenu.addItem(new PowerMenuItem("Report"));
                powerMenu.showAsDropDown(v);
            });

            onMenuItemClickListener = (position, item) -> {
                /* Toast.makeText(getActivity(), item.getTitle() +position, Toast.LENGTH_SHORT).show(); */
                powerMenu.setSelectedPosition(position); // change selected item
                switch (item.getTitle()) {
                    case "Edit": {


                        if (user != null) {

                            final AlertDialog dialogBuilder = new Builder(ReplayActivity.this).create();
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.post_dialog, null);

                            final EditText editText = dialogView.findViewById(R.id.write);
                            final Spinner spinner = dialogView.findViewById(R.id.spinner);
                            spinner.setVisibility(View.INVISIBLE);
                            TextView button1 = dialogView.findViewById(R.id.post_button);
                            ImageView button2 = dialogView.findViewById(R.id.close);
                            editText.setText(comment_text);

                            button2.setOnClickListener(view -> dialogBuilder.dismiss());
                            button1.setOnClickListener(view -> {

                                // DO SOMETHINGS
                                ProgressDialog progressDialog = new ProgressDialog(ReplayActivity.this);
                                progressDialog.setMessage("loading");
                                progressDialog.show();

                                String text1 = editText.getText().toString();
                                if (!text.isEmpty()) {

                                    Map<String, Object> map = new HashMap<>();
                                    Long updated = 9999999999999L;
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("post").child(post_id).child("comment").child(key).child("replay").child(key_rep);

                                    databaseReference.child("text").setValue(text1);

                                    dialogBuilder.dismiss();
                                    progressDialog.dismiss();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Please Write Your Message and select a category", Toast.LENGTH_SHORT).show();
                                }

                            });


                            dialogBuilder.setView(dialogView);
                            dialogBuilder.show();
                        }

                    }

                    break;
                    case "Report": {
                        if (user != null) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(key);
                            databaseReference.child("user_id").setValue(user.getUid());
                            databaseReference.child("post_id").setValue(post_id);
                            databaseReference.child("comment_id").setValue(key);
                            databaseReference.child("replay_id").setValue(key_rep);
                            Toast.makeText(getApplicationContext(), "Report Success", Toast.LENGTH_SHORT).show();

                        } else {
                            Builder builder = new Builder(ReplayActivity.this);
                            builder.setTitle("Sign In Required");
                            builder.setMessage("You need to Sign In to use the feature");
                            builder.setPositiveButton("ok", (dialog, which) -> getApplicationContext().startActivity(new Intent(getApplicationContext(), SignInActivity.class)));
                            builder.setNegativeButton("cancel", null);
                            builder.show();

                        }
                    }
                    break;
                    case "Delete":
                        Builder builder = new Builder(ReplayActivity.this);
                        builder.setTitle("Delete Post");
                        builder.setMessage("Are you Sure !");
                        builder.setPositiveButton("ok", (dialog, which) -> {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment").child(key).child("replay").child(key_rep);
                            databaseReference.removeValue();
                            Toast.makeText(getApplicationContext(), " Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), ReplayActivity.class);
                            intent.putExtra("key", key);
                            intent.putExtra("name", name);
                            intent.putExtra("user_id", id);
                            intent.putExtra("text", text);
                            intent.putExtra("image", profile);
                            intent.putExtra("post_id", post_id);
                            // startActivity(intent);
                        });
                        builder.setNegativeButton("cancel", null);
                        builder.show();
                        break;
                }


                powerMenu.dismiss();
            };


            DatabaseReference like_count_rep = FirebaseDatabase.getInstance().getReference("post").child(link).child("replay").child(key_rep).child("like");
            like_count_rep.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (user != null) {


                        if (dataSnapshot.child(user.getUid() + "").exists()) {
                            String total_like2 = dataSnapshot.getChildrenCount() + "";
                            like_text_rep.setText(total_like2);
                            liked2 = true;
                            like_icon_rep.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));


                        } else {
                            liked2 = false;
                            like_text_rep.setText("0");
                            like_icon_rep.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            like_icon_rep.setOnClickListener(v ->

            {
                DatabaseReference like_database_rep = FirebaseDatabase.getInstance().getReference("post").child(link).child("replay").child(key_rep).child("like").child(user.getUid());

//                like_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                if (liked2) {

                    like_icon_rep.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up));
                    like_database_rep.removeValue();

                    //  setupReplayAdapter(key);

                    liked2 = false;
                } else {
                    like_icon_rep.setImageDrawable(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                    liked2 = true;

                    like_database_rep.setValue(user.getDisplayName()).addOnCompleteListener(task ->   Notify.notify(key,link,like_text_rep.getText().toString(),"like","replay",name,id,key_rep));



                    //   setupReplayAdapter(key);


                }

            });


            replay_text.setText(comment_text);
            try {
                Picasso.get().load(profile).placeholder(R.drawable.ic_user).into(post_icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            post_name.setText(name);


        }
    }
}
