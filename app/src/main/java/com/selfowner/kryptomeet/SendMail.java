package com.selfowner.kryptomeet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SendMail extends AppCompatActivity {
    EditText email,subject,message;
    Button send;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    String EMAIL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_form);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        EMAIL= user.getEmail().toLowerCase();
        email=findViewById(R.id.email);
        subject=findViewById(R.id.subject);
        message=findViewById(R.id.messagetxt);
        send=findViewById(R.id.send);
        email.setText(""+EMAIL);
        send.setOnClickListener(v->{
            if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(subject.getText().toString()) || TextUtils.isEmpty(message.getText().toString())){
                Snackbar snackbar1 = Snackbar.make(v, "Please Provide All Information", Snackbar.LENGTH_SHORT);
                snackbar1.show();
            }
            else {
                Intent i = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:kryptoconnect2020@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                i.putExtra(Intent.EXTRA_TEXT, (message.getText().toString() + "\n" + email.getText().toString()));
                startActivity(i);
                email.setText("");
                subject.setText("");
                message.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SendMail.this,User_Dashboard.class));
        finish();
    }
}
