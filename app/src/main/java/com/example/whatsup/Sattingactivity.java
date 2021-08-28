package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsup.Model.User;
import com.example.whatsup.databinding.ActivitySattingactivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Sattingactivity extends AppCompatActivity {
    ActivitySattingactivityBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySattingactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        binding.backarrowsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Sattingactivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
         binding.savebutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String username=binding.setusername.getText().toString();
                 String status=binding.setstatus.getText().toString();
                 HashMap<String,Object>obj=new HashMap<>();
                 obj.put("username",username);
                 obj.put("about",status);
                 database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).
                         updateChildren(obj);
             }
         });
        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                Log.d("`Usermm`",user.getProfilepic());
                Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.pic1).into(binding.image);
                binding.setusername.setText(user.getUsername());
                binding.setstatus.setText(user.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,33);
            }
        });;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null)
        {
           Uri sfile=data.getData();
           final StorageReference reference=storage.getReference().child("Profile_pictures").child(FirebaseAuth.getInstance().getUid());
           reference.putFile(sfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                   reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {

                           Log.d("Userj",uri.toString());
                           database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("profilepic").
                                   setValue(uri.toString());
                           Toast.makeText(Sattingactivity.this,"picture upload succedded",Toast.LENGTH_SHORT).show();
                           Picasso.get().load(uri.toString()).into(binding.image);
                       }
                   });
               }
           });

        }
    }
}