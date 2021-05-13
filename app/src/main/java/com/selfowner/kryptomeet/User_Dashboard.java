package com.selfowner.kryptomeet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.IntentRequiredException;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User_Dashboard extends AppCompatActivity {
    TextView nav,help;
    static  final int REQUEST_CODE=123;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DrawerLayout navDrawer;
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String UID,Name,GetDate;
    Button createmeet,joinmeet,chatshare;
    private static final String NIGHT_MODE = "night_mode";
    private SharedPreferences mSharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init shared preferences
        mSharedPref = getPreferences(Context.MODE_PRIVATE);
        if (isNightModeEnabled()) {
            setAppTheme(R.style.AppTheme_Base_Night);
        } else {
            setAppTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__dashboard);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        UID = user.getUid();
        CHECKPERMISSION();
        LOADME();
        DATE();
        chatshare=findViewById(R.id.chatsdk);
        chatshare.setOnClickListener(v->{
            CHATSHARE();
        });
        createmeet=findViewById(R.id.createmeet);
        joinmeet=findViewById(R.id.joinmeet);
        joinmeet.setEnabled(false);
        createmeet.setOnClickListener(v->{
            startActivity(new Intent(User_Dashboard.this,CreateMeet.class));
            finish();
        });
        joinmeet.setOnClickListener(v->{
            JOINMEET();
        });
        help=findViewById(R.id.help);
        help.setOnClickListener(v -> {
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.helpview, null);
            //text_entry is an Layout XML file containing two text field to display in alert dialog
            final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
            alert.setView(textEntryView)
                    .setPositiveButton("OK",
                            (dialog, whichButton) -> {
                                Snackbar snackbar1 = Snackbar.make(v, "Thank You:"+Name, Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            });
            alert.setCancelable(false);
            alert.show();
        });
        nav=findViewById(R.id.nav);
        nav.setOnClickListener(v -> {
            navDrawer = findViewById(R.id.drawerLayout);
            // If the navigation drawer is not open then open it, if its already open then close it.
            if(!navDrawer.isDrawerOpen(Gravity.LEFT)) navDrawer.openDrawer(Gravity.LEFT);
            else navDrawer.closeDrawer(Gravity.RIGHT);

        });
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.profile:
                    startActivity(new Intent(User_Dashboard.this, Profile.class));
                    finish();
                    break;
                case R.id.feedback:
                    startActivity(new Intent(User_Dashboard.this, SendMail.class));
                    finish();
                    break;
                case R.id.join_history:
                    startActivity(new Intent(User_Dashboard.this,History.class));
                    finish();
                    break;
                case R.id.instant:
                    startActivity(new Intent(User_Dashboard.this, InstantHistory.class));
                    finish();
                    break;
                case R.id.page_3:
                    startActivity(new Intent(User_Dashboard.this, AboutActivity.class));
                    finish();
                    break;
                case R.id.apptheme:
                    LayoutInflater factory = LayoutInflater.from(this);
                    final View textEntryView = factory.inflate(R.layout.switch_mode, null);
                    final Switch name=textEntryView.findViewById(R.id.item_switch);
                    if (isNightModeEnabled()) {
                        name.setChecked(true);
                    } else {
                        name.setChecked(false);
                    }
                    name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (isNightModeEnabled()) {
                                setIsNightModeEnabled(false);
                                setAppTheme(R.style.AppTheme);
                            } else {
                                setIsNightModeEnabled(true);
                                setAppTheme(R.style.AppTheme_Base_Night);
                            }

                            // Recreate activity
                            recreate();
                        }
                    });
                    final androidx.appcompat.app.AlertDialog.Builder alert1 = new androidx.appcompat.app.AlertDialog.Builder(this);
                    alert1.setView(textEntryView)
                            .setPositiveButton("OK",
                                    (dialog, whichButton) -> {

                                    });

                    alert1.setCancelable(false);
                    alert1.show();
                    break;
                case R.id.page_6:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Use The Link Below To Install The Application\n https://play.google.com/store/apps/details?id=com.selfowner.kryptomeet";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Teacher Referral");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    break;
                case R.id.page_7:
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    break;
                case R.id.page_8:
                    AlertDialog.Builder builder = new AlertDialog.Builder(User_Dashboard.this);
                    builder.setTitle(R.string.app_name);
                    builder.setIcon(R.drawable.imagelogo);
                    builder.setMessage("Are You Sure To Exit From Krypto Connect?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {
                                finish();
                            })
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                case R.id.page_9:
                    firebaseAuth.signOut();
                    finish();
                    break;
            }
            return true;

        });
    }

    private void CHATSHARE() {
        startActivity(new Intent(User_Dashboard.this,ChatWindow.class));
        finish();
    }

    private void JOINMEET() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.join_request, null);
        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final EditText name=textEntryView.findViewById(R.id.name);
        final EditText meetid=textEntryView.findViewById(R.id.meetid);
        name.setEnabled(false);
        name.setText(""+Name);
        final androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setView(textEntryView)
                .setPositiveButton("JOIN",
                        (dialog, whichButton) -> {
                            if(TextUtils.isEmpty(meetid.getText().toString())){
                                Toast.makeText(this, "Please Verify RoomName For Security Purpose", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(meetid.getText().toString().length()==8) {
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER").child("" + UID);
                                    JoinMeet joinMeet = new JoinMeet(GetDate, meetid.getText().toString());
                                    databaseReference.child("JOIN_MEET").push().setValue(joinMeet);
                                    JitsiMeetConferenceOptions options
                                            = new JitsiMeetConferenceOptions.Builder()
                                            .setRoom("" + meetid.getText().toString())
                                            .setFeatureFlag("invite.enabled", false)
                                            .build();
                                    JitsiMeetActivity.launch(this, options);
                                    dialog.dismiss();
                                }
                                else{
                                    Toast.makeText(this, "Invalid Meeting Id", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
        alert.setNegativeButton("CANCEL", (dialog, which) -> {
            Toast.makeText(this, "Thank You Try Later", Toast.LENGTH_SHORT).show();
        });
        alert.setCancelable(false);
        alert.show();
    }

    private void LOADME() {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("REGISTERED_USER");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UID)){
                    Name=dataSnapshot.child(UID).child("name").getValue().toString();
                    joinmeet.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CHECKPERMISSION() {
        if(ContextCompat.checkSelfPermission(User_Dashboard.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                ContextCompat.checkSelfPermission(User_Dashboard.this,
                        Manifest.permission.CAMERA)+
                ContextCompat.checkSelfPermission(User_Dashboard.this,
                        Manifest.permission.RECORD_AUDIO)+
                ContextCompat.checkSelfPermission(User_Dashboard.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(User_Dashboard.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(User_Dashboard.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(User_Dashboard.this,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(User_Dashboard.this,Manifest.permission.RECORD_AUDIO)){
                AlertDialog.Builder builder=new AlertDialog.Builder(User_Dashboard.this);
                builder.setTitle("Please Grant The Permissions");
                builder.setMessage("For Storage,Camera,Microphone");
                builder.setCancelable(false);
                builder.setPositiveButton("Grant", (dialog, which) -> ActivityCompat.requestPermissions(User_Dashboard.this,new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        },
                        REQUEST_CODE
                ));
                builder.setNegativeButton("Denied",null);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
            else{
                ActivityCompat.requestPermissions(User_Dashboard.this,new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        },
                        REQUEST_CODE
                );
            }
        }else{

        }
    }

    private void restartApp() {
        startActivity(new Intent(User_Dashboard.this,User_Dashboard.class));
        finish();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 &&
                    (grantResults[0]+grantResults[1]+grantResults[2]+grantResults[3])==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void DATE(){

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        GetDate =""+currentDate.format(todayDate);

    }
    private void setAppTheme(@StyleRes int style) {
        setTheme(style);
    }
    private boolean isNightModeEnabled() {
        return  mSharedPref.getBoolean(NIGHT_MODE, false);
    }
    private void setIsNightModeEnabled(boolean state) {
        SharedPreferences.Editor mEditor = mSharedPref.edit();
        mEditor.putBoolean(NIGHT_MODE, state);
        mEditor.apply();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(User_Dashboard.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.drawable.imagelogo);
        builder.setMessage("Are You Sure To Exit From Krypto Connect?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->{
                        finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}