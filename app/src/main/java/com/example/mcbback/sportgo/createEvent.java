package com.example.mcbback.sportgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class createEvent extends AppCompatActivity {
    ArrayAdapter<CharSequence> add_city, add_gu;
    String choice_city = null;
    String choice_gu = null;
    String choice_date = null;
    String choice_name = null;
    String choice_sports = null;
    DatePicker eventdate=null;
    private Button btnCancel;
    private Button btn_create;
    private Spinner city_spin;
    private Spinner gu_spin;
    private Spinner sports_spin;
    private EditText editName;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private Profile userProfile;
    private Event event;
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(createEvent.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        editName = (EditText)findViewById(R.id.create_Name);
        city_spin = (Spinner) findViewById(R.id.create_city);
        gu_spin = (Spinner) findViewById(R.id.create_gu);
        sports_spin = (Spinner) findViewById(R.id.create_SportsName);
        btn_create = (Button) findViewById(R.id.event_create);
        btnCancel = (Button) findViewById(R.id.create_cancel);

        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");






        add_city = ArrayAdapter.createFromResource(this, R.array.spinner_city, android.R.layout.simple_spinner_dropdown_item);

        city_spin.setAdapter(add_city);
        city_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (add_city.getItem(i).equals("Seoul")) {
                    choice_city = "Seoul";
                    add_gu = ArrayAdapter.createFromResource(createEvent.this, R.array.spinner_city_seoul, android.R.layout.simple_spinner_dropdown_item);
                    add_gu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    gu_spin.setAdapter(add_gu);
                    gu_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int j, long id) {
                            choice_gu = add_gu.getItem(j).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (add_city.getItem(i).equals("Incheon")) {
                    choice_city = "Incheon";
                    add_gu = ArrayAdapter.createFromResource(createEvent.this, R.array.spinner_city_incheon, android.R.layout.simple_spinner_dropdown_item);
                    add_gu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    gu_spin.setAdapter(add_gu);
                    gu_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            choice_gu = add_gu.getItem(i).toString();
                        }
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eventdate = (DatePicker) findViewById(R.id.create_Date);
        eventdate.init(eventdate.getYear(), eventdate.getMonth(), eventdate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                choice_date = String.format("%d/ %d/ %d",year,monthOfYear+1,dayOfMonth);
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                choice_sports = sports_spin.getSelectedItem().toString().trim();
                choice_name  = editName.getText().toString().trim();

                choice_city = city_spin.getSelectedItem().toString().trim();
                choice_gu = gu_spin.getSelectedItem().toString().trim();
                if( choice_name.length() == 0 ) {
                    Toast.makeText(createEvent.this, "Event Name을 입력하세요!", Toast.LENGTH_SHORT).show();
                    editName.requestFocus();
                    return;
                }
                if( choice_sports == null ) {
                    Toast.makeText(createEvent.this, "Sports를 입력하세요!", Toast.LENGTH_SHORT).show();
                    sports_spin.requestFocus();
                    return;
                }if( choice_city == null ) {
                    Toast.makeText(createEvent.this, "City를 입력하세요!", Toast.LENGTH_SHORT).show();
                    city_spin.requestFocus();
                    return;
                }if( choice_gu == null ) {
                    Toast.makeText(createEvent.this, "구 를 입력하세요!", Toast.LENGTH_SHORT).show();
                    gu_spin.requestFocus();
                    return;
                }
                if( choice_date == null ) {
                    Toast.makeText(createEvent.this, "Date를 입력하세요!", Toast.LENGTH_SHORT).show();
                    eventdate.requestFocus();
                    return;
                }
                mDatabase = FirebaseDatabase.getInstance().getReference();
                event = new Event(choice_name, choice_sports, choice_city, choice_gu, choice_date, user.getUid());
                event.joining(userProfile.email);
                try{
                    mDatabase.child("event").child(user.getUid()).setValue(event);
                    userProfile.setEventInfo(user.getUid());
                    mDatabase.child("users").child(user.getUid()).setValue(userProfile);
                    Toast.makeText(createEvent.this, "Event Created Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), myEvent.class);
                    intent.putExtra("userProfile", userProfile);
                    intent.putExtra("userEvent", event);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    Toast.makeText(createEvent.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
