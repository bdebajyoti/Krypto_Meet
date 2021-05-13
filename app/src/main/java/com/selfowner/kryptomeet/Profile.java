package com.selfowner.kryptomeet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView name,email,contact,id;
    String Name,Email,Contact,ID;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button update,delete;
    DatabaseReference databaseReference;
    String UID,PASSWORD="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_section);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        UID=user.getUid();
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        update=findViewById(R.id.pass);
        delete=findViewById(R.id.delete);
        LOADME();
        update.setOnClickListener(v->{
            UpdateMe(v);
        });
        delete.setOnClickListener(v->{
            DeleteMe(v);
        });
    }

    private void DeleteMe(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.imagelogo);
        builder.setMessage("Are You Sure To Delete Your Account From Krypto Connect?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential  authCredential= EmailAuthProvider.getCredential(Email,PASSWORD);
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(task -> firebaseUser.delete().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {

                            DatabaseReference dref=FirebaseDatabase.getInstance().getReference("REGISTERED_USER").child(""+UID);
                            dref.removeValue();
                            Snackbar snackbar1 = Snackbar.make(v, "Your Account Is Deleted", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                            startActivity(new Intent(Profile.this,LoginActivity.class));
                            finish();
                        }
                    }));
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void UpdateMe(View v) {
        LayoutInflater factory1 = LayoutInflater.from(this);
        final View textEntryView1 = factory1.inflate(R.layout.password_reset, null);
        final EditText email1=textEntryView1.findViewById(R.id.pass_reset);
        final EditText passcode=textEntryView1.findViewById(R.id.passcode);
        final Button sendmail=textEntryView1.findViewById(R.id.sendmail);

        sendmail.setOnClickListener(v1 -> {
            databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
            databaseReference.child(UID).child("password").setValue(""+passcode.getText().toString());
            if(email1.getText().toString().equals(Email)){
                firebaseAuth.sendPasswordResetEmail(email1.getText().toString())
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                email1.setText("");
                                passcode.setText("");
                                Snackbar snackbar1 = Snackbar.make(v1, "Password Confirmation Mail has Been Sent", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                            else{
                                Snackbar snackbar1 = Snackbar.make(v1, "Server Error Or Password Is Not Matched", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });
            }
            if(!email1.getText().toString().equals(Email)){
                Snackbar snackbar1 = Snackbar.make(v1, "Please Provide Valid Email Id", Snackbar.LENGTH_LONG);
                snackbar1.show();
            }

        });
        final AlertDialog.Builder alert1 = new AlertDialog.Builder(this);
        alert1.setView(textEntryView1)
                .setPositiveButton("CLOSE",
                        (dialog, whichButton) -> {
                        });
        alert1.setCancelable(false);
        alert1.show();
    }

    private void LOADME() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){

                        Name=dataSnapshot.child(UID).child("name").getValue().toString();
                        Email=dataSnapshot.child(UID).child("email").getValue().toString();
                        PASSWORD=dataSnapshot.child(UID).child("email").getValue().toString();
                        name.setText(""+Name);
                        email.setText(""+Email);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Profile.this,User_Dashboard.class));
        finish();
    }
}
