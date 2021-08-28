package com.example.whatsup.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsup.Chatdetail;
import com.example.whatsup.Model.User;
import com.example.whatsup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.whatsup.R.id.image;

public class Fragmentuser extends RecyclerView.Adapter<Fragmentuser.ViewHolder> {


    ArrayList<User> list;
    Context context;

    public Fragmentuser(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_user,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       User user=list.get(position);
        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.pic1).into(holder.imageView);
        holder.username.setText(user.getUsername());
        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getUid()+user.getUserid()).
                orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for (DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        holder.lastmsg.setText(snapshot1.child("message").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, Chatdetail.class);

                intent.putExtra("userid",user.getUserid());
                intent.putExtra("username",user.getUsername());
                intent.putExtra("userprofilepic",user.getProfilepic());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView username;
        TextView lastmsg;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(image);
            username=itemView.findViewById(R.id.txt1);
            lastmsg=itemView.findViewById(R.id.txt2);

        }
    }
}
