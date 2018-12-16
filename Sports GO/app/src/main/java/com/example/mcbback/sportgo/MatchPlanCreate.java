package com.example.mcbback.sportgo;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchPlanCreate extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private Profile userProfile;
    private Event event;
    private String choice_date = null;
    private String choice_time = null;
    private String choice_comment = null;
    private DatePicker eventdate;
    private TimePicker eventtime;
    private TextView place;
    private EditText comment;
    private Button find_btn;
    private Button notify_btn;
    private Button cancel_btn;
    private String address = null;


    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(MatchPlanCreate.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        event = (Event) intent.getSerializableExtra("userEvent");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notify);

        place = (TextView) findViewById(R.id.notify_Place);
        comment = (EditText)findViewById(R.id.notify_Comment);
        find_btn = (Button)findViewById(R.id.notify_find);
        notify_btn = (Button)findViewById(R.id.notify_Notice);
        cancel_btn = (Button)findViewById(R.id.notify_Cancel);


        eventdate = (DatePicker) findViewById(R.id.notify_Date);
        eventdate.init(eventdate.getYear(), eventdate.getMonth(), eventdate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String msg = String.format("%d/ %d/ %d",year,monthOfYear+1,dayOfMonth);
                choice_date = msg;
            }
        });

        eventtime = (TimePicker)findViewById(R.id.notify_Time);
        eventtime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(minute<10){
                    choice_time = hourOfDay+":0"+minute;
                }else{
                    choice_time = hourOfDay+":"+minute;
                }

            }
        });

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 15);
            }
        });

        notify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice_comment = comment.getText().toString();
                if(choice_date == null || choice_time == null  || address == null){

                    Toast.makeText(MatchPlanCreate.this, "Please, set the date or time or place first", Toast.LENGTH_SHORT).show();
                    return;
                }

                event.setMatchPlan(choice_date, choice_time, comment.getText().toString().trim(), address);

                mDatabase.child("event").child(event.holder).setValue(event);
                Toast.makeText(MatchPlanCreate.this, "Match plan has been registered successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                intent.putExtra("Success", "Success");
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 15){
            if(resultCode == RESULT_OK){
                address = data.getExtras().getString("place_address");
                place.setText(address);

            }
        }
    }
}