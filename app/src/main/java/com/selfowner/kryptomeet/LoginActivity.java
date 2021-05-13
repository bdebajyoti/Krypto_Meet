package com.selfowner.kryptomeet;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView signup;
    EditText email,password;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressBar;
    private String Email,Password;
    private int RC_SIGN_IN=1;
    DatabaseReference firebaseDatabase;
    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,User_Dashboard.class));
            finish();
        }
       /* google=findViewById(R.id.google);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        google.setOnClickListener(v -> google());
        googleSignInClient= GoogleSignIn.getClient(this,gso);*/
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        progressBar = new ProgressDialog(this);
        signup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,Registration_Page.class));
            finish();
        });
        login.setOnClickListener(v -> LOGIN(v));
    }
/*private void google(){
    Intent signInIntent=googleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent,RC_SIGN_IN);
}*/

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }*/

  /*  private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication Error Please Try Again", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                });
    }

    private void updateUI(FirebaseUser user) {
        UID=user.getUid();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
            firebaseDatabase.child(UID).child("name").setValue(personName);
            firebaseDatabase.child(UID).child("givenName").setValue(personGivenName);
            firebaseDatabase.child(UID).child("email").setValue(personEmail);
            firebaseDatabase.child(UID).child("personId").setValue(personId);
            firebaseDatabase.child(UID).child("password").setValue("");
            Toast.makeText(this, "Hello "+personName, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,User_Dashboard.class));
            finish();
        }
    }*/

    private void LOGIN(View v){
        Email=email.getText().toString();
        Password=password.getText().toString();
        if(TextUtils.isEmpty(Email)){
            Snackbar snackbar1 = Snackbar.make(v, "Please Provide Your Email", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            Snackbar snackbar1 = Snackbar.make(v, "Please Provide Your Password", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return;
        }
        progressBar.setMessage("Logging In Please Wait...");
        progressBar.show();
        firebaseAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Snackbar snackbar1 = Snackbar.make(v, "Successfully Logged In", Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        progressBar.dismiss();
                        startActivity(new Intent(LoginActivity.this,User_Dashboard.class));
                        finish();
                    }
                    else {
                        Snackbar snackbar1 = Snackbar.make(v, "Server Error"+task.getException(), Snackbar.LENGTH_SHORT);
                        snackbar1.show();
                        progressBar.dismiss();
                    }
                });
    }
}