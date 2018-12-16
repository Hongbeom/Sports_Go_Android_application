package com.example.mcbback.sportgo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AlarmEvent extends AppCompatActivity {
    private ArrayAdapter<CharSequence> add_city, add_gu;
    private Switch activation;
    private String choice_city = "";
    private String choice_gu = "";
    private String choice_sports = "";
    private int sports_position;
    private int city_position;
    private int gu_position;
    private Spinner city_spin;
    private Spinner gu_spin;
    private Spinner sports_spin;
    private Button alarm_closeBtn;
    private TextView currentCondition;
    private Profile userProfile;
    private FirebaseUser user;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(AlarmEvent.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmevent);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        activation = (Switch) findViewById(R.id.alarm_switch);
        city_spin = (Spinner) findViewById(R.id.alarm_city);
        gu_spin = (Spinner) findViewById(R.id.alarm_gu);
        sports_spin = (Spinner) findViewById(R.id.alarm_sports);
        alarm_closeBtn = (Button) findViewById(R.id.alarm_cancle);
        currentCondition = (TextView) findViewById(R.id.currentCondition);

        add_city = ArrayAdapter.createFromResource(this, R.array.spinner_city, android.R.layout.simple_spinner_dropdown_item);
        city_spin.setAdapter(add_city);
        city_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                if (add_city.getItem(i).equals("Seoul")) {
                    choice_city = "Seoul";
                    add_gu = ArrayAdapter.createFromResource(AlarmEvent.this, R.array.spinner_city_seoul, android.R.layout.simple_spinner_dropdown_item);
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
                    add_gu = ArrayAdapter.createFromResource(AlarmEvent.this, R.array.spinner_city_incheon, android.R.layout.simple_spinner_dropdown_item);
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
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        boolean check = pref.getBoolean("check1",false); // <---아마도 이렇게 불러오면 됩니다. 여기서 false는 default 값일껍니다.
        if(check){
            choice_sports = pref.getString("sport", "");
            choice_city = pref.getString("city", "");
            choice_gu = pref.getString("gu", "");


            sports_spin.setEnabled(false);
            city_spin.setEnabled(false);
            gu_spin.setEnabled(false);
            currentCondition.setText("Current Condition\n"+"Sports: "+ choice_sports+"\nRegion: "+choice_city+"-"+choice_gu);

            activation.setChecked(true);
        }
        activation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    choice_sports = sports_spin.getSelectedItem().toString();
                    choice_city = city_spin.getSelectedItem().toString().trim();
                    choice_gu = gu_spin.getSelectedItem().toString().trim();

                    if(choice_city.length()==0 || choice_gu.length() ==0 || choice_sports.length()==0){
                        Toast.makeText(AlarmEvent.this,"Please, select your conditions, Not default", Toast.LENGTH_SHORT).show();
                        activation.setChecked(false);
                        return;
                    }
                    currentCondition.setText("Current Condition\n"+"Sports: "+ choice_sports+"\nRegion: "+choice_city+"-"+choice_gu);
                    city_spin.setEnabled(false);
                    gu_spin.setEnabled(false);
                    sports_spin.setEnabled(false);
                    userProfile.setCondtion(choice_sports, choice_city, choice_gu);
                    mDatabase.child("users").child(user.getUid()).setValue(userProfile);
                    Intent intentService = new Intent(getApplicationContext(), AlarmService.class );
                    startService(intentService);

                }
                else{
                    currentCondition.setText("");
                    city_spin.setEnabled(true);
                    gu_spin.setEnabled(true);
                    sports_spin.setEnabled(true);
                    userProfile.setCondtion("", "", "");
                    mDatabase.child("users").child(user.getUid()).setValue(userProfile);
                    stopService(new Intent(getApplicationContext(), AlarmService.class));

                }
            }
        });

        alarm_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sports_position = sports_spin.getSelectedItemPosition();
                city_position = city_spin.getSelectedItemPosition();
                gu_position = gu_spin.getSelectedItemPosition();
                SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);  // UI 상태를 저장합니다.
                SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다

                editor.putBoolean("check1", activation.isChecked()); // 저장할 값들을 입력합니다.
                editor.putString("sport", choice_sports);
                editor.putString("city", choice_city);
                editor.putString("gu", choice_gu);
                editor.commit();  // 저장합니다.
                finish();
            }
        });




    }
    @Override
    public void onBackPressed(){
        sports_position = sports_spin.getSelectedItemPosition();
        city_position = city_spin.getSelectedItemPosition();
        gu_position = gu_spin.getSelectedItemPosition();
        SharedPreferences pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);  // UI 상태를 저장합니다.
        SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다

        editor.putBoolean("check1", activation.isChecked()); // 저장할 값들을 입력합니다.
        editor.putString("sport", choice_sports);
        editor.putString("city", choice_city);
        editor.putString("gu", choice_gu);
        editor.commit();  // 저장합니다.
        finish();

    }

}
