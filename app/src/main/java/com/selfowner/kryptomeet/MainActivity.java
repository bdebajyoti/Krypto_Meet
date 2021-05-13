package com.selfowner.kryptomeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class MainActivity extends AppIntro {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance(
                "Welcome To Krypto Connect","Krypto Connect Is The Native Video Call Conferencing App",
                R.drawable.imag3, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryNight)));
        addSlide(AppIntroFragment.newInstance(
                "Hundreds Of Users","Krypto Connect Provides The Feature To Add 100+ Users Free",
                R.drawable.imag1, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryNight)));
        addSlide(AppIntroFragment.newInstance(
                "Widely Usable","Krypto Connect Can Be Used For Teachings.Study,Institution Work,Business Meetings, etc. Please Share This Work",
                R.drawable.imag2, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimaryNight)));
        setFlowAnimation();
        sharedPreferences=getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if(sharedPreferences!=null){
            Boolean checkShare=sharedPreferences.getBoolean("CheckState",false);
            if(checkShare==true){
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        }

    }
    @Override
    public void onSkipPressed(Fragment currentFragment){
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        editor.putBoolean("CheckState",false).commit();
        finish();
    }
    @Override
    public void onDonePressed(Fragment currentFragment){
        super.onDonePressed(currentFragment);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        editor.putBoolean("CheckState",true).commit();
        finish();
    }
}