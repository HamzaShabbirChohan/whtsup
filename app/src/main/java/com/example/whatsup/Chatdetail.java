package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.whatsup.Adapter.MessageAdapter;
import com.example.whatsup.Model.User;
import com.example.whatsup.Model.messsagemodel;
import com.example.whatsup.databinding.ActivityChatdetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class Chatdetail extends AppCompatActivity {
    ActivityChatdetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding=ActivityChatdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
       final String senderid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String receiveid=getIntent().getStringExtra("userid");
        String Username=getIntent().getStringExtra("username");
        String profilepic=getIntent().getStringExtra("userprofilepic");
        Log.d("username",receiveid);
        binding.username.setText(Username);
        Picasso.get().load(profilepic).placeholder(R.drawable.pic1).into(binding.image);
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Chatdetail.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<messsagemodel> messsagemodel=new ArrayList<>();
        final MessageAdapter messageAdapter=new MessageAdapter (messsagemodel, this,receiveid);
        binding.chatrecyclerdetail.setAdapter(messageAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatrecyclerdetail.setLayoutManager(layoutManager);
        final String senderroom=senderid+receiveid;
        final  String receiverroom=receiveid+senderid;
        database.getReference().child("chats").child(senderroom).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messsagemodel.clear();

                        for (DataSnapshot snapshort1:snapshot.getChildren())
                        {

                            messsagemodel model =snapshort1.getValue(messsagemodel.class);
                            model.setMessageid(snapshort1.getKey());
                            if(!messsagemodel.contains(model)) {
                                messsagemodel.add(model);
                            }

                            messageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        binding.send.setOnClickListener(new View.OnClickListener() {
            //Log.d("chats","send");
            @Override
            public void onClick(View view) {
                if(binding.editmessage.getText().toString().isEmpty())
                {
                    binding.editmessage.setError("Ente you Message");
                    return;
                }

                String message=binding.editmessage.getText().toString();
                final messsagemodel model=new messsagemodel(senderid,message);
                model.setTimestamp(new Date().getTime());
                binding.editmessage.setText("");
                Log.d("chats",senderid);
                database.getReference().child("chats").child(senderroom).push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats").child(receiverroom).push().
                          setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })  ;

                    }
                });

            }
        });
        
    }
}