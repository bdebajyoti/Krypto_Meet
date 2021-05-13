package com.selfowner.kryptomeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InstantHistory extends AppCompatActivity {
    ImageView imageView;
    ArrayAdapter<String> adapter;
    ArrayList<String> namelist=new ArrayList<>();
    private ListView listView;
    Button loading,clearhistory;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    String UID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instant_history);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        imageView=findViewById(R.id.norecordfound);
        listView=findViewById(R.id.listview);
        clearhistory=findViewById(R.id.clearhistory);
        clearhistory.setEnabled(false);
        loading=findViewById(R.id.loading);
        loading.setEnabled(false);
        adapter=new ArrayAdapter<String>(InstantHistory.this,android.R.layout.simple_list_item_1,namelist);
        listView.setAdapter(adapter);
        LOADME();
        clearhistory.setOnClickListener(v->{
            CLEARHISTORY(v);
        });
    }

    private void CLEARHISTORY(View  v) {
        DatabaseReference dref=FirebaseDatabase.getInstance().getReference("REGISTERED_USER").child(""+UID).child("INSTANT_MEET");
        dref.removeValue();
        listView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        loading.setText("MEETING DATA CLEARED");
        Snackbar snackbar1 = Snackbar.make(v, "Instant Meeting Data Cleared", Snackbar.LENGTH_SHORT);
        snackbar1.show();
    }

    private void LOADME() {
        databaseReference= FirebaseDatabase.getInstance().getReference("REGISTERED_USER").child(""+UID).child("INSTANT_MEET");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String meetFuture="DATE:"+dataSnapshot.child("date").getValue().toString()+"\nMEETID:"+dataSnapshot.child("code").getValue().toString()+"\nROOMNAME:"+dataSnapshot.child("roomName").getValue().toString();
                namelist.add(meetFuture);
                adapter.notifyDataSetChanged();
                loading.setText("Data Fetched Successfully");
                clearhistory.setEnabled(true);
                imageView.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InstantHistory.this,User_Dashboard.class));
        finish();
    }
}