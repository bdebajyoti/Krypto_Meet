package com.selfowner.kryptomeet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Future_Meet extends AppCompatActivity {
    EditText name,password,meetid,roomname;
    TextView  getdate,gettime;
    String Getdate,Gettime,Password,CODE,Rname,UID,Name;
    private String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
    private String SUBJECT="Online Room";
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Button save,loading,share;
    ImageButton stdate,sttime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.future_meet);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        LOADME();
        name=findViewById(R.id.name);
        getdate=findViewById(R.id.getdate);
        gettime=findViewById(R.id.gettime);
        stdate=findViewById(R.id.stdate);
        stdate.setOnClickListener(v->{
            handleDateButton();
        });
        sttime=findViewById(R.id.sttime);
        sttime.setOnClickListener(v->{
            handleTimeButton();
        });
        password=findViewById(R.id.password);
        meetid=findViewById(R.id.meetid);
        roomname=findViewById(R.id.meetsubject);
        save=findViewById(R.id.save);
        save.setEnabled(false);
        save.setEnabled(false);
        loading=findViewById(R.id.loading);
        loading.setEnabled(false);
        share=findViewById(R.id.share);
        share.setEnabled(false);
        share.setOnClickListener(v->{
            name.setText("");
            meetid.setText("");
            roomname.setText("");
            gettime.setText("Set Time");
            getdate.setText("Set Date");
            password.setText("");
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "RoomName:"+Rname+"\n\nJoin Code:"+CODE+"\n\nPassCode:"+Password+"\n\nDate:"+Getdate+"\n\nTime:"+Gettime+"\n\n\nThe Application Download Link\n\n https://play.google.com/store/apps/details?id=com.selfowner.kryptomeet";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Meeting Referral");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });
        name.setEnabled(false);
        meetid.setEnabled(false);
        save.setOnClickListener(v->{
            SAVEMEET(v);
        });
    }

    private void SAVEMEET(View v) {
        Rname=roomname.getText().toString();
        Password=password.getText().toString();
        Gettime=gettime.getText().toString();
        Getdate=getdate.getText().toString();
        if(Gettime.equals("Set Time")){
            Toast.makeText(this, "Please Provide Valid Meeting Time", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Getdate.equals("Set Date")){
            Toast.makeText(this, "Please Provide Valid Meeting Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Rname)){
            Rname=""+SUBJECT;
        }

        databaseReference=FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER").child(""+UID);
        MeetFuture meetFuture=new MeetFuture(CODE,getdate.getText().toString(),gettime.getText().toString(),Rname,Password);
        databaseReference.child("FUTURE_MEET").push().setValue(meetFuture);
        Snackbar snackbar1 = Snackbar.make(v, "Meet Information Saved Please Share The Information", Snackbar.LENGTH_SHORT);
        snackbar1.show();
        save.setEnabled(false);
        share.setEnabled(true);
    }

    private void handleTimeButton() {

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hour, minute) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR, hour);
            calendar1.set(Calendar.MINUTE, minute);
            CharSequence dateText = DateFormat.format("hh:mm a", calendar1).toString();
            gettime.setText(dateText);
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }

    private void handleDateButton() {

            Calendar calendar = Calendar.getInstance();
            int YEAR = calendar.get(Calendar.YEAR);
            int MONTH = calendar.get(Calendar.MONTH);
            int DATE = calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, date) -> {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                CharSequence charSequence= android.text.format.DateFormat.format("EEEE, dd MMM yyyy", calendar1).toString();
                getdate.setText(charSequence);
            }, YEAR, MONTH, DATE);
            datePickerDialog.show();
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
                    loading.setText("Resources Fetched Successfully");
                    save.setEnabled(true);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Future_Meet.this,CreateMeet.class));
        finish();
    }
}
