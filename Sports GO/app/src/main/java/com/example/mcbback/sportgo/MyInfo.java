package com.example.mcbback.sportgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;




public class MyInfo extends AppCompatActivity {
    private TextView my_info_email;
    private TextView my_info_name;
    private TextView my_info_sex;
    private TextView my_info_region;
    private TextView my_info_evaluation;
    private Button my_info_closeBtn;
    private Profile userProfile;
    private FirebaseUser user;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(MyInfo.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);

        my_info_email = (TextView) findViewById(R.id.my_info_email);
        my_info_name = (TextView) findViewById(R.id.my_info_name);
        my_info_sex = (TextView) findViewById(R.id.my_info_sex);
        my_info_region = (TextView) findViewById(R.id.my_info_region);
        my_info_evaluation = (TextView) findViewById(R.id.my_info_evaluation);
        my_info_closeBtn = (Button) findViewById(R.id.my_info_closebtn);


        my_info_email.setText(userProfile.email);
        my_info_name.setText(userProfile.name);
        my_info_sex.setText(userProfile.sex);
        my_info_region.setText(userProfile.city + "-" + userProfile.gu);
        my_info_evaluation.setText(Math.round(userProfile.reliability * 10) / 10 + " ");

        my_info_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
