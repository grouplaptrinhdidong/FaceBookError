package facebook.socialnetwork.nhom3.facebook;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class FriendRequestsActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private RecyclerView FriendRequestsList;
    private DatabaseReference FriendRequestRef, UserRef, FriendRef;
    private FirebaseAuth mAuth;
    private String currentUserID, saveCurrentDate;
    private String CURRENT_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        CURRENT_STATE = "not_friends";
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        FriendRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        FriendRequestRef = FirebaseDatabase.getInstance().getReference().child("FriendRequests").child(currentUserID);

        mToolbar = (Toolbar) findViewById(R.id.friend_requests_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("friend Requets");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FriendRequestsList =(RecyclerView) findViewById(R.id.friend_requests_list);
        FriendRequestsList.setHasFixedSize(true);
        FriendRequestsList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<FindFriends, FriendRequestsViewHolder> adapter =
                new FirebaseRecyclerAdapter<FindFriends, FriendRequestsViewHolder>
                        (
                                FindFriends.class,
                                R.layout.friend_requests_display_layout,
                                FriendRequestsViewHolder.class,
                                FriendRequestRef
                        )

                {
                    @Override
                    protected void populateViewHolder(final FriendRequestsViewHolder viewHolder, FindFriends model, final int position) {




                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
                        getTypeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){
                                    String type = dataSnapshot.getValue().toString();
                                    if (type.equals("received")){
                                        viewHolder.Acceptbtn.setVisibility(View.VISIBLE);
                                        //viewHolder.Declinebtn.setVisibility(View.VISIBLE);
                                        viewHolder.myStatus.setVisibility(View.VISIBLE);
                                        viewHolder.myName.setVisibility(View.VISIBLE);
                                        viewHolder.profileImage.setVisibility(View.VISIBLE);
                                        UserRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("profileimage")){
                                                    final String requestUsername = dataSnapshot.child("fullname").getValue().toString();
                                                    final String requestUserStatus = dataSnapshot.child("status").getValue().toString();
                                                    final String requestUserProfileImage = dataSnapshot.child("profileimage").getValue().toString();




                                                    viewHolder.setFullname(requestUsername);
                                                    viewHolder.setStatus(requestUserStatus);
                                                    viewHolder.setProfileimage(getApplicationContext(),requestUserProfileImage);

                                                    viewHolder.Acceptbtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent profileintent =new Intent(FriendRequestsActivity.this, PersonProfileActivity.class);
                                                            profileintent.putExtra("visit_user_id",list_user_id);
                                                            startActivity(profileintent);
                                                        }
                                                    });

                                                    FriendRequestRef.child(list_user_id)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            CharSequence options[] = new CharSequence[]{
                                                                   requestUsername + "'s Profile",
                                                                    "Send Message"
                                                            };
                                                            AlertDialog.Builder builder =new AlertDialog.Builder(FriendRequestsActivity.this);
                                                            builder.setTitle("Select Option");

                                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    //option view profile
                                                                    if(which ==0){

                                                                        Intent profileintent =new Intent(FriendRequestsActivity.this, PersonProfileActivity.class);
                                                                        profileintent.putExtra("visit_user_id",list_user_id);
                                                                        startActivity(profileintent);

                                                                    }if(which==1){

                                                                        //option chat
                                                                        Intent Chatintent =new Intent(FriendRequestsActivity.this, ChatActivity.class);
                                                                        Chatintent.putExtra("visit_user_id",list_user_id);
                                                                        Chatintent.putExtra("userName",requestUsername);
                                                                        startActivity(Chatintent);
                                                                    }
                                                                }
                                                            });

                                                            builder.show();
                                                        }
                                                    });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else {
                                        viewHolder.Acceptbtn.setVisibility(View.INVISIBLE);
                                       // viewHolder.Declinebtn.setVisibility(View.INVISIBLE);
                                        viewHolder.myStatus.setVisibility(View.INVISIBLE);
                                        viewHolder.myName.setVisibility(View.INVISIBLE);
                                        viewHolder.profileImage.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                };
        FriendRequestsList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FriendRequestsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        TextView myName, myStatus;
        CircleImageView profileImage;

        Button Acceptbtn;


        public FriendRequestsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
            myName=(TextView) mView.findViewById(R.id.friend_requests_fullname);
            myStatus=(TextView) mView.findViewById(R.id.friend_requests_status);
            Acceptbtn = itemView.findViewById(R.id.requests_accept_btn);
            //Declinebtn = itemView.findViewById(R.id.requests_decline_btn);
            profileImage = itemView.findViewById(R.id.friend_requests_imageProfile);

        }

        public void setProfileimage(Context ctx, String profileimage){
            CircleImageView myImage=(CircleImageView) mView.findViewById(R.id.friend_requests_imageProfile);
            Picasso.with(ctx).load(profileimage).placeholder(R.drawable.profile).into(myImage);
        }


        public void setFullname(String fullname){

            TextView myName=(TextView) mView.findViewById(R.id.friend_requests_fullname);
            myName.setText(fullname);
        }

        public void setStatus(String status){

            TextView myStatus=(TextView) mView.findViewById(R.id.friend_requests_status);
            myStatus.setText(status);
        }




    }
}
