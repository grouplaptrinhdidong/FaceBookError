package facebook.socialnetwork.nhom3.facebook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView CommentsList;
    private ImageButton PostCommentButton;
    private EditText CommentInputText;

    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String Post_Key, current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        Post_Key=getIntent().getExtras().get("PostKey").toString();

        mAuth= FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef =FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key).child("Comments");



        CommentsList = (RecyclerView) findViewById(R.id.comments_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);


        CommentInputText = (EditText) findViewById(R.id.comment_input);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_btn);

        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            String userName=dataSnapshot.child("username").getValue().toString();

                            ValidateComment(userName);

                            CommentInputText.setText("");
                            CommentsList.smoothScrollToPosition(CommentsList.getAdapter().getItemCount());

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                UsersRef.child(current_user_id).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        CommentsList.smoothScrollToPosition(CommentsList.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Comments, CommentsViewHolder> fbRecycleAdapter
                = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(
               Comments.class,
                R.layout.all_comments_layout,
                CommentsViewHolder.class,
                PostsRef
        )
        {
            @Override
            protected void populateViewHolder(CommentsViewHolder viewHolder, Comments model, int position) {
                viewHolder.setUser(model.getUsername());
                viewHolder.setComment(model.getComment());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
            }
        };
        CommentsList.setAdapter(fbRecycleAdapter);
    }
    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setUser(String user) {
            TextView myUsername = (TextView) mView.findViewById(R.id.comment_username);
            myUsername.setText("@"+user+"  ");
        }
        public void setComment(String comment) {
            TextView myComment = (TextView) mView.findViewById(R.id.comment_text);
            myComment.setText(comment);
        }
        public void setDate(String date) {
            TextView myDate = (TextView) mView.findViewById(R.id.comment_date);
            myDate.setText("  Date: " + date);
        }
        public void setTime(String time) {
            TextView myTime = (TextView) mView.findViewById(R.id.comment_time);
            myTime.setText("  Time: "+time);
        }

    }
    private void ValidateComment(String userName) {
        String commentText = CommentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText)){
            Toast.makeText(this, "please write text to comment...", Toast.LENGTH_SHORT).show();
        }else {

            Calendar calFordDate=Calendar.getInstance();
            SimpleDateFormat currentDate= new SimpleDateFormat("dd-MM-yyyy");
            final String saveCurrentDate=currentDate.format(calFordDate.getTime());

            Calendar calFordTime=Calendar.getInstance();
            SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss");
            final String saveCurrentTime=currentTime.format(calFordTime.getTime());

            final String RandomKey = saveCurrentDate + saveCurrentTime + current_user_id ;

            HashMap commentsMap= new HashMap();
            commentsMap.put("uid", current_user_id);
            commentsMap.put("comment", commentText);
            commentsMap.put("date", saveCurrentDate);
            commentsMap.put("time", saveCurrentTime);
            commentsMap.put("username", userName);

            PostsRef.child(RandomKey).updateChildren(commentsMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if(task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this, "you have commented successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(CommentsActivity.this, "Error Occured, try again...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
