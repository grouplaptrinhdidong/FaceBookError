package facebook.socialnetwork.nhom3.facebook;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
        public ImageView senderMessageImage, receiverMessageImage;

        public MessageViewHolder(View itemView) {
            super(itemView);

            SenderMessageText =(TextView) itemView.findViewById(R.id.sender_message_text);
            ReceiverMessageText =(TextView) itemView.findViewById(R.id.receiver_message_text);
            senderMessageImage = (ImageView) itemView.findViewById(R.id.sender_message_img);
            receiverMessageImage=(ImageView) itemView.findViewById(R.id.receiver_message_img);

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
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position) {

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
        holder.senderMessageImage.setVisibility(View.GONE);
        holder.receiverMessageImage.setVisibility(View.GONE);

        if(fromMessageType.equals("text")){

            //display messages
            if(fromUserID.equals(messageSenderID)){

                holder.SenderMessageText.setVisibility(View.VISIBLE);
                holder.SenderMessageText.setBackgroundResource(R.drawable.sender_message_text_background);
                holder.SenderMessageText.setTextColor(Color.WHITE);
                holder.SenderMessageText.setGravity(Gravity.LEFT);
                holder.senderMessageImage.setVisibility(View.GONE);

                holder.SenderMessageText.setText(messages.getMessage());

            }else {
                holder.SenderMessageText.setVisibility(View.INVISIBLE);

                holder.ReceiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverProfileImage.setVisibility(View.VISIBLE);

                holder.ReceiverMessageText.setBackgroundResource(R.drawable.receiver_message_text_background);
                holder.ReceiverMessageText.setTextColor(Color.BLACK);
                holder.ReceiverMessageText.setGravity(Gravity.LEFT);
                holder.ReceiverMessageText.setText(messages.getMessage());
            }
        }
        else{
            if (fromMessageType.equals("image")){
                if(fromUserID.equals(messageSenderID)){

                    holder.receiverMessageImage.setVisibility(View.VISIBLE);
                    holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                    holder.SenderMessageText.setVisibility(View.INVISIBLE);

                    Picasso.with(holder.receiverMessageImage.getContext()).load(messages.getMessage())
                            .into(holder.receiverMessageImage);

                    holder.receiverMessageImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });

                }else {

                    holder.receiverProfileImage.setVisibility(View.VISIBLE);
                    holder.senderMessageImage.setVisibility(View.VISIBLE);

                    holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                    holder.SenderMessageText.setVisibility(View.INVISIBLE);


                    Picasso.with(holder.senderMessageImage.getContext()).load(messages.getMessage())
                            .into(holder.senderMessageImage);

                    holder.senderMessageImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });
                }
            }
            else {
                if (fromMessageType.equals("docx")){
                    if(fromUserID.equals(messageSenderID)){

                        holder.receiverMessageImage.setVisibility(View.VISIBLE);
                        holder.senderMessageImage.setImageResource(R.drawable.fileword);
                        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                        holder.SenderMessageText.setVisibility(View.INVISIBLE);

                        holder.receiverMessageImage.setImageResource(R.drawable.fileword);

                        holder.receiverMessageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });

                    }else {

                        holder.receiverProfileImage.setVisibility(View.VISIBLE);
                        holder.senderMessageImage.setVisibility(View.VISIBLE);

                        holder.receiverMessageImage.setImageResource(R.drawable.fileword);
                        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                        holder.SenderMessageText.setVisibility(View.INVISIBLE);


                        holder.senderMessageImage.setImageResource(R.drawable.fileword);

                        holder.senderMessageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });
                    }
                }
                else if (fromMessageType.equals("mp3")){
                    if(fromUserID.equals(messageSenderID)){

                        holder.receiverMessageImage.setVisibility(View.VISIBLE);
                        holder.senderMessageImage.setImageResource(R.drawable.filemp3);
                        holder.senderMessageImage.setMaxHeight(1);
                        holder.senderMessageImage.set
                        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                        holder.SenderMessageText.setVisibility(View.INVISIBLE);

                        holder.receiverMessageImage.setImageResource(R.drawable.filemp3);

                        holder.receiverMessageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });

                    }else {

                        holder.receiverProfileImage.setVisibility(View.VISIBLE);
                        holder.senderMessageImage.setVisibility(View.VISIBLE);

                        holder.receiverMessageImage.setImageResource(R.drawable.fileword);
                        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                        holder.SenderMessageText.setVisibility(View.INVISIBLE);


                        holder.senderMessageImage.setImageResource(R.drawable.fileword);

                        holder.senderMessageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });
                    }
                }
                else {
                    if(fromUserID.equals(messageSenderID)){

                        holder.receiverMessageImage.setVisibility(View.VISIBLE);
                        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                        holder.SenderMessageText.setVisibility(View.INVISIBLE);

                        holder.receiverMessageImage.setImageResource(R.drawable.filepdf);
                        holder.senderMessageImage.setImageResource(R.drawable.filepdf);

                        holder.receiverMessageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });

                    }else {

                        holder.receiverProfileImage.setVisibility(View.VISIBLE);
                        holder.senderMessageImage.setVisibility(View.VISIBLE);

                        holder.ReceiverMessageText.setVisibility(View.INVISIBLE);
                        holder.SenderMessageText.setVisibility(View.INVISIBLE);
                        holder.senderMessageImage.setImageResource(R.drawable.filepdf);

                        holder.receiverMessageImage.setImageResource(R.drawable.filepdf);

                        holder.senderMessageImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });
                    }
                }
            }
        }




    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }
}
