package com.selfowner.kryptomeet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CreateMeet extends AppCompatActivity {
    CardView instant,future,pickdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meet);
        instant=findViewById(R.id.instant);
        instant.setOnClickListener(v -> {
            SetInstantMeet();
        });
        future=findViewById(R.id.future);
        future.setOnClickListener(v->{
            SetFutureMeet();
        });
        pickdate=findViewById(R.id.pickdate);
        pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelMeeting();
            }
        });
    }

    private void SelMeeting() {
        startActivity(new Intent(CreateMeet.this,DatePickerClass.class));
        finish();
    }

    private void SetFutureMeet() {
        startActivity(new Intent(CreateMeet.this,Future_Meet.class));
        finish();
    }

    private void SetInstantMeet() {
        startActivity(new Intent(CreateMeet.this,Instant_Meet.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreateMeet.this,User_Dashboard.class));
        finish();
    }
}
