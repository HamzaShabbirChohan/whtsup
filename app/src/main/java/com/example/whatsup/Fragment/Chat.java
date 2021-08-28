package com.example.whatsup.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsup.Adapter.Fragmentuser;
import com.example.whatsup.Model.User;
import com.example.whatsup.R;
import com.example.whatsup.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class Chat extends Fragment {

    public Chat() {
        // Required empty public constructor
    }
    FragmentChatBinding binding;
    ArrayList<User>  list=new ArrayList<>();
    FirebaseDatabase database;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater,container,false);
        database=FirebaseDatabase.getInstance();
        Fragmentuser fragmentuser=new Fragmentuser(list,getContext());
          binding.chatrecycler.setAdapter(fragmentuser);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext());
        binding.chatrecycler .setLayoutManager(layoutManager);
        userId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
              for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                  User user=dataSnapshot.getValue(User.class);
                     user.setUserid(dataSnapshot.getKey());
                     if(!user.getUserid().equals(userId))
                  {
                      list.add(user);
                      Log.d("userids",user.getUserid()+ "=>" +FirebaseAuth.getInstance().getUid());

                  }
                //  list.remove(userId);

              }
              fragmentuser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


 return binding.getRoot();
    }
}