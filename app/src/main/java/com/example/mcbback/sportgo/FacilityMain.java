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

public class FacilityMain extends AppCompatActivity {

    private ListView mfacilityView = null;
    private Button btnback;
    private FirebaseUser user;
    private Profile userProfile;
    private Profile eventHolder;
    private Event event;
    private SearchInfo searchInfo;
    private DatabaseReference mDatabase;
    private ArrayList<FacilityList> facilitydata;
    private FacilityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_facility);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(FacilityMain.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
        userProfile = (Profile)intent.getSerializableExtra("userProfile");
        event = (Event) intent.getSerializableExtra("userEvent");


        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnback = (Button) findViewById(R.id.back_facility);


        facilitydata = new ArrayList<>();

        mfacilityView = (ListView)findViewById(R.id.facilityListView);


        mDatabase.child("facility").child(event.city).child(event.gu).child(event.sports).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Facility tmp = snapshot.getValue(Facility.class);
                    FacilityList faclist = new FacilityList(tmp.fac_name, tmp.fac_image, tmp.fac_address, tmp.fac_capacity, tmp.fac_info, tmp.fac_url);
                    facilitydata.add(faclist);
                }
                adapter = new FacilityAdapter(facilitydata);
                mfacilityView.setAdapter(adapter);
                mfacilityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        FacilityList list = facilitydata.get(position);
                        Intent intent = new Intent(getApplicationContext(), FacilityInfo.class);
                        intent.putExtra("userProfile", userProfile);
                        intent.putExtra("userEvent", event);
                        intent.putExtra("fac_info", list);
                        startActivity(intent);

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
