package com.selfowner.kryptomeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration_Page extends AppCompatActivity {
    EditText name,contact,email,password;
    Button create;
    TextView login;
    FirebaseAuth firebaseAuth;
    DatabaseReference firebaseDatabase;
    String Name,Contact,Email,Password,UID;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__page);
        firebaseAuth=FirebaseAuth.getInstance();
        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        create=findViewById(R.id.create);
        login=findViewById(R.id.login);
        login.setOnClickListener(v-> {
            startActivity(new Intent(Registration_Page.this, LoginActivity.class));
            finish();
        });
        progressDialog = new ProgressDialog(this);
        create.setOnClickListener(v -> SETDATA(v));
    }

    private void SETDATA(View v) {
        Name=name.getText().toString();
        Email=email.getText().toString().trim();
        Contact=contact.getText().toString();
        Password=password.getText().toString();

        if(TextUtils.isEmpty(Name)){
            Snackbar snackbar1 = Snackbar.make(v, "Please Provide Your Name", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return;
        }
        if(TextUtils.isEmpty(Email)){
            Snackbar snackbar1 = Snackbar.make(v, "Please Provide Your Email", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return;
        }
        if(TextUtils.isEmpty(Contact)){
            Snackbar snackbar1 = Snackbar.make(v, "Please Provide Your Contact Detail", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return;
        }
        if(TextUtils.isEmpty(Password) || Password.length()<8){
            Snackbar snackbar1 = Snackbar.make(v, "Please Provide Your Valid Password", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return;
        }

        CREATEACCOUNT(Name,Contact,Email,Password,v);
    }

    private void CREATEACCOUNT(String Name,String Contact,String Email,String Password,View v){
        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(Registration_Page.this, task -> {
                    if(task.isSuccessful()){
                        UID=firebaseAuth.getCurrentUser().getUid().toString();
                        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER").child(""+UID);
                        RegisterHelperClass registerHelperClass=new RegisterHelperClass(Name,Email,Contact,Password);
                        firebaseDatabase.setValue(registerHelperClass);
                        Snackbar snackbar1 = Snackbar.make(v, "Registration Completed", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        startActivity(new Intent(Registration_Page.this,LoginActivity.class));
                        finish();
                    }
                    if (!task.isSuccessful()) {
                        Snackbar snackbar1 = Snackbar.make(v, "Registration Failed", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                    }
                    progressDialog.dismiss();
                });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Registration_Page.this, LoginActivity.class));
        finish();
    }
}