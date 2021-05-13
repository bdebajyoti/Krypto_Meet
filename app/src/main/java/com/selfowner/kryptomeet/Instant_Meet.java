package com.selfowner.kryptomeet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Instant_Meet extends AppCompatActivity {
    EditText name,meetid,date,roomname,password;
    private String GetDate;
    private String CODE="";
    private String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
    private String SUBJECT="Online Room";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String UID,Name,Rname,Password;
    Button share,stmeet,loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instant_meet);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        DATE();
        LOADME();
        name=findViewById(R.id.name);
        meetid=findViewById(R.id.meetid);
        date=findViewById(R.id.date);
        roomname=findViewById(R.id.meetsubject);
        password=findViewById(R.id.password);
        share=findViewById(R.id.share);
        loading=findViewById(R.id.loading);
        loading.setEnabled(false);
        name.setEnabled(false);
        date.setEnabled(false);
        meetid.setEnabled(false);
        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");

        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        stmeet=findViewById(R.id.stmeet);
        stmeet.setEnabled(false);
        stmeet.setOnClickListener(v->{
            LAUNCHMEET();
        });
        share.setOnClickListener(v->{
            SHAREMEET();
        });
    }

    private void LAUNCHMEET() {
        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER").child(""+UID);
        MeetInstant meetInstant =new MeetInstant(GetDate,CODE,SUBJECT);
        databaseReference.child("INSTANT_MEET").push().setValue(meetInstant);
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(""+CODE)
                .setSubject(""+SUBJECT)
                .setFeatureFlag("invite.enabled",false)
                .build();
        JitsiMeetActivity.launch(this, options);
    }

    private void SHAREMEET() {
        Rname=roomname.getText().toString().toUpperCase();
        Password=password.getText().toString();
        if(TextUtils.isEmpty(Rname)){
            Rname=""+SUBJECT;
        }
        name.setText("");
        date.setText("");
        roomname.setText("");
        meetid.setText("");
        password.setText("");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "RoomName:"+Rname+"\n\nJoin Code:"+CODE+"\n\nPassCode"+Password+"\n\nThe Application Download Link\n\n https://play.google.com/store/apps/details?id=com.selfowner.kryptomeet";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Meeting Referral");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void LOADME() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){

                    Name=dataSnapshot.child(UID).child("name").getValue().toString();
                    name.setText(""+Name);
                    CODE=""+GENSTR();
                    meetid.setText(""+CODE);
                    date.setText(""+GetDate);
                    loading.setText("Resources Fetched Successfully");
                    stmeet.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private String GENSTR() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString().toUpperCase();
    }
    private void DATE(){

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        GetDate =""+currentDate.format(todayDate);

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Instant_Meet.this,CreateMeet.class));
        finish();
    }
}
