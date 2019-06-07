package facebook.socialnetwork.nhom3.facebook;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDatabaseRef;

    public MessagesAdapter(List<Messages> userMessagesList){

        this.userMessagesList = userMessagesList;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView SenderMessageText, ReceiverMessageText;
        public CircleImageView receiverProfileImage;
        public ImageView messageImg;

        public MessageViewHolder(View itemView) {
            super(itemView);

            SenderMessageText =(TextView) itemView.findViewById(R.id.sender_message_text);
            ReceiverMessageText =(TextView) itemView.findViewById(R.id.receiver_message_text);
            messageImg = (ImageView) itemView.findViewById(R.id.message_img);
            receiverProfileImage= (CircleImageView) itemView.findViewById(R.id.message_profile_image);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View V= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_layout_of_users, parent, false);

        mAuth =FirebaseAuth.getInstance();

        return new MessageViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {

        String messageSenderID = mAuth.getCurrentUser().getUid();
        Messages messages =userMessagesList.get(position);

        String fromUserID = messages.getFrom() ;
        String fromMessageType = messages.getType();

        usersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        usersDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String image = dataSnapshot.child("profileimage").getValue().toString();

                    Picasso.with(holder.receiverProfileImage.getContext()).load(image)
                            .placeholder(R.drawable.profile).into(holder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
        holder.SenderMessageText.setVisibility(View.INVISIBLE);
        holder.receiverProfileImage.setVisibility(View.INVISIBLE);
        holder.messageImg.setVisibility(View.GONE);

        if(fromMessageType.equals("text")){

            //display messages
            if(fromUserID.equals(messageSenderID)){

                holder.SenderMessageText.setVisibility(View.VISIBLE);
                holder.SenderMessageText.setBackgroundResource(R.drawable.sender_message_text_background);
                holder.SenderMessageText.setTextColor(Color.WHITE);
                holder.SenderMessageText.setGravity(Gravity.LEFT);
                holder.messageImg.setVisibility(View.GONE);
                //holder.messageImg.setMaxHeight(0);
                holder.SenderMessageText.setText(messages.getMessage());

            }else {
                holder.SenderMessageText.setVisibility(View.INVISIBLE);
                //holder.messageImg.setVisibility(View.GONE);
                holder.ReceiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverProfileImage.setVisibility(View.VISIBLE);

                holder.ReceiverMessageText.setBackgroundResource(R.drawable.receiver_message_text_background);
                holder.ReceiverMessageText.setTextColor(Color.BLACK);
                holder.ReceiverMessageText.setGravity(Gravity.LEFT);
                holder.ReceiverMessageText.setText(messages.getMessage());
            }
        }
        else{
                holder.receiverProfileImage.setVisibility(View.VISIBLE);
                holder.messageImg.setVisibility(View.VISIBLE);
                holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                holder.SenderMessageText.setVisibility(View.INVISIBLE);
               /* holder.SenderMessageText.setPadding(0,0,0,0);
                holder.ReceiverMessageText.setPadding(0,0,0,0);*/

            Picasso.with(holder.receiverProfileImage.getContext()).load(messages.getMessage())
                    .placeholder(R.drawable.profile).into(holder.messageImg);
           /* Picasso.with(holder.messageImg.getContext()).load(messages.getMessage())
                    .into(holder.messageImg);*/
        }

       /* holder.messageText.setVisibility(View.GONE);
        holder.messagePicture.setVisibility(View.GONE);
        if("text".equals(fromMessageType)){
            holder.messageText.setVisibility(View.VISIBLE);
            if(Objects.equals(fromUserId, messageSenderId)){
                holder.messageText.setBackgroundResource(R.drawable.message_text_background_two);
                holder.messageText.setTextColor(Color.BLACK);
                holder.messageText.setGravity(Gravity.RIGHT);
            }
            else{
                holder.messageText.setBackgroundResource(R.drawable.message_text_background);
                holder.messageText.setTextColor(Color.WHITE);
                holder.messageText.setGravity(Gravity.LEFT);
            }
            holder.messageText.setText(messages.getMessage());
        }else{
            holder.messagePicture.setVisibility(View.VISIBLE);
            Picasso.with(holder.messagePicture.getContext()).load(messages.getMessage())
                    .into(holder.messagePicture);
        }*/


    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }
}
