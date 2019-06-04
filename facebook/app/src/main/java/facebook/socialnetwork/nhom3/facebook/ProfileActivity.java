package facebook.socialnetwork.nhom3.facebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView username, userProfileName, userStatus, userCountry, userGender, userRelationship, userDob;
    private CircleImageView userProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username = (TextView) findViewById(R.id.my_username);
        userStatus = (TextView) findViewById(R.id.my_profile_status);
        userProfileName = (TextView) findViewById(R.id.my_profile_full_name);
        userCountry = (TextView) findViewById(R.id.my_profile_country);
        userGender = (TextView) findViewById(R.id.my_gender);
        userRelationship = (TextView) findViewById(R.id.my_relation);
        userDob = (TextView) findViewById(R.id.my_dob);

        userProfileImage = (CircleImageView) findViewById(R.id.my_profile_pic);
    }
}
