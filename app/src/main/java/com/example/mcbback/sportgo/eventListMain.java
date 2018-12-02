package com.example.mcbback.sportgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class eventListMain extends AppCompatActivity {

    public static Activity eventListMainActivity;

    private ListView meventView = null;
    private Button btnback;
    private FirebaseUser user;
    private Profile userProfile;
    private Profile eventHolder;
    private Event event;
    private SearchInfo searchInfo;
    private DatabaseReference mDatabase;
    private ArrayList<eventList> eventdata;
    private eventListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(eventListMain.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event);
        eventListMainActivity = eventListMain.this;
        Intent intent = getIntent();
        userProfile = (Profile)intent.getSerializableExtra("userProfile");
        searchInfo = (SearchInfo)intent.getSerializableExtra("searchInfo");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnback = (Button) findViewById(R.id.back);
        eventdata = new ArrayList<>();



        // 데이트가 선택 안되었을때
        if(searchInfo.date.length()==0){
            mDatabase.child("event").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Event tmp = snapshot.getValue(Event.class);
                        String capacity = null;
                        if(tmp.sports.equals("Soccer")){
                            capacity = ""+tmp.users.size()+"/25";
                        }else if(tmp.sports.equals("BasketBall")){
                            capacity = ""+tmp.users.size()+"/15";
                        }else{
                            capacity = ""+tmp.users.size()+"/25";
                        }
                        if (tmp.sports.equals(searchInfo.sports) && tmp.city.equals(searchInfo.city) && tmp.gu.equals(searchInfo.gu)) {
                            eventList e_list = new eventList(tmp.eventName, tmp.sports, tmp.date, tmp.holder, capacity);
                            eventdata.add(e_list);

                        }
                    }
                    meventView = (ListView)findViewById(R.id.eventListView);
                    adapter = new eventListAdapter(eventdata);
                    meventView.setAdapter(adapter);

                    meventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            eventList list = eventdata.get(position);
                            showEventInfo(list.holder);

                        }
                    });



                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }else{  // 모든게 선택 되었을때
            mDatabase.child("event").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        Event tmp = snapshot.getValue(Event.class);
                        String capacity = null;
                        if(tmp.sports.equals("Soccer")){
                            capacity = ""+tmp.users.size()+"/25";
                        }else if(tmp.sports.equals("BasketBall")){
                            capacity = ""+tmp.users.size()+"/15";
                        }else{
                            capacity = ""+tmp.users.size()+"/25";
                        }
                        if(tmp.sports.equals(searchInfo.sports) && tmp.city.equals(searchInfo.city) && tmp.gu.equals(searchInfo.gu) && tmp.date.equals(searchInfo.date)){
                            eventList e_list = new eventList(tmp.eventName, tmp.sports, tmp.date, tmp.holder, capacity);
                            eventdata.add(e_list);

                        }
                    }
                    meventView = (ListView)findViewById(R.id.eventListView);
                    adapter = new eventListAdapter(eventdata);
                    meventView.setAdapter(adapter);
                    meventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            eventList list = eventdata.get(position);
                            showEventInfo(list.holder);
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }









        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showEventInfo(String holder){
        mDatabase.child("event").child(holder).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        event = dataSnapshot.getValue(Event.class);
                        mDatabase.child("users").child(event.holder).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Get user value
                                        eventHolder = dataSnapshot.getValue(Profile.class);
                                        Intent intent = new Intent(getApplicationContext(), EventInfo.class);
                                        intent.putExtra("userProfile", userProfile);
                                        intent.putExtra("eventInfo", event);
                                        intent.putExtra("holder", eventHolder);
                                        startActivity(intent);

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(eventListMain.this, "db error! -- can not receive event info", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(eventListMain.this, "db error! -- can not receive event info", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
