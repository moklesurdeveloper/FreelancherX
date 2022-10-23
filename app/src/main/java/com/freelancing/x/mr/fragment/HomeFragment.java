package com.freelancing.x.mr.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.devs.readmoreoption.ReadMoreOption;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.freelancing.x.EditActivity;
import com.freelancing.x.PostingActivity;
import com.freelancing.x.R;
import com.freelancing.x.mr.MainActivity;
import com.freelancing.x.mr.Notify;
import com.freelancing.x.mr.SignInActivity;
import com.freelancing.x.mr.model.Model;
import com.freelancing.x.mr.model.PostModel;
import com.freelancing.x.mr.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.nested)
    NestedScrollView nested;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.like_icon)
    ImageView likeIcon;
    @BindView(R.id.post_like)
    TextView postLike;
    @BindView(R.id.like)
    CardView like;
    @BindView(R.id.like_icon2)
    ImageView likeIcon2;
    @BindView(R.id.post_like2)
    TextView postLike2;
    @BindView(R.id.improve)
    CardView improve;
    @BindView(R.id.comment_icon)
    ImageView commentIcon;
    @BindView(R.id.post_comment)
    TextView postComment;
    @BindView(R.id.comment)
    CardView comment;
    @BindView(R.id.category)
    TextView category;
    @BindView(R.id.post_text)
    TextView postText;
    @BindView(R.id.post_image)
    ImageView postImage;
    @BindView(R.id.image_holder)
    RelativeLayout imageHolder;
    @BindView(R.id.post_name)
    TextView postName;
    @BindView(R.id.post_time)
    TextView postTime;
    @BindView(R.id.divider2)
    View divider2;
    @BindView(R.id.post_icon)
    CircleImageView postIcon;
    @BindView(R.id.pin)
    LinearLayout pin;

    private boolean mIsLoading = false;
    @BindView(R.id.post_edit)
    TextView postEdit;
    @BindView(R.id.profile)
    CircleImageView profile;
    private FirebaseUser user;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.frameLayout)
    LinearLayout frameLayout;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.pin_title)
    TextView pinTitle;
    @BindView(R.id.pin_image)
    ImageView pinImage;
    @BindView(R.id.top)
    LinearLayout top;
    @BindView(R.id.scrollView2)
    ScrollView scrollView2;
    PostAdapter adapter;
    private ImageView photo;
    private ImageView remove;
    private String image_link = "";
    private Intent data;
    String total_like, total_comment;
    String total_like2, total_comment2;
    boolean liked = false;
    boolean liked2 = false;


    OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;
    PowerMenu powerMenu;

    public HomeFragment() {
        // Required empty public constructor
    }

    private ProgressBar imageUploadProgress;
    private List<String> item = new ArrayList<>();
    private List<String> key = new ArrayList<>();
    private List<PostModel> postList = new ArrayList<>();
    private boolean isLoading = false;
    private Bitmap bitmap;
    private View dialogView;


    //oncreate
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        Toolbar toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
        Spinner spinner = toolbar.findViewById(R.id.spinner2);
        BottomNavigationView navBar = getActivity().findViewById(R.id.navigation);
        navBar.setVisibility(View.VISIBLE);
        frameLayout.requestFocus();

        user = FirebaseAuth.getInstance().getCurrentUser();
        //initScrollListener();
        postSetup();
        noticeSetup();
        setPinPost();
        //   setupRecycler1();
        nested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    if (item.size() != 0)
                        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().startAt(item.get(item.size() - 1)).limitToFirst(10));
                    progressBar2.setVisibility(View.VISIBLE);
                    isLoading = true;
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(10));


        swipe.setOnRefreshListener(() -> {
            key.clear();
            item.clear();
            postList.clear();
            adapter.notifyDataSetChanged();
            setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(15));
        });
        Model.setI(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((TextView) parent.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (position == 0) {
                    Log.i("123321", "200:" + spinner.getSelectedItem().toString());
                    setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(10));
                }


                //  setupRecycler1();

                else {
                    String txt = spinner.getSelectedItem().toString();
                    setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(10));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        adapter = new PostAdapter(getActivity(), postList);
        adapter.setHasStableIds(true);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(adapter);
        return view;
    }


    private void noticeSetup() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/pin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    pinTitle.setText(dataSnapshot.child("title").getValue().toString());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(pinImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setPinPost() {
        pin.setVisibility(View.GONE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/pinPost");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("post_id").exists())
                {
                    Log.i("123321", "312:"+snapshot.child("post_id").getValue().toString());
                   DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("post").child(snapshot.child("post_id").getValue().toString());
                   databaseReference1.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           Log.i("123321", "317:" + dataSnapshot);
                           if (dataSnapshot.exists()) {
                               pin.setVisibility(View.VISIBLE);
                               setDetails(
                                       dataSnapshot.child("name").getValue().toString(),
                                       dataSnapshot.child("image").getValue().toString(),
                                       dataSnapshot.child("post_id").getValue().toString(),
                                       dataSnapshot.child("text").getValue().toString(),
                                       dataSnapshot.child("time").getValue(Long.class),
                                       dataSnapshot.child("user_id").getValue().toString(),
                                       dataSnapshot.child("category").getValue().toString(),
                                       dataSnapshot.child("post_image").getValue().toString()

                               );
                           }
                           else
                           pin.setVisibility(View.GONE);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                }
                else pin.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setDetails(String name, String image, String post_id, String text, long time, String user_id, String Scategory, String post_image) {
        postImage.setImageDrawable(getResources().getDrawable(R.drawable.empty));
        Context context = getActivity();
        imageHolder.setVisibility(View.GONE);

        if (text.equals("")) {
            postText.setVisibility(View.GONE);
        } else {
            postText.setVisibility(View.VISIBLE);
        }
        postName.setText(name);
        if (!post_image.equals("")) {
            imageHolder.setVisibility(View.VISIBLE);


            Picasso.get().load(post_image).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(postImage, new Callback() {
                @Override

                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.i("123321", "675:" + e.getMessage());
                }
            });

        }
        // text_txt.setText(Html.fromHtml(text));
        postTime.setText(reformat(time));
        category.setText(Scategory);

        postImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("name", name);
            intent.putExtra("image", image);
            intent.putExtra("post_id", post_id);
            intent.putExtra("total_comment", postComment.getText().toString());
            intent.putExtra("total_like", total_like);
            intent.putExtra("time", reformat(time));
            intent.putExtra("user_id", user_id);
            intent.putExtra(Utils.post_image, post_image);
            context.startActivity(intent);
        });
        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(getActivity()).build();
        readMoreOption.addReadMoreTo(postText, Html.fromHtml(text));
        try {
            //text_txt.setText(Html.fromHtml(text));
        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        DatabaseReference comment_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment");
        comment_count.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    total_comment = dataSnapshot.getChildrenCount() + "";

                    postComment.setText(total_comment);
                } catch (Exception e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference like_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like");
        like_count.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_like = dataSnapshot.getChildrenCount() + "";

                postLike.setText(total_like);
                if (user != null) {


                    if (dataSnapshot.child(user.getUid() + "").exists()) {
                        liked = true;
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));


                    } else {
                        liked = false;
                        likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference like_count2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2");
        like_count2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_like2 = dataSnapshot.getChildrenCount() + "";
                postLike2.setText(total_like2);
                if (user != null) {


                    if (dataSnapshot.child(user.getUid()).exists()) {
                        liked2 = true;
                        likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l2));

                    } else {
                        liked2 = false;
                        likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l1));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        like.setOnClickListener(v ->

        {

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading");
            progressDialog.show();
            if (user != null) {
                DatabaseReference like_database = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                if (liked) {
                    likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                    liked = false;
                    like_database.removeValue();


                } else {
                    liked = true;
                    // like_database.child(user_id).setValue(name);
                    likeIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));
                    like_database.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("123321", "491: liked =" + liked);
                            Notify.notify(post_id, post_id, total_like, "like", "post", name, user_id, "");
                        }
                    });


                }
                progressDialog.dismiss();

            } else {
                progressDialog.dismiss();
                Builder builder = new Builder(context);
                builder.setTitle("Sign In Required");
                builder.setMessage("You need to Sign In to use the feature");
                builder.setPositiveButton("ok", (dialog, which) -> context.startActivity(new Intent(context, SignInActivity.class)));
                builder.setNegativeButton("cancel", null);
                builder.show();

            }

        });


        improve.setOnClickListener(v ->

        {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("loading");
            progressDialog.show();
            if (user != null) {
                DatabaseReference like_database2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2").child(user.getUid());


                //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                if (liked2) {

                    likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l1));
                    liked2 = false;
                    like_database2.removeValue();


                } else {
                    liked2 = true;
                    likeIcon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l2));
                    like_database2.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i("123321", "491: liked =" + liked);
                            Notify.notify(post_id, post_id, total_like2, "like2", "post", name, user_id, "");
                        }
                    });


                }
                progressDialog.dismiss();

            } else {
                progressDialog.dismiss();
                Builder builder = new Builder(context);
                builder.setTitle("Sign In Required");
                builder.setMessage("You need to Sign In to use the feature");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(context, SignInActivity.class));
                    }
                });
                builder.setNegativeButton("cancel", null);
                builder.show();

            }

        });


        postIcon.setOnClickListener(v -> intentUser(user_id, name, image));


        Picasso.get().load(image).placeholder(R.drawable.ic_user).into(postIcon);
        postTime.setText(reformat(time));
        comment.setOnClickListener(v -> {

            MainActivity activity = (MainActivity) (context);
            activity.setComment(true);
            PostDetailActivity nextFrag = new PostDetailActivity();

            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("name", name);
            intent.putExtra("image", image);
            intent.putExtra("post_id", post_id);
            intent.putExtra("total_comment", postComment.getText().toString());
            intent.putExtra("total_like", total_like);
            intent.putExtra("time", reformat(time));
            intent.putExtra("user_id", user_id);
            intent.putExtra(Utils.post_image, post_image);
            context.startActivity(intent);


            // OR using options to customize


        });
        if(!Utils.role.equals("admin"))
            more.setVisibility(View.GONE);

        more.setOnClickListener(v ->
        {


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
            if (user.getUid().equals(user_id)) {
                powerMenu.addItem(new PowerMenuItem("Edit"));
                powerMenu.addItem(new PowerMenuItem("Unpin this post"));
                powerMenu.addItem(new PowerMenuItem("Delete"));
            } else {
                powerMenu.addItem(new PowerMenuItem("Delete"));
                powerMenu.addItem(new PowerMenuItem("Report"));
                powerMenu.addItem(new PowerMenuItem("Unpin this post"));
            }
            powerMenu.showAsDropDown(v);
        });

        onMenuItemClickListener = (position, items) -> {
            //        Toast.makeText(context, item.getTitle() + position, Toast.LENGTH_SHORT).show();
            powerMenu.setSelectedPosition(position); // change selected item
            switch (items.getTitle()) {
                case "Unpin this post":
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("admin/pinPost");
                    databaseReference1.removeValue();

                break;
                case "Report": {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(post_id);
                    databaseReference.child("user_id").setValue(user.getUid());
                    Toast.makeText(context, "Report Success", Toast.LENGTH_SHORT).show();
                }
                break;
                case "Delete":
                    Builder builder = new Builder(context);
                    builder.setTitle("Delete Post");
                    builder.setMessage("Are you Sure !");
                    builder.setPositiveButton("ok", (dialog, which) -> {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id);
                        databaseReference.removeValue();
                        key.clear();
                        item.clear();
                        postList.clear();
                        adapter.notifyDataSetChanged();
                        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(15));
                        Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton("cancel", null);
                    builder.show();
                    break;
                case "Edit": {
                    Intent intent = new Intent(getActivity(), EditActivity.class);
                    String finalText = text.replace("<br>", "\n");
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("text", finalText);
                    intent.putExtra("image", post_image);
                    startActivity(intent);
                }


                break;
            }
            powerMenu.dismiss();
        };
    }

    private void intentUser(String user_id, String name, String image) {

        Intent intent = new Intent(getActivity(), UserActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_name", name);
        intent.putExtra("user_profile", image);
        startActivity(intent);
    }

    private void setupRecycler(Query query) {
        shimmerViewContainer.startShimmerAnimation();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 1) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("post_id").exists()) {
                            PostModel model = new PostModel(
                                    snapshot.child("name").exists() ? getData(snapshot.child("name").getValue().toString()) : "",
                                    snapshot.child("image").exists() ? getData(snapshot.child("image").getValue().toString()) : "",
                                    getData(snapshot.child("post_id").getValue().toString()),
                                    getData(snapshot.child("text").getValue().toString()),
                                    getData(snapshot.child("user_id").getValue().toString()),
                                    getData(snapshot.child("category").getValue().toString()),
                                    getData(snapshot.child("post_image").exists() ? snapshot.child("post_image").getValue().toString() : ""),
                                    getData2(snapshot.child("time").getValue(Long.class))
                            );
                            if (!key.contains(snapshot.getKey())) {
                                key.add(snapshot.getKey());
                                postList.add(model);
                                item.add(snapshot.getKey());
                            }
                        } else {
                            Log.i("123321", "290:" + snapshot.getKey());
                        }
                    }

                }


                //    shimmerViewContainer.stopShimmerAnimation();
                // shimmerViewContainer.setVisibility(View.GONE);
                swipe.setRefreshing(false);

                if (mIsLoading) {
                    Log.i("123321", "318:" + item.size());
                    adapter.notifyDataSetChanged();
                } else {

                    mIsLoading = true;
                    adapter.notifyDataSetChanged();
                }
                progressBar2.setVisibility(View.GONE);
                if (dataSnapshot.getChildrenCount() >= 10)
                    isLoading = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!item.isEmpty()) {
            if (mIsLoading) {
                Log.i("123321", "318");
                adapter.notifyDataSetChanged();
            } else {
                Log.i("123321", "322");
                mIsLoading = true;
                adapter = new PostAdapter(getActivity(), postList);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
            }

        }
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


    private void postSetup() {
        if (user != null) {


            Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.ic_user).into(profile);
        }
    }

    @OnClick(R.id.post_edit)
    public void onViewClicked() {
        Log.i("123321", "318:" + image_link);
        image_link = "";
        if (user != null)
            startActivity(new Intent(getActivity(), PostingActivity.class));

        else {
            Builder builder = new Builder(getActivity());
            builder.setTitle("Sign In Required");
            builder.setMessage("You need to Sign In to use the feature");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().startActivity(new Intent(getActivity(), SignInActivity.class));
                }
            });
            builder.setNegativeButton("cancel", null);
            builder.show();

        }
    }

    @OnClick(R.id.more)
    public void abc() {
        setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().startAt(item.get(item.size() - 1)).limitToFirst(5));
    }

    @Override
    public void onStart() {
        super.onStart();
        //mAdapter.startListening();
    }

    //Stop Listening Adapter
    @Override
    public void onStop() {
        super.onStop();
//        mAdapter.stopListening();
    }

    public void onActivityResult(int i, int j, Intent data) {
        this.data = data;

        if (j == Activity.RESULT_OK) {
            image_link = "";
            uploadToServer("http://nul.com");

            uploadToServer(ImagePicker.Companion.getFilePath(data));

            try {
                Uri selectedImg = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImg);
                imageUploadProgress.setVisibility(View.VISIBLE);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(i, j, data);


    }

    private void uploadToServer(String url) {
        Log.i("123321", "Uploading.................................");
        MediaManager.get().upload(url)
                .unsigned("sample_preset")
                .option("resource_type", "auto")
                .option("folder", "image")
                .option("public_id", "IMG_" + System.currentTimeMillis())
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.i("123321", "start:" + requestId);

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                        Log.i("123321", "total:" + totalBytes + " byte:" + bytes);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        Log.i("123321", "41:" + resultData);
                        imageUploadProgress.setVisibility(View.GONE);
                        remove.setVisibility(View.VISIBLE);
                        image_link = Objects.requireNonNull(resultData.get("url")).toString();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Log.i("123321", "42:" + error.getDescription());
                        if (!error.getDescription().equals("File 'http://nul.com' does not exist"))
                            Snackbar.make(dialogView, "Something want's wrong!", Snackbar.LENGTH_INDEFINITE).setAction("Retry", v -> {
                                uploadToServer("http://null.com");
                                uploadToServer(ImagePicker.Companion.getFilePath(data));
                            }).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        Log.i("123321", "error:::" + error.getDescription());

                    }
                })
                .dispatch();
    }

    public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ProductViewHolder> {


        private Context mCtx;
        private List<PostModel> productList;

        public PostAdapter(Context mCtx, List<PostModel> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.post, null);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            PostModel postModel = productList.get(position);
            holder.setDetails(postModel.getName(), postModel.getImage(), postModel.getPost_id(), postModel.getText(), postModel.getTime(), postModel.getUser_id(), postModel.getCategory(), postModel);
            //loading the image

        }

        @Override
        public int getItemCount() {
            //Log.i("123321", "45:"+productList.size());
            return productList.size();

        }

        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView name_txt, time_txt, text_txt, comment_text, like_text, category_text, like_text2;
            ImageView photo, comment_icon, like_icon, more, like_icon2, post_image;
            boolean liked = false;
            boolean liked2 = false;
            RelativeLayout image_holder;

            CardView like, improve, comment;
            Context context;
            String total_like, total_comment;
            String total_like2, total_comment2;
            FirebaseUser user;
            OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener;
            PowerMenu powerMenu;

            public ProductViewHolder(View itemView) {
                super(itemView);
                category_text = itemView.findViewById(R.id.category);
                more = itemView.findViewById(R.id.more);
                name_txt = itemView.findViewById(R.id.post_name);
                text_txt = itemView.findViewById(R.id.post_text);
                like_icon = itemView.findViewById(R.id.like_icon);
                like_icon2 = itemView.findViewById(R.id.like_icon2);
                time_txt = itemView.findViewById(R.id.post_time);
                photo = itemView.findViewById(R.id.post_icon);
                post_image = itemView.findViewById(R.id.post_image);
                image_holder = itemView.findViewById(R.id.image_holder);
                comment_icon = itemView.findViewById(R.id.comment_icon);
                comment_text = itemView.findViewById(R.id.post_comment);
                like_text = itemView.findViewById(R.id.post_like);
                like_text2 = itemView.findViewById(R.id.post_like2);
                like = itemView.findViewById(R.id.like);
                improve = itemView.findViewById(R.id.improve);
                comment = itemView.findViewById(R.id.comment);
                context = itemView.getContext();
                user = FirebaseAuth.getInstance().getCurrentUser();

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

            private void intentUser(String user_id, String name, String image) {

                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", name);
                intent.putExtra("user_profile", image);
                context.startActivity(intent);
            }

            public void setDetails(String name, String image, String post_id, String text, long time, String user_id, String category, PostModel model) {
                post_image.setImageDrawable(itemView.getContext().getDrawable(R.drawable.empty));
                image_holder.setVisibility(View.GONE);
                if (text.equals("")) {
                    text_txt.setVisibility(View.GONE);
                } else {
                    text_txt.setVisibility(View.VISIBLE);
                }
                name_txt.setText(name);
                if (!model.getPost_image().equals("")) {
                    image_holder.setVisibility(View.VISIBLE);
                    Log.i("123321", "652:text:" + text + "  image:" + model.getPost_image());

                    Picasso.get().load(model.getPost_image()).placeholder(R.drawable.ic_rectangle_2).error(R.drawable.ic_group_1).into(post_image, new Callback() {
                        @Override

                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Log.i("123321", "675:" + e.getMessage());
                        }
                    });

                }
                // text_txt.setText(Html.fromHtml(text));
                time_txt.setText(reformat(time));
                category_text.setText(category);

                post_image.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("name", name);
                    intent.putExtra("image", image);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("total_comment", comment_text.getText().toString());
                    intent.putExtra("total_like", total_like);
                    intent.putExtra("time", reformat(time));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra(Utils.post_image, model.getPost_image());
                    context.startActivity(intent);
                });
                ReadMoreOption readMoreOption = new ReadMoreOption.Builder(getActivity()).build();
                readMoreOption.addReadMoreTo(text_txt, Html.fromHtml(text));
                try {
                    //text_txt.setText(Html.fromHtml(text));
                } catch (Exception e) {
                    Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

                DatabaseReference comment_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("comment");
                comment_count.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        try {
                            total_comment = dataSnapshot.getChildrenCount() + "";

                            comment_text.setText(total_comment);
                        } catch (Exception e) {
                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                DatabaseReference like_count = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like");
                like_count.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        total_like = dataSnapshot.getChildrenCount() + "";
                        like_text.setText(total_like);
                        if (user != null) {


                            if (dataSnapshot.child(user.getUid() + "").exists()) {
                                liked = true;
                                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));


                            } else {
                                liked = false;
                                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                DatabaseReference like_count2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2");
                like_count2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        total_like2 = dataSnapshot.getChildrenCount() + "";
                        like_text2.setText(total_like2);
                        if (user != null) {


                            if (dataSnapshot.child(user.getUid()).exists()) {
                                liked2 = true;
                                like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l2));

                            } else {
                                liked2 = false;
                                like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l1));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                like.setOnClickListener(v ->

                {

                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    if (user != null) {
                        DatabaseReference like_database = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


                        //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                        if (liked) {
                            like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
                            liked = false;
                            like_database.removeValue();


                        } else {
                            liked = true;
                            // like_database.child(user_id).setValue(name);
                            like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart2));
                            like_database.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.i("123321", "491: liked =" + liked);
                                    Notify.notify(post_id, post_id, total_like, "like", "post", name, user_id, "");
                                }
                            });


                        }
                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Builder builder = new Builder(context);
                        builder.setTitle("Sign In Required");
                        builder.setMessage("You need to Sign In to use the feature");
                        builder.setPositiveButton("ok", (dialog, which) -> context.startActivity(new Intent(context, SignInActivity.class)));
                        builder.setNegativeButton("cancel", null);
                        builder.show();

                    }

                });


                improve.setOnClickListener(v ->

                {
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("loading");
                    progressDialog.show();
                    if (user != null) {
                        DatabaseReference like_database2 = FirebaseDatabase.getInstance().getReference("post").child(post_id).child("like2").child(user.getUid());


                        //                like_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                        if (liked2) {

                            like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l1));
                            liked2 = false;
                            like_database2.removeValue();


                        } else {
                            liked2 = true;
                            like_icon2.setImageDrawable(context.getResources().getDrawable(R.drawable.l2));
                            like_database2.setValue(user.getDisplayName()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.i("123321", "491: liked =" + liked);
                                    Notify.notify(post_id, post_id, total_like2, "like2", "post", name, user_id, "");
                                }
                            });


                        }
                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Builder builder = new Builder(context);
                        builder.setTitle("Sign In Required");
                        builder.setMessage("You need to Sign In to use the feature");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, SignInActivity.class));
                            }
                        });
                        builder.setNegativeButton("cancel", null);
                        builder.show();

                    }

                });


                photo.setOnClickListener(v -> intentUser(user_id, name, image));


                Picasso.get().load(image).placeholder(R.drawable.ic_user).into(photo);
                time_txt.setText(reformat(time));
                comment.setOnClickListener(v -> {

                    MainActivity activity = (MainActivity) (context);
                    activity.setComment(true);
                    PostDetailActivity nextFrag = new PostDetailActivity();

                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("text", text);
                    intent.putExtra("name", name);
                    intent.putExtra("image", image);
                    intent.putExtra("post_id", post_id);
                    intent.putExtra("total_comment", comment_text.getText().toString());
                    intent.putExtra("total_like", total_like);
                    intent.putExtra("time", reformat(time));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra(Utils.post_image, model.getPost_image());
                    context.startActivity(intent);


                    // OR using options to customize


                });

                more.setOnClickListener(v ->
                {


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
                    if (user.getUid().equals(user_id)) {
                        powerMenu.addItem(new PowerMenuItem("Edit"));
                        powerMenu.addItem(new PowerMenuItem("Delete"));
                    } else {
                        powerMenu.addItem(new PowerMenuItem("Report"));
                    }
                    Log.i("123321", "1279:" + Utils.role);
                    if (Utils.role.equals("admin") && !user.getUid().equals(user_id)) {

                        powerMenu.addItem(new PowerMenuItem("Delete"));
                        powerMenu.addItem(new PowerMenuItem("Pin this post"));

                    }
                    if (Utils.role.equals("admin") && user.getUid().equals(user_id)) {
                        powerMenu.addItem(new PowerMenuItem("Pin this post"));

                    }
                    powerMenu.showAsDropDown(v);
                });

                onMenuItemClickListener = (position, items) -> {
                    //        Toast.makeText(context, item.getTitle() + position, Toast.LENGTH_SHORT).show();
                    powerMenu.setSelectedPosition(position); // change selected item
                    switch (items.getTitle()) {
                        case "Pin this post": {
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("admin/pinPost");
                        databaseReference1.child("post_id").setValue(post_id).addOnSuccessListener(aVoid ->
                                Toast.makeText(context, "Post Pin Success", Toast.LENGTH_SHORT).show());

                        }
                        break;
                        case "Report": {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("admin/report").child(post_id);
                            databaseReference.child("user_id").setValue(user.getUid());
                            Toast.makeText(context, "Report Success", Toast.LENGTH_SHORT).show();
                        }
                        break;
                        case "Delete":
                            Builder builder = new Builder(context);
                            builder.setTitle("Delete Post");
                            builder.setMessage("Are you Sure !");
                            builder.setPositiveButton("ok", (dialog, which) -> {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("post").child(post_id);
                                databaseReference.removeValue();
                                key.clear();
                                item.clear();
                                postList.clear();
                                adapter.notifyDataSetChanged();
                                setupRecycler(FirebaseDatabase.getInstance().getReference("post").orderByKey().limitToFirst(15));
                                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                            });
                            builder.setNegativeButton("cancel", null);
                            builder.show();
                            break;
                        case "Edit": {
                            Intent intent = new Intent(getActivity(), EditActivity.class);
                            String finalText = text.replace("<br>", "\n");
                            intent.putExtra("post_id", post_id);
                            intent.putExtra("text", finalText);
                            intent.putExtra("image", model.getPost_image());
                            startActivity(intent);
                        }


                        break;
                    }
                    powerMenu.dismiss();
                };
            }


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