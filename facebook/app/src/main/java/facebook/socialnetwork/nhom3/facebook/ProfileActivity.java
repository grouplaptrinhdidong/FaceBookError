package facebook.socialnetwork.nhom3.facebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView username, userProfileName, userStatus, userCountry, userGender, userRelationship, userDob;
    private CircleImageView userProfileImage;

    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        profileUserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);




        username = (TextView) findViewById(R.id.my_username);
        userStatus = (TextView) findViewById(R.id.my_profile_status);
        userProfileName = (TextView) findViewById(R.id.my_profile_full_name);
        userCountry = (TextView) findViewById(R.id.my_profile_country);
        userGender = (TextView) findViewById(R.id.my_gender);
        userRelationship = (TextView) findViewById(R.id.my_relation);
        userDob = (TextView) findViewById(R.id.my_dob);

        userProfileImage = (CircleImageView) findViewById(R.id.my_profile_pic);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                    String myDob = dataSnapshot.child("dob").getValue().toString();
                    String myCountry = dataSnapshot.child("country").getValue().toString();
                    String myGender = dataSnapshot.child("gender").getValue().toString();
                    String myRelationshipStatus = dataSnapshot.child("relationshipstatus").getValue().toString();


                    Picasso.with(ProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    username.setText("@" + myUserName);
                    userProfileName.setText(myProfileName);
                    userStatus.setText(myProfileStatus);
                    userDob.setText("DOB: " + myDob);
                    userCountry.setText("Country: " + myCountry);
                    userGender.setText("Gender: " + myGender);
                    userRelationship.setText("Relationship: " + myRelationshipStatus);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
