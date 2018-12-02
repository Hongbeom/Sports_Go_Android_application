package com.example.mcbback.sportgo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myEvent extends AppCompatActivity {
    private Button btnBack;
    private TextView tvEventManager;
    private TextView tvEventName;
    private TextView tvEventSports;
    private TextView tvEventRegion;
    private TextView tvEventDate;
    private TextView tvEventCapacity;
    private Button cancelEvent_btn;
    private Button chatting_btn;
    private Button userList_btn;
    private Button notifiy_btn;
    private Button facililty_btn;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private Profile userProfile;
    private Profile holderProfile;
    private Event userEvent;


    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(myEvent.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        userEvent = (Event) intent.getSerializableExtra("userEvent");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_event);

        tvEventManager = (TextView) findViewById(R.id.my_manager);
        tvEventName = (TextView) findViewById(R.id.my_name);
        tvEventSports = (TextView) findViewById(R.id.my_sports);
        tvEventRegion = (TextView) findViewById(R.id.my_region);
        tvEventDate = (TextView) findViewById(R.id.my_date);
        tvEventCapacity = (TextView) findViewById(R.id.my_capacity);
        cancelEvent_btn = (Button) findViewById(R.id.my_cancelEvent);
        chatting_btn = (Button) findViewById(R.id.my_chatting);
        userList_btn = (Button) findViewById(R.id.my_user);
        notifiy_btn = (Button) findViewById(R.id.my_notify);
        facililty_btn = (Button) findViewById(R.id.my_facility);

        if(!user.getUid().equals(userEvent.holder) || userEvent.matchPlan != null ){
            notifiy_btn.setText("VIEW MATCH PLAN");
        }


        tvEventName.setText(userEvent.eventName);
        tvEventSports.setText(userEvent.sports);
        tvEventRegion.setText(userEvent.city+"-"+userEvent.gu);
        tvEventDate.setText(userEvent.date);
        String capacity = null;
        if(userEvent.sports.equals("Soccer")){
            capacity = ""+userEvent.users.size()+"/25";
        }else if(userEvent.sports.equals("BasketBall")){
            capacity = ""+userEvent.users.size()+"/15";
        }else{
            capacity = ""+userEvent.users.size()+"/25";
        }
        tvEventCapacity.setText(capacity);

        mDatabase.child("users").child(userEvent.holder).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        holderProfile = dataSnapshot.getValue(Profile.class);
                        tvEventManager.setText(holderProfile.name+"\n"+holderProfile.email);





                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(myEvent.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                    }
                });



        btnBack = (Button) findViewById(R.id.my_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancelEvent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myEvent.this);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure to cancel the Event?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelEvent();
                    }
                });
                builder.show();
            }
        });

        chatting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), chatmain.class);
                intent.putExtra("userProfile", userProfile);
                intent.putExtra("userEvent", userEvent);
                startActivity(intent);
            }
        });

        userList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserListMain.class);
                intent.putExtra("userProfile", userProfile);
                intent.putExtra("userEvent", userEvent);
                startActivity(intent);
            }
        });
        facililty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FacilityMain.class);
                intent.putExtra("userProfile", userProfile);
                intent.putExtra("userEvent", userEvent);
                startActivity(intent);
            }
        });
        notifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notifiy_btn.getText().equals("NOTIFY MATCH PLAN")){
                    Intent intent = new Intent(getApplicationContext(), MatchPlanCreate.class);
                    intent.putExtra("userProfile", userProfile);
                    intent.putExtra("userEvent", userEvent);
                    startActivityForResult(intent, 18);
                }
                else{
                    if(userEvent.matchPlan == null){
                        Toast.makeText(myEvent.this, "There is no match plan yet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), MatchInfo.class);
                    intent.putExtra("userProfile", userProfile);
                    intent.putExtra("userEvent", userEvent);
                    startActivity(intent);
                }

            }
        });




    }

    private void cancelEvent(){
        if(!user.getUid().equals(userEvent.holder)){
            userProfile.setEventInfo("");
            mDatabase.child("users").child(user.getUid()).setValue(userProfile);
            userEvent.deletePlayer(userProfile.email);
            mDatabase.child("event").child(userEvent.holder).setValue(userEvent);
            Toast.makeText(myEvent.this, "Event has been canceled", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            mDatabase.child("event").child(userEvent.holder).removeValue();
            mDatabase.child("chat").child(user.getUid()).removeValue();
            mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Profile tmp = snapshot.getValue(Profile.class);
                        if(tmp.eventInfo.equals(userEvent.holder) ){
                            tmp.setEventInfo("");
                            mDatabase.child("users").child(snapshot.getKey()).setValue(tmp);
                        }



                    }
                    Toast.makeText(myEvent.this, "Event has been canceled", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data){
        if(requestCode == 18){
            if(resultCode == RESULT_OK){
                String success = data.getExtras().getString("Success");
                if(success.equals("Success")){
                    notifiy_btn.setText("VIEW MATCH PLAN");
                    mDatabase.child("event").child(userEvent.holder).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get user value
                                    userEvent = dataSnapshot.getValue(Event.class);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(myEvent.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                                }
                            });
                }


            }
        }
    }



}
