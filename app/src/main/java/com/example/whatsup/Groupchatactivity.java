package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsup.Adapter.MessageAdapter;
import com.example.whatsup.Model.messsagemodel;
import com.example.whatsup.databinding.ActivityChatdetailBinding;
import com.example.whatsup.databinding.ActivityGroupchatactivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Groupchatactivity extends AppCompatActivity {

    ActivityGroupchatactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityGroupchatactivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        //FirebaseDatabase database=FirebaseDatabase.getInstance();
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Groupchatactivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String senderid = FirebaseAuth.getInstance().getUid();
        binding.username.setText("Group Chats");
   
        final ArrayList<messsagemodel> messsagemodels = new ArrayList<>();
        final MessageAdapter adapter = new MessageAdapter(messsagemodels, this);
        binding.grouochatrecyclerdetail.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.grouochatrecyclerdetail.setLayoutManager(layoutManager);
        database.getReference().child("GroupChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messsagemodels.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    messsagemodel model=dataSnapshot.getValue(messsagemodel .class);
                    messsagemodels.add(model);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         binding.send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(binding.editmessage.getText().toString().isEmpty())
                 {
                     binding.editmessage.setError("Ente you Message");
                     return;
                 }
                 final String message=binding.editmessage.getText().toString();
                 final messsagemodel model= new messsagemodel(senderid,message);
                 model.setTimestamp(new Date().getTime());
                binding.editmessage.setText("");
                database.getReference().child("GroupChat").push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
             }
         });



    }
}