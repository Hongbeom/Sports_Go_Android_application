package com.example.mcbback.sportgo;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class searchEvent extends AppCompatActivity {
    public static Activity searchEventActivity;
    ArrayAdapter<CharSequence> add_city, add_gu;
    String choice_city = "";
    String choice_gu = "";
    String choice_date = "";
    String choice_sports = "";
    DatePicker eventdate;
    private Button btnCancel;
    private Button btn_search;
    private Spinner city_spin;
    private Spinner gu_spin;
    private Spinner sports_spin;
    private FirebaseUser user;
    private Profile userProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_event);
        searchEventActivity = searchEvent.this;
        user= FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(searchEvent.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");

        city_spin = (Spinner) findViewById(R.id.search_city);
        gu_spin = (Spinner) findViewById(R.id.search_gu);
        sports_spin = (Spinner) findViewById(R.id.searchSportsName);
        btn_search = (Button) findViewById(R.id.event_search);
        btnCancel = (Button) findViewById(R.id.event_cancel);

        add_city = ArrayAdapter.createFromResource(this, R.array.spinner_city, android.R.layout.simple_spinner_dropdown_item);

        city_spin.setAdapter(add_city);
        city_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (add_city.getItem(i).equals("Seoul")) {
                    choice_city = "Seoul";
                    add_gu = ArrayAdapter.createFromResource(searchEvent.this, R.array.spinner_city_seoul, android.R.layout.simple_spinner_dropdown_item);
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
                    add_gu = ArrayAdapter.createFromResource(searchEvent.this, R.array.spinner_city_incheon, android.R.layout.simple_spinner_dropdown_item);
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

        eventdate = (DatePicker) findViewById(R.id.search_Date);
        eventdate.init(eventdate.getYear(), eventdate.getMonth(), eventdate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String msg = String.format("%d/ %d/ %d",year,monthOfYear+1,dayOfMonth);
                choice_date = msg;
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                choice_sports = sports_spin.getSelectedItem().toString();
                choice_city = city_spin.getSelectedItem().toString().trim();
                choice_gu = gu_spin.getSelectedItem().toString().trim();

                SearchInfo searchInfo = new SearchInfo(choice_sports, choice_city, choice_gu, choice_date);
                Intent intent = new Intent(getApplicationContext(), eventListMain.class);
                intent.putExtra("userProfile", userProfile);
                intent.putExtra("searchInfo", searchInfo);
                startActivity(intent);
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