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

public class EventInfo extends AppCompatActivity {

    private FirebaseUser user;
    private TextView infoManager;
    private TextView infoName;
    private TextView infoSports;
    private TextView infoRegion;
    private TextView infoDate;
    private TextView infoCapacity;
    private Button info_back;
    private Button join;
    private Profile userProfile;
    private Event event;
    private Profile eventHolder;
    private DatabaseReference mDatabase;

    eventListMain priorClass = (eventListMain) eventListMain.eventListMainActivity;
    searchEvent ppriorClass = (searchEvent) searchEvent.searchEventActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(EventInfo.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventinfo);
        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        event = (Event) intent.getSerializableExtra("eventInfo");
        eventHolder = (Profile) intent.getSerializableExtra("holder");
        if(((AlarmCondition) intent.getSerializableExtra("cond")) != null &&
                ((AlarmCondition) intent.getSerializableExtra("cond")).condition == 1){
            stopService(new Intent(getApplicationContext(), AlarmService.class));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        infoManager = (TextView) findViewById(R.id.info_manager);
        infoName = (TextView) findViewById(R.id.info_name);
        infoSports = (TextView) findViewById(R.id.info_sports);
        infoRegion = (TextView) findViewById(R.id.info_region);
        infoDate = (TextView) findViewById(R.id.info_date);
        infoCapacity = (TextView) findViewById(R.id.info_capacity);
        info_back = (Button) findViewById(R.id.info_back);
        join = (Button) findViewById(R.id.info_joinEvent);

        infoManager.setText(eventHolder.name+"\n"+eventHolder.email);
        infoName.setText(event.eventName);
        infoSports.setText(event.sports);
        infoRegion.setText(event.city+"-"+event.gu);
        infoDate.setText(event.date);
        String capacity = null;
        if(event.sports.equals("Soccer")){
            capacity = ""+event.users.size()+"/25";
        }else if(event.sports.equals("BasketBall")){
            capacity = ""+event.users.size()+"/15";
        }else{
            capacity = ""+event.users.size()+"/25";
        }
        infoCapacity.setText(capacity);

        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userProfile.eventInfo.length()>0){
                    Toast.makeText(EventInfo.this, "You already have Sport Event", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(event.sports.equals("Soccer")){
                    if(event.users.size()>25){
                        Toast.makeText(EventInfo.this, "It's over capacity.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if(event.sports.equals("BasketBall")){
                    if(event.users.size()>14){
                        Toast.makeText(EventInfo.this, "It's over capacity.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    if(event.users.size()>25){
                        Toast.makeText(EventInfo.this, "It's over capacity.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                userProfile.setEventInfo(event.holder);
                mDatabase.child("users").child(user.getUid()).setValue(userProfile);
                event.joining(userProfile.email);
                mDatabase.child("event").child(event.holder).setValue(event);
                Toast.makeText(EventInfo.this, "Successfully join!.", Toast.LENGTH_SHORT).show();
                Intent intentService = new Intent(getApplicationContext(), CancelEventNotificationService.class );
                startService(intentService);
                Intent intent2Service = new Intent(getApplicationContext(), MatchPlanService.class );
                startService(intent2Service);
                priorClass.finish();
                ppriorClass.finish();
                finish();


            }
        });






    }

    @Override
    protected void onDestroy(){
        userProfile = null;
        event = null;
        eventHolder = null;
        super.onDestroy();
    }


}
