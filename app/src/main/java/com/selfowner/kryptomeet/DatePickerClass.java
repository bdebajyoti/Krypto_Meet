package com.selfowner.kryptomeet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
import java.util.ArrayList;
import java.util.List;

public class DatePickerClass extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ArrayList<String> namelist=new ArrayList<>();
    private ListView listView;
    private TextView loading;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String UID,CODE;
    private String Temp;
    private Button launch;
    //meeting
    private String Name,Roomname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_activity);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        LOADME();
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
        launch=findViewById(R.id.launch);
        launch.setEnabled(false);
        listView=findViewById(R.id.listview);
        loading=findViewById(R.id.loading);
        adapter=new ArrayAdapter<String>(DatePickerClass.this,android.R.layout.simple_list_item_1,namelist);
        listView.setAdapter(adapter);
        LOADDATA();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Temp=listView.getItemAtPosition(position).toString();
            launch.setEnabled(true);
        });
        launch.setOnClickListener(v->{
            EXTRACTSTRING(Temp);
        });
    }
    private void EXTRACTSTRING(String temp) {
        int i=0;
        int k=0;
        int pos=0;
        for(i=0;i<temp.length();i++){
            if(temp.charAt(i)==':'){
                if(k==3){
                    pos=i;
                    break;
                }
                k++;
            }
        }

        CODE=""+GENSTR(pos,Temp);
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.confirm_page, null);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final EditText name=textEntryView.findViewById(R.id.name);
        final EditText meetid=textEntryView.findViewById(R.id.meetid);
        final EditText rname=textEntryView.findViewById(R.id.meetsubject);
        name.setEnabled(false);
        meetid.setEnabled(false);
        name.setText(""+Name);
        meetid.setText(""+CODE);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(textEntryView)
                .setPositiveButton("OK",
                        (dialog, whichButton) -> {
                            if(TextUtils.isEmpty(rname.getText().toString())){
                                Toast.makeText(this, "Please Verify RoomName For Security Purpose", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                JitsiMeetConferenceOptions options
                                        = new JitsiMeetConferenceOptions.Builder()
                                        .setRoom(""+CODE)
                                        .setSubject(""+rname.getText().toString())
                                        .setFeatureFlag("invite.enabled",false)
                                        .build();
                                JitsiMeetActivity.launch(this, options);
                                dialog.dismiss();
                            }

                        });
        alert.setNegativeButton("CANCEL", (dialog, which) -> {
            Toast.makeText(this, "Thank You Try Later", Toast.LENGTH_SHORT).show();
        });
        alert.setCancelable(false);
        alert.show();
    }
    private String GENSTR(int position,String temp) {
        position=position+1;
        StringBuilder sb = new StringBuilder(8);
        for (int i = position; i < position+8; i++) {

            sb.append(temp
                    .charAt(i));
        }
        return sb.toString().toUpperCase();
    }

    private void LOADDATA() {
        databaseReference= FirebaseDatabase.getInstance().getReference("REGISTERED_USER").child(""+UID).child("FUTURE_MEET");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String meetFuture="DATE:"+dataSnapshot.child("date").getValue().toString()+"\nTIME:"+dataSnapshot.child("time").getValue().toString()+"\nCODE:"+dataSnapshot.child("code").getValue().toString()+"\nROOMNAME:"+dataSnapshot.child("roomName").getValue().toString()+"\nPASSWORD:"+dataSnapshot.child("password").getValue().toString();
                namelist.add(meetFuture);
                adapter.notifyDataSetChanged();
                loading.setText("Data Fetched Successfully");
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
private void LOADME(){
    databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChild(UID)){
                Name=dataSnapshot.child(UID).child("name").getValue().toString();
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DatePickerClass.this,CreateMeet.class));
        finish();
    }
}
