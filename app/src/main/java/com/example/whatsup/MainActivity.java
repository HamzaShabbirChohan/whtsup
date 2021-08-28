package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsup.Adapter.FragmentAdapter;
import com.example.whatsup.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.whatsup.R.menu.menu;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        binding.viewpager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tablayout.setupWithViewPager(binding.viewpager);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setting:
                Intent intent1=new Intent(MainActivity.this,Sattingactivity.class);
                startActivity(intent1);
            break;
            case R.id.logout:
                auth.signOut();
                Intent intent=new Intent(MainActivity.
                        this,SignIn.class);
                startActivity(intent);
                finish();
                break;
            case R.id.groupchat:
                Intent intentt=new Intent(MainActivity.
                        this,Groupchatactivity.class);
                startActivity(intentt);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
;