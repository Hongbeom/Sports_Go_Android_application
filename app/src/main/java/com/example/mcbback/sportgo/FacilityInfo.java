package com.example.mcbback.sportgo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class FacilityInfo extends AppCompatActivity {

    private FirebaseUser user;
    private TextView fifoname;
    private ImageView fimage;
    private TextView f_address;
    private TextView f_capacity;
    private TextView f_information;
    private TextView f_link;
    private Button fifo_back;
    private Button map_btn;
    private Profile userProfile;
    private Event event;
    private FacilityList faclist;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(FacilityInfo.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_info);
        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        event = (Event) intent.getSerializableExtra("userEvent");
        faclist = (FacilityList) intent.getSerializableExtra("fac_info");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        fifoname = (TextView) findViewById(R.id.fifo_name);
        f_address= (TextView) findViewById(R.id.fifo_address);
        f_capacity = (TextView) findViewById(R.id.fifo_cap);
        f_information = (TextView) findViewById(R.id.fifo_info);
        f_link = (TextView) findViewById(R.id.fifo_url);
        fimage = (ImageView) findViewById(R.id.imageView3);
        map_btn = (Button) findViewById(R.id.fifo_map);
        fifo_back = (Button) findViewById(R.id.fifo_back);

        fifoname.setText(faclist.list_facilityName);
        f_address.setText(faclist.list_facilityAddress);
        f_capacity.setText(faclist.list_facilityCapacity);
        f_information.setText(faclist.list_facilityInfo);
        f_link.setText(faclist.list_facility_url);
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try{
            is = am.open(faclist.list_facilityPicture);

            Bitmap bm = BitmapFactory.decodeStream(is);

            fimage.setImageBitmap(bm);
            is.close();
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }



        fifo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Showmap.class);
                intent.putExtra("place_address", faclist.list_facilityAddress);
                startActivity(intent);
            }
        });







    }


}
