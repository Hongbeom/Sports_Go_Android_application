package com.example.mcbback.sportgo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MatchInfo extends AppCompatActivity {

    private FirebaseUser user;
    private TextView notieventname;
    private TextView notidate;
    private TextView notitime;
    private TextView notiplace;
    private TextView notiaddress;
    private TextView comment;
    private Button noti_back;
    private Button noti_map;
    private Profile userProfile;
    private Event event;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(MatchInfo.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_notify);
        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        event = (Event) intent.getSerializableExtra("userEvent");
        if(((AlarmCondition) intent.getSerializableExtra("cond")) != null &&
                ((AlarmCondition) intent.getSerializableExtra("cond")).condition == 1){
            stopService(new Intent(getApplicationContext(), MatchPlanService.class));
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();

        notieventname = (TextView) findViewById(R.id.mnotify_name);
        notidate = (TextView) findViewById(R.id.mnotify_date);
        notitime = (TextView) findViewById(R.id.mnotify_time);
        notiaddress = (TextView) findViewById(R.id.mnotify_address);
        comment = (TextView) findViewById(R.id.mnotify_place);

        noti_back = (Button) findViewById(R.id.mnotify_back);
        noti_map = (Button) findViewById(R.id.mnofity_map);


        notieventname.setText(event.eventName);
        notidate.setText(event.matchPlan.date);
        notitime.setText(event.matchPlan.time);
        comment.setText(event.matchPlan.comment);
        notiaddress.setText(event.matchPlan.address);

        noti_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        noti_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Showmap.class);
                intent.putExtra("place_address", event.matchPlan.address);
                startActivity(intent);
            }
        });







    }


}
